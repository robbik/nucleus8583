package org.nucleus8583.core.charset.spi;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.Writer;

import org.junit.Test;

public class Utf8ProviderTest {

	@Test
	public void testEncoder() throws Exception {
		Writer writer = new Utf8Provider()
				.createEncoder(new ByteArrayOutputStream());

		assertNotNull(writer);
		assertTrue(writer instanceof Utf8Encoder);
	}

	@Test
	public void testDecoder() throws Exception {
		Reader reader = new Utf8Provider()
				.createDecoder(new ByteArrayInputStream(new byte[0]));

		assertNotNull(reader);
		assertTrue(reader instanceof Utf8Decoder);
	}
}
