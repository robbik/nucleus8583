package org.nucleus8583.core.charset.spi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class AsciEncoderTest {

	@Test
	public void testClose() throws Exception {
		final AtomicBoolean closed = new AtomicBoolean(false);

		OutputStream is = new OutputStream() {

			public void write(int b) throws IOException {
				// do nothing
			}

			@Override
			public void close() throws IOException {
				closed.set(true);
			}
		};

		AsciiEncoder encoder = new AsciiEncoder(is);
		encoder.close();

		assertTrue(closed.get());
	}

	@Test
	public void testFlush() throws Exception {
		final AtomicBoolean flushed = new AtomicBoolean(false);

		OutputStream is = new OutputStream() {

			public void write(int b) throws IOException {
				// do nothing
			}

			@Override
			public void flush() throws IOException {
				flushed.set(true);
			}
		};

		AsciiEncoder encoder = new AsciiEncoder(is);
		encoder.flush();

		assertTrue(flushed.get());
	}

	@Test
	public void testWriteString() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		AsciiEncoder encoder = new AsciiEncoder(out);

		encoder.write("abcde");
		encoder.flush();

		assertEquals("abcde", new String(out.toByteArray()));
	}

	@Test
	public void testWriteString2() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		AsciiEncoder encoder = new AsciiEncoder(out);

		encoder.write("abcde", 1, 3);
		encoder.flush();

		assertEquals("bcd", new String(out.toByteArray()));
	}

	@Test
	public void testWriteString3() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		AsciiEncoder encoder = new AsciiEncoder(out);

		encoder.write("abcde", 0, 3);
		encoder.flush();

		assertEquals("abc", new String(out.toByteArray()));
	}

	@Test
	public void testWriteChar() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		AsciiEncoder encoder = new AsciiEncoder(out);

		encoder.write('a');
		encoder.flush();

		assertEquals("a", new String(out.toByteArray()));
	}

	@Test
	public void testWriteCharArray() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		AsciiEncoder encoder = new AsciiEncoder(out);

		encoder.write("abcde".toCharArray());
		encoder.flush();

		assertEquals("abcde", new String(out.toByteArray()));
	}

	@Test
	public void testWriteCharArray2() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		AsciiEncoder encoder = new AsciiEncoder(out);

		encoder.write("abcde".toCharArray(), 1, 3);
		encoder.flush();

		assertEquals("bcd", new String(out.toByteArray()));
	}

	@Test
	public void testWriteCharArray3() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		AsciiEncoder encoder = new AsciiEncoder(out);

		encoder.write("abcde".toCharArray(), 0, 3);
		encoder.flush();

		assertEquals("abc", new String(out.toByteArray()));
	}
}
