package org.nucleus8583.core.xml;

import rk.commons.ioc.factory.xml.NamespaceHandlerSupport;

public class NamespaceHandlerImpl extends NamespaceHandlerSupport {

	public void init() {
		registerObjectDefinitionParser(
				MessageSerializerDefinitionParser.ELEMENT_LOCAL_NAME,
				new MessageSerializerDefinitionParser());

		registerObjectDefinitionParser(FieldDefinitionParser.ELEMENT_LOCAL_NAME,
				new FieldDefinitionParser());

		registerObjectDefinitionParser(TypeDefinitionParser.ELEMENT_LOCAL_NAME,
				new TypeDefinitionParser());
	}
}
