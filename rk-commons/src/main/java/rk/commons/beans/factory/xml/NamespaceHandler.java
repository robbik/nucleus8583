package rk.commons.beans.factory.xml;

import org.w3c.dom.Element;

import rk.commons.beans.factory.config.BeanDefinition;

public interface NamespaceHandler {

	void init();

	BeanDefinition parse(Element element, BeanDefinitionParserDelegate delegate);
}
