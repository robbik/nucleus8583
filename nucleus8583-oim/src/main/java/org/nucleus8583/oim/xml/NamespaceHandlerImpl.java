package org.nucleus8583.oim.xml;

import rk.commons.inject.factory.xml.NamespaceHandlerSupport;

public class NamespaceHandlerImpl extends NamespaceHandlerSupport {

	public void init() {
		registerObjectDefinitionParser(
				MessageEntityDefinitionParser.ELEMENT_LOCAL_NAME,
				new MessageEntityDefinitionParser());

		registerObjectDefinitionParser(
				TypeDefinitionParser.ELEMENT_LOCAL_NAME,
				new TypeDefinitionParser());

		registerObjectDefinitionParser(
				ExpressionDefinitionParser.ELEMENT_LOCAL_NAME,
				new ExpressionDefinitionParser());

		registerObjectDefinitionParser(
				BasicDefinitionParser.ELEMENT_LOCAL_NAME,
				new BasicDefinitionParser());

		registerObjectDefinitionParser(
				ListDefinitionParser.ELEMENT_LOCAL_NAME,
				new ListDefinitionParser());

		registerObjectDefinitionParser(
				ListCountDefinitionParser.ELEMENT_LOCAL_NAME,
				new ListCountDefinitionParser());

		registerObjectDefinitionParser(
				PadDefinitionParser.ELEMENT_LOCAL_NAME,
				new PadDefinitionParser());

		registerObjectDefinitionParser(
				RecordDefinitionParser.ELEMENT_LOCAL_NAME,
				new RecordDefinitionParser());

		registerObjectDefinitionParser(
				SkipDefinitionParser.ELEMENT_LOCAL_NAME,
				new SkipDefinitionParser());
	}
}
