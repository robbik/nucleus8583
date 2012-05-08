package org.nucleus8583.oim.xml;

import java.util.HashSet;
import java.util.Set;

import org.nucleus8583.oim.expression.ExpressionHandlerResolver;
import org.w3c.dom.Element;

import rk.commons.ioc.factory.support.ObjectDefinitionBuilder;
import rk.commons.ioc.factory.xml.ObjectDefinitionParserDelegate;
import rk.commons.util.StringUtils;

public class ExpressionDefinitionParser extends BasicDefinitionParser {

	public static final String ELEMENT_LOCAL_NAME = "expression";
	
	private static final Set<String> reserved;
	
	static {
		reserved = new HashSet<String>();
		reserved.add("language");
		reserved.add("language-ref");
		reserved.add("type");
		reserved.add("type-ref");
	}
	
	private ExpressionHandlerResolver resolver;
	
	protected Set<String> getReservedAttrs() {
		Set<String> merged = new HashSet<String>();
		
		merged.addAll(super.getReservedAttrs());
		merged.addAll(reserved);
		
		return merged;
	}
	
	@Override
	protected Class<?> getObjectClass(Element element) {
		return ExpressionFactory.class;
	}

	protected void doParse(Element element, ObjectDefinitionParserDelegate delegate, ObjectDefinitionBuilder builder) {
		super.doParse(element, delegate, builder);
		
		if (resolver == null) {
			resolver = new ExpressionHandlerResolver(delegate.getResourceLoader());
		}

		String expression = element.getTextContent();
		if (!StringUtils.hasText(expression)) {
			throw new IllegalArgumentException("expression body must be set");
		}

		builder.addPropertyValue("resolver", resolver);

		builder.addPropertyReference("type", element.getAttribute("type"));

		builder.addPropertyValue("language", element.getAttribute("language"));
		
		builder.addPropertyValue("expression", expression.trim());
	}
}
