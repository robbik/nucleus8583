package org.nucleus8583.oim.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rk.commons.ioc.factory.support.ObjectDefinitionBuilder;
import rk.commons.ioc.factory.xml.ObjectDefinitionParserDelegate;
import rk.commons.ioc.factory.xml.SingleObjectDefinitionParser;
import rk.commons.util.StringUtils;

public class ListDefinitionParser extends SingleObjectDefinitionParser {
	
	public static final String ELEMENT_LOCAL_NAME = "list";
	
	@Override
	protected Class<?> getObjectClass(Element element) {
		return org.nucleus8583.oim.field.spi.List.class;
	}
	
	protected void doParse(Element element, ObjectDefinitionParserDelegate delegate, ObjectDefinitionBuilder builder) {
		List<Object> childFields = new ArrayList<Object>();
		
		NodeList childNodes = element.getChildNodes();
		
		for (int i = 0, n = childNodes.getLength(); i < n; ++i) {
			Node childNode = childNodes.item(i);
			
			if (childNode instanceof Element) {
				childFields.add(delegate.parse((Element) childNode));
			}
		}
		
		String stmp = element.getAttribute("no");
		if (StringUtils.hasText(stmp)) {
			builder.addPropertyValue("no", Integer.parseInt(stmp));
		}
		
		builder.addPropertyValue("name", element.getAttribute("name"));
		
		stmp = element.getAttribute("append");
		if (StringUtils.hasText(stmp)) {
			builder.addPropertyValue("append", Boolean.parseBoolean(stmp));
		}
		
		builder.addPropertyValue("countName", element.getAttribute("countName"));
		
		stmp = element.getAttribute("maxCount");
		if (StringUtils.hasText(stmp)) {
			builder.addPropertyValue("maxCount", Integer.parseInt(stmp));
		}
		
		builder.addPropertyValue("childFields", childFields);
	}
}
