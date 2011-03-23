package org.nucleus8583.core.xml;

import static org.junit.Assert.assertEquals;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.util.ResourceUtils;

public class Iso8583MessageDefinitionTest {
	private Iso8583MessageDefinition def;

	private Iso8583MessageDefinition defError;

	@Before
	public void before() throws Exception {
		JAXBContext ctx = JAXBContext
				.newInstance(Iso8583MessageDefinition.class);
		Unmarshaller unmarshaller = ctx.createUnmarshaller();

		def = (Iso8583MessageDefinition) unmarshaller.unmarshal(ResourceUtils
				.getURL("file:src/test/resources/META-INF/codec8583.xml"));
		defError = (Iso8583MessageDefinition) unmarshaller
				.unmarshal(ResourceUtils
						.getURL("file:src/test/resources/META-INF/error8583.xml"));
	}

	@Test
	public void testGetEncoding() {
		assertEquals("ASCII", def.getEncoding());
	}

	@Test
	public void testCreateMessageTemplate() {
		def.createMessageTemplate();
	}

	@Test(expected = RuntimeException.class)
	public void testCreateMessageTemplateUnsupportedEncoding() {
		defError.createMessageTemplate();
	}

	@Test
	public void testCreateMessage() {
		def.createMessage();
	}
}
