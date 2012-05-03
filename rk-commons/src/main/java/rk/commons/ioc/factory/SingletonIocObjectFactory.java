package rk.commons.ioc.factory;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rk.commons.ioc.factory.config.ObjectDefinition;
import rk.commons.ioc.factory.support.DisposableObject;
import rk.commons.ioc.factory.support.InitializingObject;
import rk.commons.ioc.factory.support.IocObjectFactoryAware;
import rk.commons.ioc.factory.support.ObjectDefinitionRegistry;
import rk.commons.ioc.factory.support.ObjectDefinitionValueResolver;
import rk.commons.ioc.factory.support.ObjectFactory;
import rk.commons.ioc.factory.support.ObjectQNameAware;
import rk.commons.ioc.factory.type.converter.TypeConverterResolver;
import rk.commons.loader.ResourceLoader;
import rk.commons.logging.Logger;
import rk.commons.logging.LoggerFactory;
import rk.commons.util.ObjectUtils;
import rk.commons.util.StringUtils;

public class SingletonIocObjectFactory implements IocObjectFactory, ObjectDefinitionRegistry {
	
	private static final Logger log = LoggerFactory.getLogger(SingletonIocObjectFactory.class);

	private final Map<String, Object> singletons;
	
	private final TypeConverterResolver typeConverterResolver;

	private final ObjectDefinitionValueResolver valueResolver;

	private final Map<String, ObjectDefinition> definitions;
	
	private final ResourceLoader resourceLoader;

	public SingletonIocObjectFactory(ResourceLoader resourceLoader) {
		singletons = Collections.synchronizedMap(new HashMap<String, Object>());
		
		typeConverterResolver = new TypeConverterResolver();

		valueResolver = new ObjectDefinitionValueResolver(this);

		definitions = Collections.synchronizedMap(new HashMap<String, ObjectDefinition>());
		
		this.resourceLoader = resourceLoader;
	}
	
	public TypeConverterResolver getTypeConverterResolver() {
		return typeConverterResolver;
	}

	public boolean containsObject(String objectQName) {
		return singletons.containsKey(objectQName);
	}

	public Object getObject(String objectQName) {
		Object object;

		synchronized (singletons) {
			if (singletons.containsKey(objectQName)) {
				object = singletons.get(objectQName);
			} else {
				object = createObject(getObjectDefinition(objectQName));
			}

			if (object instanceof ObjectFactory<?>) {
				object = ((ObjectFactory<?>) object).getObject();
			}
		}

		return object;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getObjectsOfType(Class<T> type) {
		List<T> beans = new ArrayList<T>();

		synchronized (singletons) {
			for (Object o : singletons.values()) {
				if (type.isInstance(o)) {
					beans.add((T) o);
				}
			}
		}

		return beans;
	}

	public String[] getObjectQNames() {
		Set<String> set = new HashSet<String>();
		set.addAll(singletons.keySet());
		set.addAll(definitions.keySet());

		return set.toArray(StringUtils.EMPTY_STRING_ARRAY);
	}

	public Object createObject(final ObjectDefinition definition) {
		final String objectQName = definition.getObjectQName();
		
		Object object;
		
		synchronized (singletons) {
			if (singletons.containsKey(objectQName)) {
				return singletons.get(objectQName);
			}
			
			try {
				object = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
					
					public Object run() throws Exception {
						return doCreateObject(definition);
					}
				});
			} catch (PrivilegedActionException e) {
				throw new ObjectInstantiationException(objectQName, e.getException());
			} catch (Throwable cause) {
				throw new ObjectInstantiationException(objectQName, cause);
			}
			
			singletons.put(objectQName, object);
		}
		
		return object;
	}
	
	protected Object doCreateObject(ObjectDefinition definition) throws Exception {
		String objectQName = definition.getObjectQName();
		
		String xobjectQName = definition.getExtends();
		
		ObjectDefinition exdefinition;
		
		if (StringUtils.hasText(xobjectQName, true)) {
			exdefinition = getObjectDefinition(xobjectQName);
		} else {
			exdefinition = null;
		}
		
		// resolve object class
		definition.resolveClass(resourceLoader);
		
		// create object instance
		Constructor<?> ctor;
		
		try {
			ctor = definition.getObjectClass().getDeclaredConstructor();

			ctor.setAccessible(true);
		} catch (NoSuchMethodException e) {
			ctor = definition.getObjectClass().getConstructor();
		}
		
		Object object = ctor.newInstance();
		
		// merge property values
		Map<String, Object> merged = new HashMap<String, Object>();
		
		if (exdefinition != null) {
			merged.putAll(exdefinition.getPropertyValues());
		}
		
		merged.putAll(definition.getPropertyValues());
		
		// apply property values
		ObjectUtils.applyPropertyValues(objectQName, object, merged, valueResolver, typeConverterResolver);
		
		// post construction
		if (object instanceof ObjectQNameAware) {
			((ObjectQNameAware) object).setObjectQName(objectQName);
		}
		
		if (object instanceof IocObjectFactoryAware) {
			((IocObjectFactoryAware) object).setIocObjectFactory(this);
		}
		
		if (object instanceof InitializingObject) {
			try {
				((InitializingObject) object).initialize();
			} catch (Throwable cause) {
				throw new ObjectInstantiationException(objectQName, cause);
			}
		}
		
		return object;
	}
	
	public void destroy() {
		synchronized (singletons) {
			for (Object bean : singletons.values()) {
				if (bean instanceof DisposableObject) {
					try {
						((DisposableObject) bean).destroy();
					} catch (Throwable t) {
						// do nothing
					}
				}
			}
		}
	}
	
	public void registerObjectDefinition(ObjectDefinition definition) {
		String objectQName = definition.getObjectQName();

		synchronized (definitions) {
			if (definitions.containsKey(objectQName)) {
				log.warning("object " + objectQName + " already defined, redefined with a new one.");
			}
			
			definitions.put(objectQName, definition);
		}
	}

	public void removeObjectDefinition(String objectQName) {
		definitions.remove(objectQName);
	}

	public ObjectDefinition getObjectDefinition(String objectQName) {
		synchronized (definitions) {
			if (!definitions.containsKey(objectQName)) {
				throw new ObjectNotFoundException(objectQName);
			}

			return definitions.get(objectQName);
		}
	}

	public boolean containsObjectDefinition(String objectQName) {
		return definitions.containsKey(objectQName);
	}
}
