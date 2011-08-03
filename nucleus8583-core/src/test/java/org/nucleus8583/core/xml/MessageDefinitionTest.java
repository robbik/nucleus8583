package org.nucleus8583.core.xml;

import static org.junit.Assert.assertEquals;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.util.ResourceUtils;

public class MessageDefinitionTest {
	private MessageDefinition def;

	@Before
	public void before() throws Exception {
		JAXBContext ctx = JAXBContext.newInstance(MessageDefinition.class);
		Unmarshaller unmarshaller = ctx.createUnmarshaller();

		def = (MessageDefinition) unmarshaller.unmarshal(ResourceUtils.getURL("file:src/test/resources/META-INF/codec8583.xml"));
	}

	@Test
	public void testGetEncoding() {
		assertEquals("ASCII", def.getEncoding());
	}
}
