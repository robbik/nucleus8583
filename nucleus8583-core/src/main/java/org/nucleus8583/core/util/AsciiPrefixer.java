package org.nucleus8583.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AsciiPrefixer {
	private static final int[][] digitsToInt;

	private static final byte[] intToDigits;

	private static final int MAX_DIGIT = 10;

	static {
		digitsToInt = new int[Character.MAX_VALUE][MAX_DIGIT];

		for (int i = 0; i < digitsToInt.length; ++i) {
			if ((i >= '0') && (i <= '9')) {
				int tens = 1;
				int digit = i - '0';

				for (int j = 0; j < MAX_DIGIT; ++j) {
					digitsToInt[i][j] = digit * tens;
					tens *= 10;
				}
			} else {
				for (int j = 0; j < MAX_DIGIT; ++j) {
					digitsToInt[i][j] = -1;
				}
			}
		}

		intToDigits = new byte[] { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9' };
	}

	private int prefixLength;

	public AsciiPrefixer(int prefixLength) {
		this.prefixLength = prefixLength;
	}

	public void writeUint(OutputStream out, int value) throws IOException {
		int rem = value;

		byte[] buf = new byte[prefixLength];

		for (int i = prefixLength - 1; i >= 0; --i) {
			buf[i] = intToDigits[rem % 10];
			rem = rem / 10;
		}

		out.write(buf);
	}

	public int readUint(InputStream in) throws IOException {
		int value = 0;

		byte[] bbuf = new byte[prefixLength];
		IOUtils.readFully(in, bbuf, prefixLength);

		for (int i = prefixLength - 1, j = 0; i >= 0; --i, ++j) {
			int digitInt = digitsToInt[bbuf[j]][i];
			if (digitInt < 0) {
				throw new NumberFormatException((char) bbuf[i] + " is not a number.");
			}

			value += digitInt;
		}

		return value;
	}
}
