package rk.commons.beans.factory;

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

import rk.commons.beans.factory.config.BeanDefinition;
import rk.commons.beans.factory.support.BeanDefinitionRegistry;
import rk.commons.beans.factory.support.BeanDefinitionValueResolver;
import rk.commons.beans.factory.support.BeanFactoryAware;
import rk.commons.beans.factory.support.BeanQNameAware;
import rk.commons.beans.factory.support.DisposableBean;
import rk.commons.beans.factory.support.FactoryBean;
import rk.commons.beans.factory.support.InitializingBean;
import rk.commons.beans.factory.type.converter.TypeConverterResolver;
import rk.commons.loader.ResourceLoader;
import rk.commons.util.BeanUtils;
import rk.commons.util.StringUtils;

public class SingletonBeanFactory implements BeanFactory, BeanDefinitionRegistry {

	private final Map<String, Object> singletons;
	
	private final TypeConverterResolver typeConverterResolver;

	private final BeanDefinitionValueResolver valueResolver;

	private final Map<String, BeanDefinition> definitions;
	
	private final ResourceLoader resourceLoader;

	public SingletonBeanFactory(ResourceLoader resourceLoader) {
		singletons = Collections.synchronizedMap(new HashMap<String, Object>());
		
		typeConverterResolver = new TypeConverterResolver();

		valueResolver = new BeanDefinitionValueResolver(this);

		definitions = Collections.synchronizedMap(new HashMap<String, BeanDefinition>());
		
		this.resourceLoader = resourceLoader;
	}
	
	public TypeConverterResolver getTypeConverterResolver() {
		return typeConverterResolver;
	}

	public boolean containsBean(String beanQName) {
		return singletons.containsKey(beanQName);
	}

	public Object getBean(String beanQName) {
		Object bean;

		synchronized (singletons) {
			if (singletons.containsKey(beanQName)) {
				bean = singletons.get(beanQName);
			} else {
				bean = createBean(getBeanDefinition(beanQName));
			}

			if (bean instanceof FactoryBean<?>) {
				bean = ((FactoryBean<?>) bean).getObject();
			}
		}

		return bean;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getBeansOfType(Class<T> type) {
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

	public String[] getBeanQNames() {
		Set<String> set = new HashSet<String>();
		set.addAll(singletons.keySet());
		set.addAll(definitions.keySet());

		return set.toArray(StringUtils.EMPTY_STRING_ARRAY);
	}

	public Object createBean(final BeanDefinition definition) {
		final String beanQName = definition.getBeanQName();
		
		Object bean;
		
		synchronized (singletons) {
			if (singletons.containsKey(beanQName)) {
				return singletons.get(beanQName);
			}
			
			try {
				if (System.getSecurityManager() == null) {
					bean = doCreateBean(definition);
				} else {
					try {
						bean = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
									
									public Object run() throws Exception {
										return doCreateBean(definition);
									}
								});
					} catch (PrivilegedActionException e) {
						throw e.getException();
					}
				}
			} catch (Throwable cause) {
				throw new BeanInstantiationException(beanQName, cause);
			}
			
			singletons.put(beanQName, bean);
		}
		
		return bean;
	}
	
	protected Object doCreateBean(BeanDefinition definition) throws Exception {
		String beanQName = definition.getBeanQName();
		
		String exbeanQName = definition.getExtends();
		
		BeanDefinition exdefinition;
		
		if (StringUtils.hasText(exbeanQName, true)) {
			exdefinition = getBeanDefinition(exbeanQName);
		} else {
			exdefinition = null;
		}
		
		// resolve bean class
		definition.resolveClass(resourceLoader);
		
		// create bean instance
		Object bean = definition.getBeanClass().newInstance();
		
		// merge property values
		Map<String, Object> merged = new HashMap<String, Object>();
		
		if (exdefinition != null) {
			merged.putAll(exdefinition.getPropertyValues());
		}
		
		merged.putAll(definition.getPropertyValues());
		
		// apply property values
		BeanUtils.applyPropertyValues(beanQName, bean, merged, valueResolver, typeConverterResolver);
		
		// post construction
		if (bean instanceof BeanQNameAware) {
			((BeanQNameAware) bean).setBeanQName(beanQName);
		}
		
		if (bean instanceof BeanFactoryAware) {
			((BeanFactoryAware) bean).setBeanFactory(this);
		}
		
		if (bean instanceof InitializingBean) {
			try {
				((InitializingBean) bean).initialize();
			} catch (Throwable cause) {
				throw new BeanInstantiationException(beanQName, cause);
			}
		}
		
		return bean;
	}
	
	public void destroy() {
		synchronized (singletons) {
			for (Object bean : singletons.values()) {
				if (bean instanceof DisposableBean) {
					try {
						((DisposableBean) bean).destroy();
					} catch (Throwable t) {
						// do nothing
					}
				}
			}
		}
	}
	
	public void registerBeanDefinition(BeanDefinition definition) {
		String beanQName = definition.getBeanQName();

		synchronized (definitions) {
			if (definitions.containsKey(beanQName)) {
				throw new IllegalArgumentException("definition for bean " + beanQName + " already exists");
			}

			definitions.put(beanQName, definition);
		}
	}

	public void removeBeanDefinition(String beanQName) {
		definitions.remove(beanQName);
	}

	public BeanDefinition getBeanDefinition(String beanQName) {
		synchronized (definitions) {
			if (!definitions.containsKey(beanQName)) {
				throw new BeanNotFoundException(beanQName);
			}

			return definitions.get(beanQName);
		}
	}

	public boolean containsBeanDefinition(String beanQName) {
		return definitions.containsKey(beanQName);
	}
}
