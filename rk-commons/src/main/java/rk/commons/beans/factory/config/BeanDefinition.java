package rk.commons.beans.factory.config;

import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;

import rk.commons.loader.ResourceLoader;
import rk.commons.util.StringUtils;

public class BeanDefinition {

	protected String beanQName;

	protected String beanClassName;

	protected Class<?> beanClass;
	
	protected InvocationHandler proxyHandler;
	
	protected Map<String, Object> propertyValues;

	protected String initMethod;

	protected String destroyMethod;

	protected String extendsBeanQName;

	public BeanDefinition() {
		propertyValues = new HashMap<String, Object>();
	}

	public String getBeanQName() {
		return beanQName;
	}

	public void setBeanQName(String beanQName) {
		this.beanQName = beanQName;
	}

	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}
	
	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}
	
	public InvocationHandler getProxyHandler() {
		return proxyHandler;
	}
	
	public void setProxyHandler(InvocationHandler proxyHandler) {
		this.proxyHandler = proxyHandler;
	}

	public Map<String, Object> getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(Map<String, Object> propertyValues) {
		this.propertyValues = propertyValues;
	}

	public String getInitMethod() {
		return initMethod;
	}

	public void setInitMethod(String initMethod) {
		this.initMethod = initMethod;
	}

	public String getDestroyMethod() {
		return destroyMethod;
	}

	public void setDestroyMethod(String destroyMethod) {
		this.destroyMethod = destroyMethod;
	}

	public String getExtends() {
		return extendsBeanQName;
	}

	public void setExtends(String extendsBeanQName) {
		this.extendsBeanQName = extendsBeanQName;
	}

	public void resolveClass(ResourceLoader resourceLoader) {
		if (beanClass == null) {
			if (!StringUtils.hasText(beanClassName)) {
				throw new IllegalArgumentException(
						"bean class name or bean class must be specified for bean "
								+ beanQName);
			}

			try {
				beanClass = resourceLoader.loadClass(beanClassName);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(
						"unable to load bean class name '" + beanClassName
								+ "' for bean " + beanQName);
			}
		}
	}
}
