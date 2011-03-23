package org.nucleus8583.core.util;

import static org.junit.Assert.assertEquals;

import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;

import org.junit.Test;

public class FastIntegerTest {

	@Test
	public void smokeTestUintToString() {
		Random rnd = new Random();

		for (int i = 1; i < 1000; ++i) {
			int z = rnd.nextInt(Integer.MAX_VALUE - 100);
			String sz = FastInteger.uintToString(z, 10);

			assertEquals(sz, z, Integer.parseInt(sz));
		}
	}

	@Test
	public void smokeTestWriteUint() throws IOException {
		Random rnd = new Random();

		for (int i = 1; i < 1000; ++i) {
			StringWriter sw = new StringWriter();

			int z = rnd.nextInt(Integer.MAX_VALUE - 100);
			FastInteger.writeUint(sw, z, 10);

			String sz = sw.toString();
			assertEquals(sz, z, Integer.parseInt(sz));
		}
	}

	@Test
	public void smokeTestParseUint() {
		Random rnd = new Random();

		for (int i = 1; i < 1000; ++i) {
			int z = rnd.nextInt(Integer.MAX_VALUE - 100);
			String sz = String.valueOf(z);

			assertEquals(sz, z, FastInteger.parseUint(sz));
		}
	}

	@Test
	public void smokeTestParseUintCharArray() {
		Random rnd = new Random();

		for (int i = 1; i < 1000; ++i) {
			int z = rnd.nextInt(Integer.MAX_VALUE - 100);
			String sz = String.valueOf(z);

			assertEquals(sz, z, FastInteger.parseUint(sz.toCharArray()));
		}
	}

	@Test
	public void smokeTestReadUint() throws IOException {
		Random rnd = new Random();

		for (int i = 1; i < 1000; ++i) {
			int z = rnd.nextInt(Integer.MAX_VALUE - 100);
			String sz = String.valueOf(z);

			assertEquals(sz, z, FastInteger.readUint(new StringReader(sz), sz
					.length()));
		}
	}

	@Test(expected = NumberFormatException.class)
	public void parseUintShouldThrowNumberFormatException() {
		FastInteger.parseUint("a");
	}

	@Test(expected = NumberFormatException.class)
	public void parseUintCharArrayShouldThrowNumberFormatException() {
		FastInteger.parseUint("a".toCharArray());
	}

	@Test(expected = NumberFormatException.class)
	public void readUintShouldThrowNumberFormatException() throws IOException {
		FastInteger.readUint(new StringReader("a"), 1);
	}

	@Test(expected = EOFException.class)
	public void readUintShouldThrowEOFException() throws IOException {
		FastInteger.readUint(new StringReader("1"), 2);
	}
}
