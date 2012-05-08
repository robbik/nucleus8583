package org.nucleus8583.oim.xml;

import java.util.Map;

import org.nucleus8583.oim.field.Alignment;
import org.nucleus8583.oim.field.Field;
import org.nucleus8583.oim.field.spi.Basic;
import org.nucleus8583.oim.field.type.Type;

import rk.commons.ioc.factory.IocObjectFactory;
import rk.commons.ioc.factory.ObjectInstantiationException;
import rk.commons.ioc.factory.support.InitializingObject;
import rk.commons.ioc.factory.support.IocObjectFactoryAware;
import rk.commons.ioc.factory.support.ObjectDefinitionValueResolver;
import rk.commons.ioc.factory.support.ObjectFactory;
import rk.commons.ioc.factory.type.converter.TypeConverterResolver;
import rk.commons.util.ObjectUtils;

public class BasicFactory extends ObjectFactory<Field> implements IocObjectFactoryAware {

	protected int no;
	
	protected String name;

	protected Type type;

	protected Map<String, Object> properties;
	
	protected IocObjectFactory factory;

	public void setNo(int no) {
		this.no = no;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public void setIocObjectFactory(IocObjectFactory factory) {
		this.factory = factory;
	}
	
	protected Type reInitializeType() {
		Type type;
		
		try {
			type = (Type) this.type.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError("clone operation not supported by class " + this.type.getClass());
		}
		
		if ((properties != null) && !properties.isEmpty()) {
			TypeConverterResolver typeResolver = new TypeConverterResolver();
			typeResolver.register(String.class, Alignment.class, new StringToAlignmentConverter());
			
			ObjectDefinitionValueResolver valueResolver = new ObjectDefinitionValueResolver(factory);
			
			ObjectUtils.applyPropertyValues(name, type, properties, valueResolver, typeResolver);
		}
		
		if (type instanceof InitializingObject) {
			try {
				((InitializingObject) type).initialize();
			} catch (Exception e) {
				throw new ObjectInstantiationException(name, e);
			}
		}
		
		return type;
	}

	protected Field createInstance() {
		Basic basic = new Basic();
		basic.setNo(no);
		basic.setName(name);
		basic.setType(reInitializeType());
		
		return basic;
	}
}
