package rk.commons.beans.factory.support;

import rk.commons.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {

	void registerBeanDefinition(BeanDefinition definition);
	
	void removeBeanDefinition(String beanQName);
	
	BeanDefinition getBeanDefinition(String beanQName);
	
	boolean containsBeanDefinition(String beanQName);
	
	String[] getBeanQNames();
}
