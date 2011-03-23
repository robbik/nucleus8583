package org.nucleus8583.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class FastStringReaderTest {

	@Test
	public void markSupportedTest() throws Exception {
		FastStringReader reader = new FastStringReader("ance");
		assertFalse(reader.markSupported());
	}

	@Test(expected = IOException.class)
	public void markTest() throws Exception {
		FastStringReader reader = new FastStringReader("ance");
		reader.mark(0);
	}

	@Test
	public void testNull() throws Exception {
		FastStringReader reader = new FastStringReader(null);
		assertFalse(reader.ready());
		assertEquals(-1, reader.read());
		assertEquals(0, reader.skip(1));
	}

	@Test
	public void testEmptyString() throws Exception {
		FastStringReader reader = new FastStringReader("");
		assertFalse(reader.ready());
		assertEquals(-1, reader.read());
		assertEquals(0, reader.skip(1));
	}

	@Test
	public void testReset() throws Exception {
		FastStringReader reader = new FastStringReader("abcd");

		assertTrue(reader.ready());
		assertEquals('a', reader.read());

		reader.reset();
		assertEquals('a', reader.read());
	}

	@Test
	public void testClose() throws Exception {
		FastStringReader reader = new FastStringReader("abcd");
		assertTrue(reader.ready());

		reader.close();
		assertFalse(reader.ready());
	}

	@Test
	public void testSkip() throws Exception {
		FastStringReader reader = new FastStringReader("abcd");

		reader.skip(1);
		assertEquals('b', reader.read());

		reader.skip(7);
		assertEquals(-1, reader.read());
		
		reader.reset();
		reader.skip(2);
		
		char[] cbuf = new char[20];
		
		int readb = reader.read(cbuf);
		assertEquals(2, readb);
		
		assertEquals("cd", new String(cbuf, 0, readb));
	}

	@Test
	public void testRead() throws Exception {
		FastStringReader reader = new FastStringReader("abcd");
		char[] cbuf = new char[20];
		
		int readb = reader.read(cbuf);
		assertEquals(4, readb);
		assertEquals("abcd", new String(cbuf, 0, readb));
		
		reader.reset();
		
		readb = reader.read(cbuf, 0, 0);
		assertEquals(0, readb);
	}
}
