package org.nucleus8583.core.util;

import org.junit.jupiter.api.Test;

import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FastIntegerTest {

	@Test
	void smokeTestUintToString() {
		Random rnd = new Random();

		for (int i = 1; i < 1000; ++i) {
			int z = rnd.nextInt(Integer.MAX_VALUE - 100);
			String sz = FastInteger.uintToString(z, 10);

			assertThat(sz, Integer.parseInt(sz), is(z));
		}
	}

	@Test
	void smokeTestWriteUint() throws IOException {
		Random rnd = new Random();

		for (int i = 1; i < 1000; ++i) {
			StringWriter sw = new StringWriter();

			int z = rnd.nextInt(Integer.MAX_VALUE - 100);
			FastInteger.writeUint(sw, z, 10);

			String sz = sw.toString();
			assertThat(sz, Integer.parseInt(sz), is(z));
		}
	}

	@Test
	void smokeTestParseUint() {
		Random rnd = new Random();

		for (int i = 1; i < 1000; ++i) {
			int z = rnd.nextInt(Integer.MAX_VALUE - 100);
			String sz = String.valueOf(z);

			assertThat(sz, FastInteger.parseUint(sz), is(z));
		}
	}

	@Test
	void smokeTestParseUintCharArray() {
		Random rnd = new Random();

		for (int i = 1; i < 1000; ++i) {
			int z = rnd.nextInt(Integer.MAX_VALUE - 100);
			String sz = String.valueOf(z);

			assertThat(sz, FastInteger.parseUint(sz.toCharArray()), is(z));
		}
	}

	@Test
	void smokeTestReadUint() throws IOException {
		Random rnd = new Random();

		for (int i = 1; i < 1000; ++i) {
			int z = rnd.nextInt(Integer.MAX_VALUE - 100);
			String sz = String.valueOf(z);

			assertThat(sz, FastInteger.readUint(new StringReader(sz), sz.length()), is(z));
		}
	}

	@Test
	void parseUintShouldThrowNumberFormatException() {
		assertThrows(NumberFormatException.class, () -> FastInteger.parseUint("a"));
	}

	@Test
	void parseUintCharArrayShouldThrowNumberFormatException() {
		assertThrows(NumberFormatException.class, () -> FastInteger.parseUint("a".toCharArray()));
	}

	@Test
	void readUintShouldThrowNumberFormatException() {
		assertThrows(NumberFormatException.class, () -> FastInteger.readUint(new StringReader("a"), 1));
	}

	@Test
	void readUintShouldThrowEOFException() {
		assertThrows(EOFException.class, () -> FastInteger.readUint(new StringReader("1"), 2));
	}
}
