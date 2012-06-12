package org.nucleus8583.core.xml;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

import org.nucleus8583.core.MessageSerializer;
import org.nucleus8583.core.field.Alignment;
import org.nucleus8583.core.field.Field;
import org.nucleus8583.core.field.Type;

import rk.commons.inject.annotation.Init;
import rk.commons.inject.annotation.Inject;
import rk.commons.inject.factory.ObjectFactory;
import rk.commons.inject.factory.ObjectInstantiationException;
import rk.commons.inject.factory.support.FactoryObject;
import rk.commons.inject.factory.support.ObjectDefinitionValueResolver;
import rk.commons.inject.factory.type.converter.TypeConverterResolver;
import rk.commons.inject.util.AnnotationHelper;
import rk.commons.inject.util.PropertyHelper;

public class FieldFactory extends FactoryObject<Field> {

	private int no;

	private Type<?> type;

	private Map<String, Object> properties;
	
	private MessageSerializer serializer;
	
	@Inject
	private String objectName;
	
	@Inject
	private ObjectFactory factory;

	public void setNo(int no) {
		this.no = no;
	}

	public void setType(Type<?> type) {
		this.type = type;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
	public void setMessageSerializer(MessageSerializer serializer) {
		this.serializer = serializer;
	}

	public void setObjectQName(String objectQName) {
		this.objectName = objectQName;
	}

	public void setObjectFactory(ObjectFactory factory) {
		this.factory = factory;
	}

	protected Field createInstance() {
		Type<?> type;
		
		try {
			type = this.type.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError("clone operation not supported by class " + this.type.getClass());
		}
		
		if ((properties != null) && !properties.isEmpty()) {
			TypeConverterResolver typeConverterResolver = new TypeConverterResolver();
			typeConverterResolver.register(String.class, Alignment.class, new StringToAlignmentConverter());
			
			ObjectDefinitionValueResolver valueResolver = new ObjectDefinitionValueResolver(factory);
			
			PropertyHelper.applyPropertyValues(objectName, type, properties, valueResolver, typeConverterResolver);
		}
		
		List<Method> methods = AnnotationHelper.findAnnotatedMethods(type.getClass(), Init.class);
		for (Method m : methods) {
			m.setAccessible(true);
			
			try {
				m.invoke(type);
			} catch (Exception e) {
				throw new ObjectInstantiationException(objectName, e);
			}
		}
		
		if (serializer != null) {
			Class<?> typeClass = type.getClass();
			
			Method cloneMethod;
			
			try {
				cloneMethod = typeClass.getMethod("clone");
			} catch (Throwable t) {
				throw new AssertionError("no clone method defined at class " + typeClass);
			}
			
			Class<?> valueType = (Class<?>) ((ParameterizedType) cloneMethod.getGenericReturnType()).getActualTypeArguments()[0];
			
			if (String.class.equals(valueType)) {
				type = (Type<?>) Proxy.newProxyInstance(typeClass.getClassLoader(), new Class<?>[] { Type.class },
						new TypeInvocationHandlerForString(type, serializer));
			} else if (byte[].class.equals(valueType)) {
				type = (Type<?>) Proxy.newProxyInstance(typeClass.getClassLoader(), new Class<?>[] { Type.class },
						new TypeInvocationHandlerForBytes(type, serializer));
			} else {
				throw new ObjectInstantiationException(objectName, new ClassCastException(
						"class " + typeClass + " is not java.lang.String or byte[] based Type, it is " + valueType + " based Type"));
			}
		}
		
		return new Field(no, type);
	}
}
