package org.nucleus8583.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.nucleus8583.core.xml.FieldAlignments;

public class Base16Padder {

	private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static int hex2int(char ichar) {
		switch (ichar) {
		case '0':
			break;
		case '1':
			// 0001
			return 1;
		case '2':
			// 0010
			return 2;
		case '3':
			// 0011
			return 3;
		case '4':
			// 0100
			return 4;
		case '5':
			// 0101
			return 5;
		case '6':
			// 0110
			return 6;
		case '7':
			// 0111
			return 7;
		case '8':
			// 1000
			return 8;
		case '9':
			// 1001
			return 9;
		case 'A':
			// 1010
			return 10;
		case 'B':
			// 1011
			return 11;
		case 'C':
			// 1100
			return 12;
		case 'D':
			// 1101
			return 13;
		case 'E':
			// 1110
			return 14;
		case 'F':
			// 1111
			return 15;
		}

		return 0;
	}

	private byte padWith;

	private FieldAlignments align;

	private int length;

	private byte[] padder;

	private byte[] emptyValue;

	public void setPadWith(String padWith) {
		int len = padWith.length();
		if (len == 0) {
			padWith = "00";
		} else if (len == 1) {
			padWith += padWith;
		}

		setPadWith((byte) ((hex2int(padWith.charAt(0)) << 4) | hex2int(padWith
				.charAt(1))));
	}

	public void setPadWith(byte padWith) {
		this.padWith = padWith;
	}

	public void setAlign(FieldAlignments align) {
		this.align = align;
	}

	public FieldAlignments getAlign() {
		return align;
	}

	public void setLength(int length) {
		this.length = (length + 1) >> 1;
	}

	public void setEmptyValue(byte[] emptyValue) {
		this.emptyValue = emptyValue;
	}

	public void setEmptyValue(String emptyValue) {
		int len = emptyValue.length();
		if (len == 0) {
			emptyValue = "00";
		} else if ((len % 2) != 0) {
			emptyValue = "0" + emptyValue;
		}

		this.emptyValue = new byte[emptyValue.length() >> 1];
		for (int i = 0, j = 0; i < this.emptyValue.length; ++i, j += 2) {
			this.emptyValue[i] = (byte) ((hex2int(emptyValue.charAt(j)) << 4) | hex2int(emptyValue
					.charAt(j + 1)));
		}
	}

	public void initialize() {
		padder = new byte[length];
		Arrays.fill(padder, padWith);
	}

	public void pad(OutputStream out, byte[] value, int off, int vlen)
			throws IOException {
		if (vlen == 0) {
			write(out, padder, 0, length);
		} else if (vlen == length) {
			write(out, value, off, vlen);
		} else {
			switch (align) {
			case TRIMMED_LEFT:
			case UNTRIMMED_LEFT:
				write(out, value, off, vlen);
				write(out, padder, 0, length - vlen);

				break;
			case TRIMMED_RIGHT:
			case UNTRIMMED_RIGHT:
				write(out, padder, 0, length - vlen);
				write(out, value, off, vlen);

				break;
			default: // NONE
				write(out, value, off, vlen);
				write(out, padder, 0, length - vlen);

				break;
			}
		}
	}

	public byte[] unpad(InputStream in) throws IOException {
		byte[] value = new byte[length << 1];
		read(in, value, 0, value.length);

		byte[] result;
		int resultLength;

		switch (align) {
		case TRIMMED_LEFT:
			resultLength = 0;

			for (int i = length - 1; i >= 0; --i) {
				if (value[i] != padWith) {
					resultLength = i + 1;
					break;
				}
			}

			if (resultLength == 0) {
				result = emptyValue;
			} else if (resultLength == length) {
				result = value;
			} else {
				result = new byte[resultLength];
				System.arraycopy(value, 0, result, 0, resultLength);
			}

			break;
		case TRIMMED_RIGHT:
			int padLength = length;

			for (int i = 0; i < length; ++i) {
				if (value[i] != padWith) {
					padLength = i;
					break;
				}
			}

			if (padLength == 0) {
				result = value;
			} else if (padLength == length) {
				result = emptyValue;
			} else {
				resultLength = length - padLength;

				result = new byte[resultLength];
				System.arraycopy(value, padLength, result, 0, resultLength);
			}

			break;
		default: // NONE, UNTRIMMED_LEFT, UNTRIMMED_RIGHT
			result = value;
			break;
		}

		return result;
	}

	public int unpad(InputStream in, byte[] result, int off, int length) throws IOException {
		byte[] value = new byte[length];
		read(in, value, 0, length);

		int resultLength = length;

		switch (align) {
		case TRIMMED_LEFT:
			resultLength = 0;

			for (int i = length - 1; i >= 0; --i) {
				if (value[i] != padWith) {
					resultLength = i + 1;
					break;
				}
			}

			if (resultLength == 0) {
				System.arraycopy(emptyValue, 0, result, off, length);
			} else if (resultLength == length) {
				System.arraycopy(value, 0, result, off, length);
			} else {
				System.arraycopy(value, 0, result, off, resultLength);
			}

			break;
		case TRIMMED_RIGHT:
			int padLength = length;

			for (int i = 0; i < length; ++i) {
				if (value[i] != padWith) {
					padLength = i;
					break;
				}
			}

			if (padLength == 0) {
				System.arraycopy(value, 0, result, off, length);
			} else if (padLength == length) {
				System.arraycopy(emptyValue, 0, result, off, length);
			} else {
				resultLength = length - padLength;
				System.arraycopy(value, padLength, result, off, resultLength);
			}

			break;
		default: // NONE, UNTRIMMED_LEFT, UNTRIMMED_RIGHT
			System.arraycopy(value, 0, result, off, length);
			break;
		}

		return resultLength;
	}

	/**
	 * read N*2 bytes from input stream and store it to <code>value</code>
	 * starting from offset <code>off</code>.
	 *
	 * @param in
	 * @param value
	 * @param off
	 * @param vlen
	 * @throws IOException
	 */
	public void read(InputStream in, byte[] value, int off, int vlen)
			throws IOException {
		vlen <<= 1;

		byte[] bbuf = new byte[vlen];
		IOUtils.readFully(in, bbuf, vlen);

		for (int i = 0, j = off; i < vlen; i += 2, ++j) {
			value[j] = (byte) ((hex2int((char) (bbuf[i] & 0xFF)) << 4) | hex2int((char) (bbuf[i + 1] & 0xFF)));
		}
	}

	/**
	 * write N bytes of value to output stream. As the each byte of value will
	 * be written in hexadecimal form, so this method will write N*2 bytes in
	 * the stream.
	 *
	 * @param out
	 * @param value
	 * @param off
	 * @param vlen
	 * @throws IOException
	 */
	public void write(OutputStream out, byte[] value, int off, int vlen)
			throws IOException {
		for (int i = off; i < vlen; ++i) {
			out.write(HEX[(value[i] & 0xF0) >> 4]); // hi
			out.write(HEX[value[i] & 0x0F]); // lo
		}
	}
}
