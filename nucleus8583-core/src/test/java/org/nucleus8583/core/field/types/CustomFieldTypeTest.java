package org.nucleus8583.core.field.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.charset.Charsets;
import org.nucleus8583.core.field.type.FieldType;
import org.nucleus8583.core.field.type.FieldTypes;
import org.nucleus8583.core.xml.FieldDefinition;

public class CustomFieldTypeTest {

	private CharsetEncoder encoder;

	@Before
	public void before() throws Exception {
		encoder = Charsets.getProvider("ASCII").getEncoder();
	}

	@Test
	public void testDummyField() throws Exception {
        FieldDefinition def = new FieldDefinition();
        def.setId(1);
        def.setType("dummy");
        def.setLength(4);

		FieldType f = FieldTypes.getType(def);

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
        FieldDefinition def = new FieldDefinition();
        def.setId(1);
        def.setType("dummy2");
        def.setLength(4);

		FieldTypes.getType(def);
	}

	@Test(expected = RuntimeException.class)
	public void testDummyField3() throws Exception {
        FieldDefinition def = new FieldDefinition();
        def.setId(1);
        def.setType("dummy3");
        def.setLength(4);

		FieldTypes.getType(def);
	}
}
