package org.nucleus8583.oim.xml;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.nucleus8583.oim.field.Alignment;
import org.nucleus8583.oim.field.Field;
import org.nucleus8583.oim.field.spi.Basic;
import org.nucleus8583.oim.field.type.Type;

import rk.commons.inject.annotation.Init;
import rk.commons.inject.annotation.Inject;
import rk.commons.inject.factory.ObjectFactory;
import rk.commons.inject.factory.ObjectInstantiationException;
import rk.commons.inject.factory.support.FactoryObject;
import rk.commons.inject.factory.support.ObjectDefinitionValueResolver;
import rk.commons.inject.factory.type.converter.TypeConverterResolver;
import rk.commons.inject.util.AnnotationHelper;
import rk.commons.inject.util.PropertyHelper;

public class BasicFactory extends FactoryObject<Field> {

	protected int no;
	
	protected String name;

	protected Type type;

	protected Map<String, Object> properties;
	
	@Inject
	protected ObjectFactory factory;

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
			
			PropertyHelper.applyPropertyValues(name, type, properties, valueResolver, typeResolver);
		}
		
		List<Method> methods = AnnotationHelper.findAnnotatedMethods(type.getClass(), Init.class);
		for (Method m : methods) {
			m.setAccessible(true);
			
			try {
				m.invoke(type);
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
