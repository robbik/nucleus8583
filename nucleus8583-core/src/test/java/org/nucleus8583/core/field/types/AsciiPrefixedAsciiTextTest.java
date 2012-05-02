package org.nucleus8583.core.field.types;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.field.Type;
import org.nucleus8583.core.field.spi.AsciiPrefixedAsciiText;

public class AsciiPrefixedAsciiTextTest {

	private Type stringField;

	@Before
	public void before() throws Exception {
		stringField = new AsciiPrefixedAsciiText();
		
		((AsciiPrefixedAsciiText) stringField).setPrefixLength(1);
		((AsciiPrefixedAsciiText) stringField).setMaxLength(9);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void packBitmap() throws Exception {
		stringField.writeBitmap(new ByteArrayOutputStream(), new byte[0], 0, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void unpackBitmap() throws Exception {
		stringField.readBitmap(new ByteArrayInputStream("a".getBytes()), new byte[0], 0, 0);
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

	@Test
	public void unpackString() throws Exception {
		assertEquals("20", stringField.read(new ByteArrayInputStream("220".getBytes())));
	}

	@Test
	public void unpackEmptyString() throws Exception {
		assertEquals("", stringField.read(new ByteArrayInputStream("0".getBytes())));
	}

	@Test(expected = EOFException.class)
	public void unpackStringUnpadOverflow() throws Exception {
		stringField.read(new ByteArrayInputStream("5ab".getBytes()));
	}
}
