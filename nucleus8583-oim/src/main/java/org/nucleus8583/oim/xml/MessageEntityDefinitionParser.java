package org.nucleus8583.oim.xml;

import org.nucleus8583.oim.MessageEntity;
import org.w3c.dom.Element;

import rk.commons.inject.factory.support.ObjectDefinitionBuilder;
import rk.commons.inject.factory.xml.ObjectDefinitionParserDelegate;
import rk.commons.inject.factory.xml.SingleObjectDefinitionParser;

public class MessageEntityDefinitionParser extends SingleObjectDefinitionParser {

	public static final String ELEMENT_LOCAL_NAME = "message";

	@Override
	protected Class<?> getObjectClass(Element element) {
		return MessageEntity.class;
	}

	protected void doParse(Element element, ObjectDefinitionParserDelegate delegate, ObjectDefinitionBuilder builder) {
		builder.setObjectQName(element.getAttribute("name"));

		builder.addPropertyValue("fields", delegate.parseChildElements(element));
	}
}
