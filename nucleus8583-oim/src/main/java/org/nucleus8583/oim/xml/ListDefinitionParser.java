package org.nucleus8583.oim.xml;

import org.w3c.dom.Element;

import rk.commons.inject.factory.support.ObjectDefinitionBuilder;
import rk.commons.inject.factory.xml.ObjectDefinitionParserDelegate;
import rk.commons.inject.factory.xml.SingleObjectDefinitionParser;
import rk.commons.util.StringHelper;

public class ListDefinitionParser extends SingleObjectDefinitionParser {
	
	public static final String ELEMENT_LOCAL_NAME = "list";
	
	@Override
	protected Class<?> getObjectClass(Element element) {
		return org.nucleus8583.oim.field.spi.List.class;
	}
	
	protected void doParse(Element element, ObjectDefinitionParserDelegate delegate, ObjectDefinitionBuilder builder) {
		String stmp = element.getAttribute("no");
		if (StringHelper.hasText(stmp)) {
			builder.addPropertyValue("no", Integer.parseInt(stmp));
		}
		
		builder.addPropertyValue("name", element.getAttribute("name"));
		
		stmp = element.getAttribute("partial");
		if (StringHelper.hasText(stmp)) {
			builder.addPropertyValue("partial", Boolean.parseBoolean(stmp));
		}
		
		stmp = element.getAttribute("capacity");
		if (StringHelper.hasText(stmp)) {
			builder.addPropertyValue("capacity", Integer.parseInt(stmp));
		}
		
		builder.addPropertyValue("childFields", delegate.parseChildElements(element));
	}
}
