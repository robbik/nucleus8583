package org.nucleus8583.core.xml;

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
import rk.commons.util.StringHelper;

public class FieldDefinitionParser extends SingleObjectDefinitionParser {

	public static final String ELEMENT_LOCAL_NAME = "field";
	
	private static final Set<String> reserved;
	
	static {
		reserved = new HashSet<String>();
		reserved.add("no");
		reserved.add("no-ref");
		reserved.add("type");
		reserved.add("type-ref");
		reserved.add("description");
		reserved.add("description-ref");
		reserved.add("subMessage");
		reserved.add("subMessage-ref");
	}
	
	@Override
	protected Class<?> getObjectClass(Element element) {
		return FieldFactory.class;
	}
	
	protected void doParse(Element element, ObjectDefinitionParserDelegate delegate, ObjectDefinitionBuilder builder) {
		Map<String, Object> properties = new HashMap<String, Object>();

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

		builder.setObjectQName(element.getAttribute("name"));

		builder.addPropertyValue("no", Integer.parseInt(element.getAttribute("no")));
		builder.addPropertyReference("type", element.getAttribute("type"));
		
		if (StringHelper.hasText(element.getAttribute("subMessage-ref"), true)) {
			builder.addPropertyReference("messageSerializer", element.getAttribute("subMessage-ref").trim());
		}

		builder.addPropertyValue("properties", properties);
	}
}
