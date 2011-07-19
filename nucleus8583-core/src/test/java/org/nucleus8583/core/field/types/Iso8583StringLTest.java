package org.nucleus8583.core.field.types;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.BitSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.field.type.Iso8583FieldType;
import org.nucleus8583.core.field.type.Iso8583FieldTypes;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public class Iso8583StringLTest {
	private Iso8583FieldType stringField;

	@Before
	public void before() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(
				Iso8583FieldDefinition.class).createUnmarshaller();

		stringField = Iso8583FieldTypes.getType((Iso8583FieldDefinition) unmarshaller
				.unmarshal(new ByteArrayInputStream(
						("<iso-field id=\"39\" type=\"a .\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />").getBytes())));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void packBinary() throws Exception {
		stringField.write(new StringWriter(), new BitSet());
	}

	@Test
	public void packString() throws Exception {
		StringWriter sw = new StringWriter();
		stringField.write(sw, "20");
		assertEquals("220", sw.toString());
	}

	@Test
	public void packEmptyString() throws Exception {
		StringWriter sw = new StringWriter();
		stringField.write(sw, "");
		assertEquals("0", sw.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void packStringOverflow() throws Exception {
		StringWriter sw = new StringWriter();
		stringField.write(sw, "abcdefghji");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void unpackBinary1() throws Exception {
		stringField.read(new StringReader("a"), new BitSet());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void unpackBinary2() throws Exception {
		stringField.readBinary(new StringReader("a"));
	}

	@Test
	public void unpackString() throws Exception {
		assertEquals("20", stringField.readString(new StringReader("220")));
	}

	@Test
	public void unpackEmptyString() throws Exception {
		assertEquals("", stringField.readString(new StringReader("0")));
	}

	@Test(expected = EOFException.class)
	public void unpackStringUnpadOverflow() throws Exception {
		stringField.readString(new StringReader("5ab"));
	}
}
