package org.nucleus8583.oim.xml;

import org.nucleus8583.oim.field.spi.Record;
import org.w3c.dom.Element;

import rk.commons.inject.factory.support.ObjectDefinitionBuilder;
import rk.commons.inject.factory.xml.ObjectDefinitionParserDelegate;
import rk.commons.inject.factory.xml.SingleObjectDefinitionParser;
import rk.commons.util.StringHelper;

public class RecordDefinitionParser extends SingleObjectDefinitionParser {

	public static final String ELEMENT_LOCAL_NAME = "record";
	
	@Override
	protected Class<?> getObjectClass(Element element) {
		return Record.class;
	}
	
	protected void doParse(Element element, ObjectDefinitionParserDelegate delegate, ObjectDefinitionBuilder builder) {
		String stmp = element.getAttribute("no");
		if (StringHelper.hasText(stmp)) {
			builder.addPropertyValue("no", Integer.parseInt(stmp));
		}
		
		stmp = element.getAttribute("name");
		if (StringHelper.hasText(stmp)) {
			builder.addPropertyValue("name", stmp);
		}
		
		builder.addPropertyValue("childFields", delegate.parseChildElements(element));
		
		builder.addPropertyValue("textMode", "text".equals(element.getAttribute("mode")));
	}
}
