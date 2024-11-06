package org.nucleus8583.core.util;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AsciiPrefixerTest {

	@Test
	void smokeTestWriteUint() throws IOException {
		Random rnd = new Random();

		for (int i = 1; i < 1000; ++i) {
			try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
				int z = rnd.nextInt(Integer.MAX_VALUE - 100);

				AsciiPrefixer p = new AsciiPrefixer();
				p.setPrefixLength(10);

				p.writeUint(out, z);
				out.flush();

				String sz = out.toString(StandardCharsets.US_ASCII);
				assertThat(sz + " ? " + z, Integer.parseInt(sz), is(z));
			}
		}
	}

	@Test
	void smokeTestReadUint() throws IOException {
		Random rnd = new Random();

		for (int i = 1; i < 1000; ++i) {
			int z = rnd.nextInt(Integer.MAX_VALUE - 100);
			String sz = String.valueOf(z);

			AsciiPrefixer p = new AsciiPrefixer();
			p.setPrefixLength(sz.length());

			assertThat(sz, p.readUint(new ByteArrayInputStream(sz.getBytes(StandardCharsets.US_ASCII))), is(z));
		}
	}

	@Test
	void readUintShouldThrowNumberFormatException() {
		assertThrows(NumberFormatException.class, () -> {
			AsciiPrefixer p = new AsciiPrefixer();
			p.setPrefixLength(1);

			p.readUint(new ByteArrayInputStream("a".getBytes()));
		});
	}

	@Test
	void readUintShouldThrowEOFException() {
		assertThrows(EOFException.class, () -> {
			AsciiPrefixer p = new AsciiPrefixer();
			p.setPrefixLength(2);

			p.readUint(new ByteArrayInputStream("1".getBytes()));
		});
	}
}
