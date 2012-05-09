package org.nucleus8583.oim.xml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import rk.commons.inject.factory.config.ObjectReference;
import rk.commons.inject.factory.support.ObjectDefinitionBuilder;
import rk.commons.inject.factory.xml.ObjectDefinitionParserDelegate;
import rk.commons.inject.factory.xml.SingleObjectDefinitionParser;
import rk.commons.util.StringUtils;

public class BasicDefinitionParser extends SingleObjectDefinitionParser {

	public static final String ELEMENT_LOCAL_NAME = "basic";
	
	private static final Set<String> reserved;
	
	static {
		reserved = new HashSet<String>();
		reserved.add("no");
		reserved.add("no-ref");
		reserved.add("name");
		reserved.add("name-ref");
		reserved.add("type");
		reserved.add("type-ref");
	}
	
	@Override
	protected Class<?> getObjectClass(Element element) {
		return BasicFactory.class;
	}
	
	protected Set<String> getReservedAttrs() {
		return reserved;
	}
	
	protected Map<String, Object> parseDynamicAttrs(Element element, ObjectDefinitionParserDelegate delegate,
			ObjectDefinitionBuilder builder) {
		Map<String, Object> properties = new HashMap<String, Object>();
		Set<String> reserved = getReservedAttrs();
		
		NamedNodeMap attributeMap = element.getAttributes();
		
		for (int i = 0, n = attributeMap.getLength(); i < n; ++i) {
			Attr attr = (Attr) attributeMap.item(i);
			
			String attrName = attr.getLocalName();
			
			if (!reserved.contains(attrName)) {
				if (attrName.endsWith("-ref")) {
					properties.put(attrName.substring(0, attrName.length() - 4), new ObjectReference(attr.getValue()));
				} else {
					properties.put(attrName, attr.getValue());
				}
			}
		}
		
		return properties;
	}
	
	protected void doParse(Element element, ObjectDefinitionParserDelegate delegate, ObjectDefinitionBuilder builder) {
		String stmp = element.getAttribute("no");
		if (StringUtils.hasText(stmp)) {
			builder.addPropertyValue("no", Integer.parseInt(stmp));
		}
		
		stmp = element.getAttribute("name");
		if (StringUtils.hasText(stmp)) {
			builder.addPropertyValue("name", stmp);
		}
		
		builder.addPropertyReference("type", element.getAttribute("type"));

		builder.addPropertyValue("properties", parseDynamicAttrs(element, delegate, builder));
	}
}
