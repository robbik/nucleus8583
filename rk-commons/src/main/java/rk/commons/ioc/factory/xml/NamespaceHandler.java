package rk.commons.ioc.factory.xml;

import org.w3c.dom.Element;

import rk.commons.ioc.factory.config.ObjectDefinition;

public interface NamespaceHandler {

	void init();

	ObjectDefinition parse(Element element, ObjectDefinitionParserDelegate delegate);
}
