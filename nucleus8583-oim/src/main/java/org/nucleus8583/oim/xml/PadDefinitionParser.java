package org.nucleus8583.oim.xml;

import org.nucleus8583.oim.field.spi.BinaryPad;
import org.nucleus8583.oim.field.spi.TextPad;
import org.w3c.dom.Element;

import rk.commons.ioc.factory.support.ObjectDefinitionBuilder;
import rk.commons.ioc.factory.xml.ObjectDefinitionParserDelegate;
import rk.commons.ioc.factory.xml.SingleObjectDefinitionParser;
import rk.commons.util.StringEscapeUtils;
import rk.commons.util.StringUtils;

public class PadDefinitionParser extends SingleObjectDefinitionParser {

	public static final String ELEMENT_LOCAL_NAME = "pad";
	
	@Override
	protected Class<?> getObjectClass(Element element) {
		String mode = element.getAttribute("mode");
		
		if ("text".equals(mode)) {
			return TextPad.class;
		} else if ("binary".equals(mode)) {
			return BinaryPad.class;
		} else {
			throw new UnsupportedOperationException("unsupported mode " + mode);
		}
	}
	
	protected void doParse(Element element, ObjectDefinitionParserDelegate delegate, ObjectDefinitionBuilder builder) {
		String stmp = element.getAttribute("no");
		if (StringUtils.hasText(stmp)) {
			builder.addPropertyValue("no", Integer.parseInt(stmp));
		}
		
		stmp = element.getAttribute("padWith");
		if (StringUtils.hasText(stmp, false)) {
			builder.addPropertyValue("padWith", StringEscapeUtils.escapeJava(stmp).toCharArray());
		}
	}
}
