package rk.commons.ioc.factory.xml;

import org.w3c.dom.Element;

import rk.commons.ioc.factory.config.ObjectDefinition;
import rk.commons.ioc.factory.support.ObjectDefinitionBuilder;

public abstract class SingleObjectDefinitionParser implements
		ObjectDefinitionParser {

	protected Class<?> getObjectClass(Element element) {
		return null;
	}

	protected String getObjectClassName(Element element) {
		return null;
	}

	protected abstract void doParse(Element element,
			ObjectDefinitionParserDelegate delegate, ObjectDefinitionBuilder builder);

	public ObjectDefinition parse(Element element,
			ObjectDefinitionParserDelegate delegate) {

		ObjectDefinitionBuilder builder = new ObjectDefinitionBuilder();

		doParse(element, delegate, builder);

		builder.setObjectClass(getObjectClass(element));
		builder.setObjectClassName(getObjectClassName(element));

		return builder.createObjectDefinition();
	}
}
