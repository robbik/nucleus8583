package rk.commons.beans.factory.xml;

import java.lang.reflect.InvocationHandler;

import org.w3c.dom.Element;

import rk.commons.beans.factory.config.BeanDefinition;
import rk.commons.beans.factory.support.BeanDefinitionBuilder;

public abstract class SingleBeanDefinitionParser implements
		BeanDefinitionParser {

	protected Class<?> getBeanClass(Element element) {
		return null;
	}

	protected InvocationHandler getProxyHandler(Element element) {
		return null;
	}

	protected String getBeanClassName(Element element) {
		return null;
	}

	protected abstract void doParse(Element element,
			BeanDefinitionParserDelegate delegate, BeanDefinitionBuilder builder);

	public BeanDefinition parse(Element element,
			BeanDefinitionParserDelegate delegate) {

		BeanDefinitionBuilder builder = new BeanDefinitionBuilder();

		doParse(element, delegate, builder);

		builder.setBeanClass(getBeanClass(element));
		builder.setBeanClassName(getBeanClassName(element));

		builder.setProxyHandler(getProxyHandler(element));

		return builder.createBeanDefinition();
	}
}
