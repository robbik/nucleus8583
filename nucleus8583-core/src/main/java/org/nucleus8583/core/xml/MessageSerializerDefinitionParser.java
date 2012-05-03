package org.nucleus8583.core.xml;

import java.util.ArrayList;
import java.util.List;

import org.nucleus8583.core.MessageSerializer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rk.commons.ioc.factory.support.ObjectDefinitionBuilder;
import rk.commons.ioc.factory.xml.ObjectDefinitionParserDelegate;
import rk.commons.ioc.factory.xml.SingleObjectDefinitionParser;

public class MessageSerializerDefinitionParser extends SingleObjectDefinitionParser {

	public static final String ELEMENT_LOCAL_NAME = "message";

	@Override
	protected Class<?> getObjectClass(Element element) {
		return MessageSerializer.class;
	}

	protected void doParse(Element element, ObjectDefinitionParserDelegate delegate, ObjectDefinitionBuilder builder) {
		List<Object> fields = new ArrayList<Object>();

		NodeList childNodes = element.getChildNodes();

		for (int i = 0, n = childNodes.getLength(); i < n; ++i) {
			Node childNode = childNodes.item(i);

			if (childNode instanceof Element) {
				fields.add(delegate.parse((Element) childNode));
			}
		}

		builder.setObjectQName(element.getAttribute("name"));

		builder.addPropertyValue("fields", fields);
	}
}
