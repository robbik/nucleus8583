package org.nucleus8583.oim.xml;

import org.w3c.dom.Element;

import rk.commons.inject.factory.support.ObjectDefinitionBuilder;
import rk.commons.inject.factory.xml.ObjectDefinitionParserDelegate;
import rk.commons.inject.factory.xml.SingleObjectDefinitionParser;
import rk.commons.util.StringUtils;

public class ListDefinitionParser extends SingleObjectDefinitionParser {
	
	public static final String ELEMENT_LOCAL_NAME = "list";
	
	@Override
	protected Class<?> getObjectClass(Element element) {
		return org.nucleus8583.oim.field.spi.List.class;
	}
	
	protected void doParse(Element element, ObjectDefinitionParserDelegate delegate, ObjectDefinitionBuilder builder) {
		String stmp = element.getAttribute("no");
		if (StringUtils.hasText(stmp)) {
			builder.addPropertyValue("no", Integer.parseInt(stmp));
		}
		
		builder.addPropertyValue("name", element.getAttribute("name"));
		
		stmp = element.getAttribute("append");
		if (StringUtils.hasText(stmp)) {
			builder.addPropertyValue("append", Boolean.parseBoolean(stmp));
		}
		
		stmp = element.getAttribute("maxCount");
		if (StringUtils.hasText(stmp)) {
			builder.addPropertyValue("maxCount", Integer.parseInt(stmp));
		}
		
		builder.addPropertyValue("childFields", delegate.parseChildElements(element));
	}
}
