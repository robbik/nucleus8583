package org.nucleus8583.core.charset;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;

import org.junit.Test;

public class AsciiDecoderTest {

	@Test
	public void testEof() throws Exception {
		AsciiDecoder decoder = new AsciiDecoder();

		assertEquals(-1, decoder.read(new ByteArrayInputStream(new byte[0])));
	}

	@Test
	public void testRead() throws Exception {
		AsciiDecoder decoder = new AsciiDecoder();

		assertEquals('a', decoder.read(new ByteArrayInputStream("abcde".getBytes())));
	}

	@Test
	public void testPartialBulkRead() throws Exception {
		AsciiDecoder decoder = new AsciiDecoder();
		char[] cbuf = new char[10];

		assertEquals(5, decoder.read(new ByteArrayInputStream("abcde".getBytes()), cbuf, 0, cbuf.length));
		assertEquals("abcde", new String(cbuf, 0, 5));

		assertEquals(5, decoder.read(new ByteArrayInputStream("abcde".getBytes()), cbuf, 1, cbuf.length - 1));
		assertEquals("aabcd", new String(cbuf, 0, 5));
	}

	@Test
	public void testNoBulkRead() throws Exception {
		AsciiDecoder decoder = new AsciiDecoder();
		char[] cbuf = new char[10];

		assertEquals(-1, decoder.read(new ByteArrayInputStream(new byte[0]), cbuf, 0, cbuf.length));
	}
}
