package org.nucleus8583.core.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.BufferOverflowException;

public class AsciiPrefixer {

	private static final int[][] digitsToInt;

	private static final byte[] intToDigits;

	private static final int MAX_DIGIT = 10;

	static {
        digitsToInt = new int[10][MAX_DIGIT];

        for (int i = 0, len = digitsToInt.length; i < len; ++i) {
            int tens = 1;

            for (int j = 0; j < MAX_DIGIT; ++j) {
                digitsToInt[i][j] = i * tens;
                tens *= 10;
            }
        }

        intToDigits = new byte[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	}

	private int prefixLength;
	
	public AsciiPrefixer() {
		// do nothing
	}
	
	public AsciiPrefixer(AsciiPrefixer o) {
		prefixLength = o.prefixLength;
	}

	public void setPrefixLength(int prefixLength) {
		this.prefixLength = prefixLength;
	}

	public void writeUint(OutputStream out, int value) throws IOException {
		byte[] buf = new byte[prefixLength];

		writeUint(buf, 0, value);

		out.write(buf);
	}

	public int readUint(InputStream in) throws IOException {
		byte[] bbuf = new byte[prefixLength];
		IOHelper.readFully(in, bbuf, prefixLength);

		return readUint(bbuf, 0);
	}

	public int readUint(byte[] in, int start) throws IOException {
		if (in.length - start < prefixLength) {
			throw new EOFException();
		}

		int value = 0;

		for (int i = prefixLength - 1, j = start; i >= 0; --i, ++j) {
			int digitInt = in[j] & 0xFF;
			if ((digitInt < '0') || (digitInt > '9')) {
				throw new NumberFormatException((char) in[i] + " is not a number.");
			}

			value += digitsToInt[digitInt - '0'][i];
		}

		return value;
	}

	public void writeUint(byte[] out, int start, int value) throws IOException {
		if (out.length - start < prefixLength) {
			throw new ArrayIndexOutOfBoundsException();
		}

		int rem = value;

		for (int i = start + prefixLength - 1; i >= 0; --i) {
			out[i] = intToDigits[rem % 10];
			rem = rem / 10;
		}
	}
}
