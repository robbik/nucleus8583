package org.nucleus8583.core.xml;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import rk.commons.beans.factory.support.BeanDefinitionBuilder;
import rk.commons.beans.factory.xml.BeanDefinitionParserDelegate;
import rk.commons.beans.factory.xml.SingleBeanDefinitionParser;

public class TypeDefinitionParser extends SingleBeanDefinitionParser {
	
	public static final String ELEMENT_LOCAL_NAME = "type";
	
	private static final Set<String> reserved;
	
	static {
		reserved = new HashSet<String>();
		reserved.add("name");
		reserved.add("name-ref");
		reserved.add("class");
		reserved.add("class-ref");
		reserved.add("description");
		reserved.add("description-ref");
	}
	
	@Override
	protected String getBeanClassName(Element element) {
		return element.getAttribute("class");
	}
	
	protected void doParse(Element element, BeanDefinitionParserDelegate delegate, BeanDefinitionBuilder builder) {
		builder.setBeanQName(element.getAttribute("name"));
		
		NamedNodeMap attributeMap = element.getAttributes();
		
		for (int i = 0, n = attributeMap.getLength(); i < n; ++i) {
			Attr attr = (Attr) attributeMap.item(i);
			
			String attrName = attr.getLocalName();
			
			if (!reserved.contains(attrName)) {
				if (attrName.endsWith("-ref")) {
					builder.addPropertyReference(attrName.substring(0, attrName.length() - 4), attr.getValue());
				} else {
					builder.addPropertyValue(attrName, attr.getValue());
				}
			}
		}
	}
}
