package org.nucleus8583.core.xml;

import org.nucleus8583.core.MessageSerializer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rk.commons.beans.factory.support.BeanDefinitionBuilder;
import rk.commons.beans.factory.support.ManagedList;
import rk.commons.beans.factory.xml.BeanDefinitionParserDelegate;
import rk.commons.beans.factory.xml.SingleBeanDefinitionParser;

public class MessageSerializerDefinitionParser extends SingleBeanDefinitionParser {

	public static final String ELEMENT_LOCAL_NAME = "message";

	@Override
	protected Class<?> getBeanClass(Element element) {
		return MessageSerializer.class;
	}

	protected void doParse(Element element, BeanDefinitionParserDelegate delegate, BeanDefinitionBuilder builder) {

		ManagedList<Object> fields = new ManagedList<Object>();

		NodeList childNodes = element.getChildNodes();

		for (int i = 0, n = childNodes.getLength(); i < n; ++i) {
			Node childNode = childNodes.item(i);

			if (childNode instanceof Element) {
				fields.add(delegate.parse((Element) childNode));
			}
		}

		builder.setBeanQName(element.getAttribute("name"));

		builder.addPropertyValue("fields", fields);
	}
}
