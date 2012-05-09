package org.nucleus8583.core.xml;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Map;

import org.nucleus8583.core.MessageSerializer;
import org.nucleus8583.core.field.Field;
import org.nucleus8583.core.field.Type;

import rk.commons.inject.factory.IocObjectFactory;
import rk.commons.inject.factory.ObjectInstantiationException;
import rk.commons.inject.factory.support.InitializingObject;
import rk.commons.inject.factory.support.IocObjectFactoryAware;
import rk.commons.inject.factory.support.ObjectDefinitionValueResolver;
import rk.commons.inject.factory.support.ObjectFactory;
import rk.commons.inject.factory.support.ObjectQNameAware;
import rk.commons.inject.factory.type.converter.TypeConverterResolver;
import rk.commons.util.ObjectUtils;

public class FieldFactory extends ObjectFactory<Field> implements ObjectQNameAware, IocObjectFactoryAware {

	private int no;

	private Type<?> type;

	private Map<String, Object> properties;
	
	private MessageSerializer serializer;
	
	private String objectQName;
	
	private IocObjectFactory factory;

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
		this.objectQName = objectQName;
	}

	public void setIocObjectFactory(IocObjectFactory factory) {
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
			typeConverterResolver.register(StringToAlignmentConverter.FROM, StringToAlignmentConverter.TO, new StringToAlignmentConverter());
			
			ObjectDefinitionValueResolver valueResolver = new ObjectDefinitionValueResolver(factory);
			
			ObjectUtils.applyPropertyValues(objectQName, type, properties, valueResolver, typeConverterResolver);
		}
		
		if (type instanceof InitializingObject) {
			try {
				((InitializingObject) type).initialize();
			} catch (Exception e) {
				throw new ObjectInstantiationException(objectQName, e);
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
				throw new ObjectInstantiationException(objectQName, new ClassCastException(
						"class " + typeClass + " is not java.lang.String or byte[] based Type, it is " + valueType + " based Type"));
			}
		}
		
		return new Field(no, type);
	}
}
