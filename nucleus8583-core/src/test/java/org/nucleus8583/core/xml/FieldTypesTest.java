package org.nucleus8583.core.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Random;

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
		Random rnd = new Random();

		for (int i = 0; i < 200; ++i) {
			for (String s : FieldTypesTest.stringTypes) {
				int xlen = rnd.nextInt(800) + 1;

				FieldDefinition def = new FieldDefinition();
				def.setId(1);
				def.setType(s);
				def.setLength(xlen);

				stringFieldHelper(def, 1, xlen);
			}
		}
	}

	@Test
	public void smokeTestBinaryField() throws Exception {
		Random rnd = new Random();

		for (int i = 0; i < 200; ++i) {
			for (String s : FieldTypesTest.binaryTypes) {
				int xlen = 1 + rnd.nextInt(799);

                FieldDefinition def = new FieldDefinition();
                def.setId(1);
                def.setType(s);
                def.setLength(xlen);

				binaryFieldHelper(def, 1, xlen);
			}
		}
	}

	@Test
	public void testVarStringField() throws Exception {
		for (String s : FieldTypesTest.stringTypes) {
			for (int i = 0; i < FieldTypesTest.dots.length; ++i) {
                FieldDefinition def = new FieldDefinition();
                def.setId(1);
                def.setType(s + FieldTypesTest.dots[i]);

				stringFieldHelper(def, 1, FieldTypesTest.dotLen[i]);
			}
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testStringFieldWithLenLT0() throws Exception {
		for (String s : FieldTypesTest.stringTypes) {
            FieldDefinition def = new FieldDefinition();
            def.setId(1);
            def.setType(s);
            def.setLength(0);

			FieldTypes.getType(def);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBinaryFieldWithLenLT0() throws Exception {
		for (String s : FieldTypesTest.binaryTypes) {
            FieldDefinition def = new FieldDefinition();
            def.setId(1);
            def.setType(s);
            def.setLength(0);

			FieldTypes.getType(def);
		}
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowUnknownType() throws Exception {
        FieldDefinition def = new FieldDefinition();
        def.setId(1);
        def.setType("zzz");
        def.setLength(1);

        FieldTypes.getType(def);
	}
}
