package org.nucleus8583.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.nucleus8583.core.types.DummyField;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public class CustomFieldTypeTest {

	@Test
	public void testDummyField() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(
				Iso8583FieldDefinition.class).createUnmarshaller();

		Object x = unmarshaller
				.unmarshal(new ByteArrayInputStream(
						("<iso-field id=\"1\" type=\"dummy\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" length=\"4\" />")
								.getBytes()));

		assertNotNull(x);
		assertTrue(x instanceof Iso8583FieldDefinition);

		Iso8583Field f = ((Iso8583FieldDefinition) x).createField();

		assertNotNull(f);
		assertTrue(f instanceof DummyField);

		StringWriter sw = new StringWriter();
		((DummyField) f).pack(sw, "abcdefg");
		assertEquals("ab", sw.toString());

		sw = new StringWriter();
		((DummyField) f).pack(sw, "a");
		assertEquals("xa", sw.toString());

		sw = new StringWriter();
		((DummyField) f).pack(sw, "");
		assertEquals("xx", sw.toString());

		sw = new StringWriter();
		((DummyField) f).pack(sw, (String) null);
		assertEquals("xx", sw.toString());
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

		((Iso8583FieldDefinition) x).createField();
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

		((Iso8583FieldDefinition) x).createField();
	}
}
