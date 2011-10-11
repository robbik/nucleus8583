package org.nucleus8583.core.field.types;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.field.type.FieldType;
import org.nucleus8583.core.field.type.FieldTypes;
import org.nucleus8583.core.xml.FieldDefinition;

public class AsciiPrefixedAsciiTextTest {

	private FieldType stringField;

	@Before
	public void before() throws Exception {
        FieldDefinition def = new FieldDefinition();
        def.setId(39);
        def.setType("a .");

        stringField = FieldTypes.getType(def);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void packBinary() throws Exception {
		stringField.write(new ByteArrayOutputStream(), new byte[0]);
	}

	@Test
	public void packString() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		stringField.write(out, "20");
		assertEquals("220", out.toString());
	}

	@Test
	public void packEmptyString() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		stringField.write(out, "");
		assertEquals("0", out.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void packStringOverflow() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		stringField.write(out, "abcdefghji");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void unpackBinary1() throws Exception {
		stringField.read(new ByteArrayInputStream("a".getBytes()), new byte[0], 0, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void unpackBinary2() throws Exception {
		stringField.readBinary(new ByteArrayInputStream("a".getBytes()));
	}

	@Test
	public void unpackString() throws Exception {
		assertEquals("20", stringField.readString(new ByteArrayInputStream("220".getBytes())));
	}

	@Test
	public void unpackEmptyString() throws Exception {
		assertEquals("", stringField.readString(new ByteArrayInputStream("0".getBytes())));
	}

	@Test(expected = EOFException.class)
	public void unpackStringUnpadOverflow() throws Exception {
		stringField.readString(new ByteArrayInputStream("5ab".getBytes()));
	}
}
