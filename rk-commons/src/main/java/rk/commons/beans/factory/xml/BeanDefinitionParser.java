package rk.commons.beans.factory.xml;

import org.w3c.dom.Element;

import rk.commons.beans.factory.config.BeanDefinition;

public interface BeanDefinitionParser {

	BeanDefinition parse(Element element, BeanDefinitionParserDelegate delegate);
}
