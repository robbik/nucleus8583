package org.nucleus8583.core.charset;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Utf8ProviderTest {

	@Test
	public void testEncoder() throws Exception {
		CharsetEncoder enc = new Utf8Provider().getEncoder();

		assertNotNull(enc);
		assertTrue(enc instanceof Utf8Encoder);
	}

	@Test
	public void testDecoder() throws Exception {
		CharsetDecoder dec = new Utf8Provider().getDecoder();

		assertNotNull(dec);
		assertTrue(dec instanceof Utf8Decoder);
	}
}
