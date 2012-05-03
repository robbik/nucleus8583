package rk.commons.ioc.factory.xml;

import org.w3c.dom.Element;

import rk.commons.ioc.factory.config.ObjectDefinition;

public interface ObjectDefinitionParser {

	ObjectDefinition parse(Element element, ObjectDefinitionParserDelegate delegate);
}
