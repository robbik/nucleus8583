package org.nucleus8583.core.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Ignore;
import org.junit.Test;
import org.nucleus8583.core.Iso8583Field;
import org.nucleus8583.core.Iso8583String;
import org.nucleus8583.core.types.DummyField;

public class Iso8583FieldDefinitionTest {
	private static String[] stringTypes = new String[] { "a", "n", "s", "an",
			"as", "ns", "ans" };

	private static String[] dots = new String[] { ".", "..", "...", "....",
			"....." };
	private static int[] dotLen = new int[] { 9, 99, 999, 9999, 99999 };

	@Ignore
	private void stringFieldHelper(Object x, int id, int length)
			throws Exception {
		assertNotNull(x);
		assertTrue(x instanceof Iso8583FieldDefinition);

		Iso8583FieldDefinition def = (Iso8583FieldDefinition) x;
		Iso8583Field field = def.createField();

		assertTrue(field instanceof Iso8583String);
		assertEquals(id, field.getId());
		assertEquals(length, field.getLength());
	}

	@Test
	public void smokeTestStringField() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(
				Iso8583FieldDefinition.class).createUnmarshaller();

		int xlen;
		Object x;

		Random rnd = new Random();

		for (int i = 0; i < 200; ++i) {
			for (String s : stringTypes) {
				xlen = rnd.nextInt(800) + 1;
				x = unmarshaller
						.unmarshal(new ByteArrayInputStream(
								("<iso-field id=\"1\" type=\"" + s
										+ "\" length=\"" + xlen + "\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />")
										.getBytes()));

				stringFieldHelper(x, 1, xlen);
			}
		}
	}

	@Test
	public void testDottedField() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(
				Iso8583FieldDefinition.class).createUnmarshaller();

		Object x;

		for (int i = 0; i < dots.length; ++i) {
			x = unmarshaller
					.unmarshal(new ByteArrayInputStream(
							("<iso-field id=\"1\" type=\"a" + dots[i] + "\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />")
									.getBytes()));

			stringFieldHelper(x, 1, dotLen[i]);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithLenLT0() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(
				Iso8583FieldDefinition.class).createUnmarshaller();

		for (String s : stringTypes) {
			Object x = unmarshaller
					.unmarshal(new ByteArrayInputStream(
							("<iso-field id=\"1\" type=\"" + s + "\" length=\"0\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />")
									.getBytes()));

			assertNotNull(x);
			assertTrue(x instanceof Iso8583FieldDefinition);

			((Iso8583FieldDefinition) x).createField();
		}
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowUnknownType() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(
				Iso8583FieldDefinition.class).createUnmarshaller();

		Object x = unmarshaller
				.unmarshal(new ByteArrayInputStream(
						("<iso-field id=\"1\" type=\"zzz\" length=\"1\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />")
								.getBytes()));

		assertNotNull(x);
		assertTrue(x instanceof Iso8583FieldDefinition);

		((Iso8583FieldDefinition) x).createField();
	}

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
}
