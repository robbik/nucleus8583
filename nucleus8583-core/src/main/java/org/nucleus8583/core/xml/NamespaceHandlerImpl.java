package org.nucleus8583.core.xml;

import rk.commons.beans.factory.xml.NamespaceHandlerSupport;

public class NamespaceHandlerImpl extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionParser(
				MessageSerializerDefinitionParser.ELEMENT_LOCAL_NAME,
				new MessageSerializerDefinitionParser());

		registerBeanDefinitionParser(FieldDefinitionParser.ELEMENT_LOCAL_NAME,
				new FieldDefinitionParser());

		registerBeanDefinitionParser(TypeDefinitionParser.ELEMENT_LOCAL_NAME,
				new TypeDefinitionParser());
	}
}
