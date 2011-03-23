package org.nucleus8583.core.charset.spi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class AsciiDecoderTest {

	@Test
	public void testClose() throws Exception {
		final AtomicBoolean closed = new AtomicBoolean(false);

		InputStream is = new InputStream() {
			public int read() throws IOException {
				return 0;
			}

			@Override
			public void close() throws IOException {
				closed.set(true);
			}
		};

		AsciiDecoder decoder = new AsciiDecoder(is);
		decoder.close();

		assertTrue(closed.get());
	}

	@Test
	public void testEof() throws Exception {
		AsciiDecoder decoder = new AsciiDecoder(new ByteArrayInputStream(
				new byte[0]));

		assertEquals(-1, decoder.read());
	}

	@Test
	public void testRead() throws Exception {
		AsciiDecoder decoder = new AsciiDecoder(new ByteArrayInputStream(
				"abcde".getBytes()));

		assertEquals('a', decoder.read());
	}

	@Test
	public void testPartialBulkRead() throws Exception {
		AsciiDecoder decoder;
		char[] cbuf = new char[10];

		decoder = new AsciiDecoder(new ByteArrayInputStream("abcde".getBytes()));
		assertEquals(5, decoder.read(cbuf, 0, cbuf.length));
		assertEquals("abcde", new String(cbuf, 0, 5));

		decoder = new AsciiDecoder(new ByteArrayInputStream("abcde".getBytes()));
		assertEquals(5, decoder.read(cbuf, 1, cbuf.length - 1));
		assertEquals("aabcd", new String(cbuf, 0, 5));
	}

	@Test
	public void testNoBulkRead() throws Exception {
		AsciiDecoder decoder = new AsciiDecoder(new ByteArrayInputStream(
				new byte[0]));

		char[] cbuf = new char[10];

		assertEquals(-1, decoder.read(cbuf, 0, cbuf.length));
	}
}
