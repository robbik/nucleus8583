package rk.commons.beans.factory.xml;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import rk.commons.beans.factory.config.BeanDefinition;

public abstract class NamespaceHandlerSupport implements NamespaceHandler {

	private Map<String, BeanDefinitionParser> parsers;

	public NamespaceHandlerSupport() {
		parsers = new HashMap<String, BeanDefinitionParser>();
	}

	public abstract void init();

	protected void registerBeanDefinitionParser(String localName,
			BeanDefinitionParser parser) {
		parsers.put(localName, parser);
	}

	public BeanDefinition parse(Element element,
			BeanDefinitionParserDelegate delegate) {

		BeanDefinitionParser parser = parsers.get(delegate
				.getLocalName(element));
		if (parser == null) {
			throw new IllegalArgumentException("no parser found for element "
					+ delegate.getLocalName(element));
		}

		return parser.parse(element, delegate);
	}
}
