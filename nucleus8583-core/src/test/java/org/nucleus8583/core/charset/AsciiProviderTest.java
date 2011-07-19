package org.nucleus8583.core.charset;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AsciiProviderTest {

	@Test
	public void testEncoder() throws Exception {
		CharsetEncoder enc = new AsciiProvider().getEncoder();

		assertNotNull(enc);
		assertTrue(enc instanceof AsciiEncoder);
	}

	@Test
	public void testDecoder() throws Exception {
		CharsetDecoder dec= new AsciiProvider().getDecoder();

		assertNotNull(dec);
		assertTrue(dec instanceof AsciiDecoder);
	}
}
