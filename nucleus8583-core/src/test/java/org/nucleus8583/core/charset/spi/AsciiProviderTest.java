package org.nucleus8583.core.charset.spi;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.Writer;

import org.junit.Test;

public class AsciiProviderTest {

	@Test
	public void testEncoder() throws Exception {
		Writer writer = new AsciiProvider()
				.createEncoder(new ByteArrayOutputStream());

		assertNotNull(writer);
		assertTrue(writer instanceof AsciiEncoder);
	}

	@Test
	public void testDecoder() throws Exception {
		Reader reader = new AsciiProvider()
				.createDecoder(new ByteArrayInputStream(new byte[0]));

		assertNotNull(reader);
		assertTrue(reader instanceof AsciiDecoder);
	}
}
