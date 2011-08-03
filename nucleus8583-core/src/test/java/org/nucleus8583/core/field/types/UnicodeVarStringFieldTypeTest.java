package org.nucleus8583.core.field.types;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.charset.Charsets;
import org.nucleus8583.core.field.type.FieldType;
import org.nucleus8583.core.field.type.FieldTypes;
import org.nucleus8583.core.xml.FieldDefinition;

public class UnicodeVarStringFieldTypeTest {

	private CharsetEncoder encoder;

	private CharsetDecoder decoder;

	private FieldType stringField;

	@Before
	public void before() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(FieldDefinition.class).createUnmarshaller();

		encoder = Charsets.getProvider("ASCII").getEncoder();
		decoder = Charsets.getProvider("ASCII").getDecoder();

		stringField = FieldTypes.getType((FieldDefinition) unmarshaller
				.unmarshal(new ByteArrayInputStream(
						("<iso-field id=\"39\" type=\"a .\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />").getBytes())));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void packBinary() throws Exception {
		stringField.write(new ByteArrayOutputStream(), encoder, new byte[0]);
	}

	@Test
	public void packString() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		stringField.write(out, encoder, "20");
		assertEquals("220", out.toString());
	}

	@Test
	public void packEmptyString() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		stringField.write(out, encoder, "");
		assertEquals("0", out.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void packStringOverflow() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		stringField.write(out, encoder, "abcdefghji");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void unpackBinary1() throws Exception {
		stringField.read(new ByteArrayInputStream("a".getBytes()), decoder, new byte[0]);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void unpackBinary2() throws Exception {
		stringField.readBinary(new ByteArrayInputStream("a".getBytes()), decoder);
	}

	@Test
	public void unpackString() throws Exception {
		assertEquals("20", stringField.readString(new ByteArrayInputStream("220".getBytes()), decoder));
	}

	@Test
	public void unpackEmptyString() throws Exception {
		assertEquals("", stringField.readString(new ByteArrayInputStream("0".getBytes()), decoder));
	}

	@Test(expected = EOFException.class)
	public void unpackStringUnpadOverflow() throws Exception {
		stringField.readString(new ByteArrayInputStream("5ab".getBytes()), decoder);
	}
}
