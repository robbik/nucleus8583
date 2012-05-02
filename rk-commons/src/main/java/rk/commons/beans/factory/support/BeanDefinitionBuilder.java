package rk.commons.beans.factory.support;

import java.lang.reflect.InvocationHandler;

import rk.commons.beans.factory.config.BeanDefinition;
import rk.commons.beans.factory.config.RuntimeBeanDefinition;

public class BeanDefinitionBuilder {

	protected BeanDefinition definition;

	public BeanDefinitionBuilder() {
		definition = new BeanDefinition();
	}

	public void setBeanQName(String beanQName) {
		definition.setBeanQName(beanQName);
	}

	public void setBeanClassName(String beanClassName) {
		definition.setBeanClassName(beanClassName);
	}

	public void setBeanClass(Class<?> beanClass) {
		definition.setBeanClass(beanClass);
	}

	public void setProxyHandler(InvocationHandler proxyHandler) {
		definition.setProxyHandler(proxyHandler);
	}
	
	public void addPropertyValue(String name, Object value) {
		definition.getPropertyValues().put(name, value);
	}

	public void addPropertyReference(String name, String beanQName) {
		addPropertyValue(name, new RuntimeBeanDefinition(beanQName));
	}

	public void setInitMethod(String initMethod) {
		definition.setInitMethod(initMethod);
	}

	public void setDestroyMethod(String destroyMethod) {
		definition.setDestroyMethod(destroyMethod);
	}

	public void setExtends(String beanQName) {
		definition.setExtends(beanQName);
	}

	public BeanDefinition createBeanDefinition() {
		return definition;
	}
}
