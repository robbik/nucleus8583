package org.nucleus8583.core.charset;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

public class AsciEncoderTest {

	@Test
	public void testWriteString() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		AsciiEncoder encoder = new AsciiEncoder();

		encoder.write(out, "abcde");

		assertEquals("abcde", new String(out.toByteArray()));
	}

	@Test
	public void testWriteString2() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		AsciiEncoder encoder = new AsciiEncoder();

		encoder.write(out, "abcde", 1, 3);

		assertEquals("bcd", new String(out.toByteArray()));
	}

	@Test
	public void testWriteString3() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		AsciiEncoder encoder = new AsciiEncoder();

		encoder.write(out, "abcde", 0, 3);

		assertEquals("abc", new String(out.toByteArray()));
	}

	@Test
	public void testWriteChar() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		AsciiEncoder encoder = new AsciiEncoder();

		encoder.write(out, 'a');

		assertEquals("a", new String(out.toByteArray()));
	}

	@Test
	public void testWriteCharArray() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		AsciiEncoder encoder = new AsciiEncoder();

		encoder.write(out, "abcde".toCharArray());

		assertEquals("abcde", new String(out.toByteArray()));
	}

	@Test
	public void testWriteCharArray2() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		AsciiEncoder encoder = new AsciiEncoder();

		encoder.write(out, "abcde".toCharArray(), 1, 3);

		assertEquals("bcd", new String(out.toByteArray()));
	}

	@Test
	public void testWriteCharArray3() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		AsciiEncoder encoder = new AsciiEncoder();

		encoder.write(out, "abcde".toCharArray(), 0, 3);

		assertEquals("abc", new String(out.toByteArray()));
	}
}
