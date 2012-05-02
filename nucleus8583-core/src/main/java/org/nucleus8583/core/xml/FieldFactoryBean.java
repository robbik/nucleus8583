package org.nucleus8583.core.xml;

import java.util.Map;

import org.nucleus8583.core.field.Field;
import org.nucleus8583.core.field.Type;

import rk.commons.beans.factory.BeanFactory;
import rk.commons.beans.factory.BeanInstantiationException;
import rk.commons.beans.factory.support.BeanDefinitionValueResolver;
import rk.commons.beans.factory.support.BeanFactoryAware;
import rk.commons.beans.factory.support.BeanQNameAware;
import rk.commons.beans.factory.support.FactoryBean;
import rk.commons.beans.factory.support.InitializingBean;
import rk.commons.beans.factory.type.converter.TypeConverterResolver;
import rk.commons.util.BeanUtils;

public class FieldFactoryBean extends FactoryBean<Field> implements BeanQNameAware, BeanFactoryAware {

	private int no;

	private Type<?> type;

	private Map<String, Object> properties;
	
	private String beanQName;
	
	private BeanFactory beanFactory;

	public void setNo(int no) {
		this.no = no;
	}

	public void setType(Type<?> type) {
		this.type = type;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public void setBeanQName(String beanQName) {
		this.beanQName = beanQName;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
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
			
			BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(beanFactory);
			
			BeanUtils.applyPropertyValues(beanQName, type, properties, valueResolver, typeConverterResolver);
		}
		
		if (type instanceof InitializingBean) {
			try {
				((InitializingBean) type).initialize();
			} catch (Exception e) {
				throw new BeanInstantiationException(beanQName, e);
			}
		}
		
		return new Field(no, type);
	}
}
