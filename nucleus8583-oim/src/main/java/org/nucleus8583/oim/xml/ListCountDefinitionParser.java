package org.nucleus8583.oim.xml;

import org.w3c.dom.Element;

public class ListCountDefinitionParser extends BasicDefinitionParser {

	public static final String ELEMENT_LOCAL_NAME = "list-count";
	
	@Override
	protected Class<?> getObjectClass(Element element) {
		return ListCountFactory.class;
	}
}
