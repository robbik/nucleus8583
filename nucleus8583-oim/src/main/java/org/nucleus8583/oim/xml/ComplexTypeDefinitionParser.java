package org.nucleus8583.oim.xml;

import org.w3c.dom.Element;

import rk.commons.inject.factory.support.ObjectDefinitionBuilder;
import rk.commons.inject.factory.xml.ObjectDefinitionParserDelegate;
import rk.commons.inject.factory.xml.SingleObjectDefinitionParser;

public class ComplexTypeDefinitionParser extends SingleObjectDefinitionParser {

	public static final String ELEMENT_LOCAL_NAME = "complex-type";

	@Override
	protected Class<?> getObjectClass(Element element) {
		return ComplexTypeFactory.class;
	}

	protected void doParse(Element element, ObjectDefinitionParserDelegate delegate, ObjectDefinitionBuilder builder) {
		builder.setObjectName(element.getAttribute("name"));

		builder.addPropertyValue("name", element.getAttribute("name"));
		builder.addPropertyValue("beanClass", element.getAttribute("class"));
		builder.addPropertyValue("access", element.getAttribute("access"));
		builder.addPropertyValue("fields", delegate.parseChildElements(element));
	}
}
