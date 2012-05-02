package rk.commons.beans.factory;

import java.util.List;

import rk.commons.beans.factory.config.BeanDefinition;

public interface BeanFactory {
	
	boolean containsBean(String beanQName);

	Object getBean(String beanQName);

	<T> List<T> getBeansOfType(Class<T> type);

	String[] getBeanQNames();
	
	Object createBean(BeanDefinition definition);
	
	void destroy();
}
