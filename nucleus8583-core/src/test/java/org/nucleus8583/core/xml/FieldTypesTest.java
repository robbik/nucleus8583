package org.nucleus8583.core.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.nucleus8583.core.field.type.AbstractHexBinFieldType;
import org.nucleus8583.core.field.type.AbstractStringFieldType;
import org.nucleus8583.core.field.type.FieldType;
import org.nucleus8583.core.field.type.FieldTypes;

public class FieldTypesTest {
	private static String[] binaryTypes = new String[] { "b" };
	private static String[] stringTypes = new String[] { "a", "n", "s", "an", "as", "ns", "ans" };

	private static String[] dots = new String[] { ".", "..", "..." };
	private static int[] dotLen = new int[] { 9, 99, 999 };

	private void stringFieldHelper(Object x, int id, int length) throws Exception {
		assertNotNull(x);
		assertTrue(x instanceof FieldDefinition);

		FieldDefinition def = (FieldDefinition) x;
		FieldType field = FieldTypes.getType(def);

		assertTrue(field instanceof AbstractStringFieldType);
		assertEquals(id, field.getId());

		Field f = null;

		try {
			f = field.getClass().getDeclaredField("length");
			f.setAccessible(true);
		} catch (Throwable t) {
			// do nothing
		}

		if (f != null) {
			assertEquals(length, Integer.parseInt(String.valueOf(f.get(field))));
		}
	}

	private void binaryFieldHelper(Object x, int id, int length) throws Exception {
		assertNotNull(x);
		assertTrue(x instanceof FieldDefinition);

		FieldDefinition def = (FieldDefinition) x;
		FieldType field = FieldTypes.getType(def);

		assertTrue(field instanceof AbstractHexBinFieldType);
		assertEquals(id, field.getId());

		Field f = null;

		try {
			f = field.getClass().getDeclaredField("length");
			f.setAccessible(true);
		} catch (Throwable t) {
			// do nothing
		}

		if (f != null) {
			assertEquals(length, Integer.parseInt(String.valueOf(f.get(field))));
		}
	}

	@Test
	public void smokeTestStringField() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(FieldDefinition.class).createUnmarshaller();

		int xlen;
		Object x;

		Random rnd = new Random();

		for (int i = 0; i < 200; ++i) {
			for (String s : FieldTypesTest.stringTypes) {
				xlen = rnd.nextInt(800) + 1;

				x = unmarshaller.unmarshal(new ByteArrayInputStream(
								("<iso-field id=\"1\" type=\"" + s
										+ "\" length=\"" + xlen + "\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />")
										.getBytes()));

				stringFieldHelper(x, 1, xlen);
			}
		}
	}

	@Test
	public void smokeTestBinaryField() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(FieldDefinition.class).createUnmarshaller();

		int xlen;
		Object x;

		Random rnd = new Random();

		for (int i = 0; i < 200; ++i) {
			for (String s : FieldTypesTest.binaryTypes) {
				xlen = 1 + rnd.nextInt(799);

				x = unmarshaller.unmarshal(new ByteArrayInputStream(
								("<iso-field id=\"1\" type=\"" + s
										+ "\" length=\"" + xlen + "\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />")
										.getBytes()));

				binaryFieldHelper(x, 1, xlen);
			}
		}
	}

	@Test
	public void testVarStringField() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(FieldDefinition.class).createUnmarshaller();

		Object x;

		for (String s : FieldTypesTest.stringTypes) {
			for (int i = 0; i < FieldTypesTest.dots.length; ++i) {
				x = unmarshaller.unmarshal(new ByteArrayInputStream(
								("<iso-field id=\"1\" type=\"" + s + FieldTypesTest.dots[i] + "\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />")
										.getBytes()));

				stringFieldHelper(x, 1, FieldTypesTest.dotLen[i]);
			}
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testStringFieldWithLenLT0() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(
				FieldDefinition.class).createUnmarshaller();

		for (String s : FieldTypesTest.stringTypes) {
			Object x = unmarshaller
					.unmarshal(new ByteArrayInputStream(
							("<iso-field id=\"1\" type=\"" + s + "\" length=\"0\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />")
									.getBytes()));

			assertNotNull(x);
			assertTrue(x instanceof FieldDefinition);

			FieldTypes.getType((FieldDefinition) x);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBinaryFieldWithLenLT0() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(
				FieldDefinition.class).createUnmarshaller();

		for (String s : FieldTypesTest.binaryTypes) {
			Object x = unmarshaller
					.unmarshal(new ByteArrayInputStream(
							("<iso-field id=\"1\" type=\"" + s + "\" length=\"0\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />")
									.getBytes()));

			assertNotNull(x);
			assertTrue(x instanceof FieldDefinition);

			FieldTypes.getType((FieldDefinition) x);
		}
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowUnknownType() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(
				FieldDefinition.class).createUnmarshaller();

		Object x = unmarshaller
				.unmarshal(new ByteArrayInputStream(
						("<iso-field id=\"1\" type=\"zzz\" length=\"1\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />")
								.getBytes()));

		assertNotNull(x);
		assertTrue(x instanceof FieldDefinition);

		FieldTypes.getType((FieldDefinition) x);
	}
}
