package org.nucleus8583.core.field.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.charset.Charsets;
import org.nucleus8583.core.field.type.FieldType;
import org.nucleus8583.core.field.type.FieldTypes;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public class CustomFieldTypeTest {

	private CharsetEncoder encoder;

	@Before
	public void before() throws Exception {
		encoder = Charsets.getProvider("ASCII").getEncoder();
	}

	@Test
	public void testDummyField() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(Iso8583FieldDefinition.class).createUnmarshaller();

		Object x = unmarshaller
				.unmarshal(new ByteArrayInputStream(
						("<iso-field id=\"1\" type=\"dummy\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" length=\"4\" />")
								.getBytes()));

		assertNotNull(x);
		assertTrue(x instanceof Iso8583FieldDefinition);

		FieldType f = FieldTypes.getType((Iso8583FieldDefinition) x);

		assertNotNull(f);
		assertTrue(f instanceof DummyField);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		((DummyField) f).write(out, encoder, "abcdefg");
		assertEquals("ab", out.toString());

		out = new ByteArrayOutputStream();
		((DummyField) f).write(out, encoder, "a");
		assertEquals("xa", out.toString());

		out = new ByteArrayOutputStream();
		((DummyField) f).write(out, encoder, "");
		assertEquals("xx", out.toString());

		out = new ByteArrayOutputStream();
		((DummyField) f).write(out, encoder, (String) null);
		assertEquals("xx", out.toString());
	}

	@Test(expected = RuntimeException.class)
	public void testDummyField2() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(
				Iso8583FieldDefinition.class).createUnmarshaller();

		Object x = unmarshaller
				.unmarshal(new ByteArrayInputStream(
						("<iso-field id=\"1\" type=\"dummy2\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" length=\"4\" />")
								.getBytes()));

		assertNotNull(x);
		assertTrue(x instanceof Iso8583FieldDefinition);

		FieldTypes.getType((Iso8583FieldDefinition) x);
	}

	@Test(expected = RuntimeException.class)
	public void testDummyField3() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(
				Iso8583FieldDefinition.class).createUnmarshaller();

		Object x = unmarshaller
				.unmarshal(new ByteArrayInputStream(
						("<iso-field id=\"1\" type=\"dummy3\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" length=\"4\" />")
								.getBytes()));

		assertNotNull(x);
		assertTrue(x instanceof Iso8583FieldDefinition);

		FieldTypes.getType((Iso8583FieldDefinition) x);
	}
}
