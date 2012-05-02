package rk.commons.beans.factory.config;

public class RuntimeBeanDefinition {

	private final String beanQName;

	public RuntimeBeanDefinition(String beanQName) {
		this.beanQName = beanQName;
	}

	public String getBeanQName() {
		return beanQName;
	}
}
