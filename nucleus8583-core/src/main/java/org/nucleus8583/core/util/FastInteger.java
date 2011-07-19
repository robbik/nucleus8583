package org.nucleus8583.core.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;

public abstract class FastInteger {
	private static final int[][] digitsToInt;

	private static final char[] intToDigits;

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

		intToDigits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9' };
	}

	public static String uintToString(int ivalue, int len) {
		int rem = ivalue;
		char[] cc = new char[len];

		for (int i = len - 1; i >= 0; --i) {
			cc[i] = intToDigits[rem % 10];
			rem = rem / 10;
		}

		return new String(cc);
	}

	public static void writeUint(OutputStream out, CharsetEncoder enc,
			int ivalue, int len) throws IOException {
		int rem = ivalue;
		char[] cc = new char[len];

		for (int i = len - 1; i >= 0; --i) {
			cc[i] = intToDigits[rem % 10];
			rem = rem / 10;
		}

		enc.write(out, cc, 0, len);
	}

	public static void writeUint(Writer writer, int ivalue, int len)
			throws IOException {
		int rem = ivalue;
		char[] cc = new char[len];

		for (int i = len - 1; i >= 0; --i) {
			cc[i] = intToDigits[rem % 10];
			rem = rem / 10;
		}

		writer.write(cc, 0, len);
	}

	public static int readUint(InputStream in, CharsetDecoder dec, int len)
			throws IOException {
		int ivalue = 0;
		int ichar;

		for (int i = len - 1; i >= 0; --i) {
			ichar = dec.read(in);
			if (ichar < 0) {
				throw new EOFException();
			}

			int digitInt = digitsToInt[ichar][i];
			if (digitInt < 0) {
				throw new NumberFormatException((char) ichar
						+ " is not a number.");
			}

			ivalue += digitInt;
		}

		return ivalue;
	}

	public static int readUint(Reader reader, int len) throws IOException {
		int ivalue = 0;
		int ichar;

		for (int i = len - 1; i >= 0; --i) {
			ichar = reader.read();
			if (ichar < 0) {
				throw new EOFException();
			}

			int digitInt = digitsToInt[ichar][i];
			if (digitInt < 0) {
				throw new NumberFormatException((char) ichar
						+ " is not a number.");
			}

			ivalue += digitInt;
		}

		return ivalue;
	}

	public static int parseUint(char[] s, int start, int len) {
		int ivalue = 0;

		for (int i = len - 1, j = start; i >= 0; --i, ++j) {
			int digitInt = digitsToInt[s[j]][i];
			if (digitInt < 0) {
				throw new NumberFormatException(s[j] + " is not a number.");
			}

			ivalue += digitInt;
		}

		return ivalue;
	}

	public static int parseUint(char[] s) {
		return parseUint(s, 0, s.length);
	}

	public static int parseUint(CharSequence s, int len) {
		int ivalue = 0;

		for (int i = len - 1, j = 0; i >= 0; --i, ++j) {
			int digitInt = digitsToInt[s.charAt(j)][i];
			if (digitInt < 0) {
				throw new NumberFormatException(s.charAt(j)
						+ " is not a number.");
			}

			ivalue += digitInt;
		}

		return ivalue;
	}

	public static int parseUint(CharSequence s) {
		return parseUint(s, s.length());
	}
}
