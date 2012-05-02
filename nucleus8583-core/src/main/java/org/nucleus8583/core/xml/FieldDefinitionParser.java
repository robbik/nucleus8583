package org.nucleus8583.core.xml;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import rk.commons.beans.factory.config.RuntimeBeanDefinition;
import rk.commons.beans.factory.support.BeanDefinitionBuilder;
import rk.commons.beans.factory.support.ManagedMap;
import rk.commons.beans.factory.xml.BeanDefinitionParserDelegate;
import rk.commons.beans.factory.xml.SingleBeanDefinitionParser;

public class FieldDefinitionParser extends SingleBeanDefinitionParser {

	public static final String ELEMENT_LOCAL_NAME = "field";
	
	private static final Set<String> reserved;
	
	static {
		reserved = new HashSet<String>();
		reserved.add("name");
		reserved.add("name-ref");
		reserved.add("type");
		reserved.add("type-ref");
		reserved.add("no");
		reserved.add("no-ref");
		reserved.add("description");
		reserved.add("description-ref");
	}
	
	@Override
	protected Class<?> getBeanClass(Element element) {
		return FieldFactoryBean.class;
	}
	
	protected void doParse(Element element, BeanDefinitionParserDelegate delegate, BeanDefinitionBuilder builder) {
		ManagedMap<String, Object> properties = new ManagedMap<String, Object>();

		NamedNodeMap attributeMap = element.getAttributes();
		
		for (int i = 0, n = attributeMap.getLength(); i < n; ++i) {
			Attr attr = (Attr) attributeMap.item(i);
			
			String attrName = attr.getLocalName();
			
			if (!reserved.contains(attrName)) {
				if (attrName.endsWith("-ref")) {
					properties.put(attrName.substring(0, attrName.length() - 4), new RuntimeBeanDefinition(attr.getValue()));
				} else {
					properties.put(attrName, attr.getValue());
				}
			}
		}

		builder.setBeanQName(element.getAttribute("name"));

		builder.addPropertyValue("no", Integer.parseInt(element.getAttribute("no")));
		builder.addPropertyReference("type", element.getAttribute("type"));
		builder.addPropertyValue("properties", properties);
	}
}
