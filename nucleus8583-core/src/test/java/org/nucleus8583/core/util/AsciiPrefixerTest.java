package org.nucleus8583.core.util;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Random;

import org.junit.Test;

public class AsciiPrefixerTest {

	@Test
	public void smokeTestWriteUint() throws IOException {
		Random rnd = new Random();

		for (int i = 1; i < 1000; ++i) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			int z = rnd.nextInt(Integer.MAX_VALUE - 100);

			new AsciiPrefixer(10).writeUint(out, z);
			out.flush();

			String sz = out.toString("ASCII");
			assertEquals(sz + " ? " + z, z, Integer.parseInt(sz));
		}
	}

	@Test
	public void smokeTestReadUint() throws IOException {
		Random rnd = new Random();

		for (int i = 1; i < 1000; ++i) {
			int z = rnd.nextInt(Integer.MAX_VALUE - 100);
			String sz = String.valueOf(z);

			assertEquals(sz, z,
					new AsciiPrefixer(sz.length())
							.readUint(new ByteArrayInputStream(sz
									.getBytes("ASCII"))));
		}
	}

	@Test(expected = NumberFormatException.class)
	public void readUintShouldThrowNumberFormatException() throws IOException {
		new AsciiPrefixer(1).readUint(new ByteArrayInputStream("a".getBytes()));
	}

	@Test(expected = EOFException.class)
	public void readUintShouldThrowEOFException() throws IOException {
		new AsciiPrefixer(2).readUint(new ByteArrayInputStream("1".getBytes()));
	}
}
