package org.nucleus8583.core;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.BitSet;

public final class Iso8583Binary extends Iso8583Field {
	private static final long serialVersionUID = 3977789121124596289L;

	private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public Iso8583Binary(int id, int length) {
		super(id, 0, length << 1, 'n', ' ', "");
	}

	public void pack(Writer writer, BitSet value) throws IOException {
		int bitsIndex = 0;
		int ivalue;

		for (int i = 0; i < super.length; ++i) {
			ivalue = 0;

			if (value.get(bitsIndex++)) {
				ivalue |= 8;
			}
			if (value.get(bitsIndex++)) {
				ivalue |= 4;
			}
			if (value.get(bitsIndex++)) {
				ivalue |= 2;
			}
			if (value.get(bitsIndex++)) {
				ivalue |= 1;
			}

			writer.write(HEX[ivalue]);
		}
	}

	public void pack(Writer writer, String value) throws IOException {
		throw new UnsupportedOperationException();
	}

	public void unpackBinary(Reader reader, BitSet bits) throws IOException {
		int bitsIndex = 0;
		int ichar;

		bits.clear();

		for (int i = 0; i < super.length; ++i, bitsIndex += 4) {
			ichar = reader.read();
			if (ichar < 0) {
				throw new EOFException();
			}

			switch ((char) ichar) {
			case '0':
				break;
			case '1':
				// 0001
				bits.set(bitsIndex + 3);
				break;
			case '2':
				// 0010
				bits.set(bitsIndex + 2);
				break;
			case '3':
				// 0011
				bits.set(bitsIndex + 2);
				bits.set(bitsIndex + 3);
				break;
			case '4':
				// 0100
				bits.set(bitsIndex + 1);
				break;
			case '5':
				// 0101
				bits.set(bitsIndex + 1);
				bits.set(bitsIndex + 3);
				break;
			case '6':
				// 0110
				bits.set(bitsIndex + 1);
				bits.set(bitsIndex + 2);
				break;
			case '7':
				// 0111
				bits.set(bitsIndex + 1);
				bits.set(bitsIndex + 2);
				bits.set(bitsIndex + 3);
				break;
			case '8':
				// 1000
				bits.set(bitsIndex);
				break;
			case '9':
				// 1001
				bits.set(bitsIndex);
				bits.set(bitsIndex + 3);
				break;
			case 'A':
				// 1010
				bits.set(bitsIndex);
				bits.set(bitsIndex + 2);
				break;
			case 'B':
				// 1011
				bits.set(bitsIndex);
				bits.set(bitsIndex + 2);
				bits.set(bitsIndex + 3);
				break;
			case 'C':
				// 1100
				bits.set(bitsIndex);
				bits.set(bitsIndex + 1);
				break;
			case 'D':
				// 1101
				bits.set(bitsIndex);
				bits.set(bitsIndex + 1);
				bits.set(bitsIndex + 3);
				break;
			case 'E':
				// 1110
				bits.set(bitsIndex);
				bits.set(bitsIndex + 1);
				bits.set(bitsIndex + 2);
				break;
			case 'F':
				// 1111
				bits.set(bitsIndex);
				bits.set(bitsIndex + 1);
				bits.set(bitsIndex + 2);
				bits.set(bitsIndex + 3);
				break;
			}
		}
	}

	public BitSet unpackBinary(Reader reader) throws IOException {
		int bitsIndex = 0;
		int ichar;

		BitSet bits = new BitSet();

		for (int i = 0; i < super.length; ++i, bitsIndex += 4) {
			ichar = reader.read();
			if (ichar < 0) {
				throw new EOFException();
			}

			switch ((char) ichar) {
			case '0':
				break;
			case '1':
				// 0001
				bits.set(bitsIndex + 3);
				break;
			case '2':
				// 0010
				bits.set(bitsIndex + 2);
				break;
			case '3':
				// 0011
				bits.set(bitsIndex + 2);
				bits.set(bitsIndex + 3);
				break;
			case '4':
				// 0100
				bits.set(bitsIndex + 1);
				break;
			case '5':
				// 0101
				bits.set(bitsIndex + 1);
				bits.set(bitsIndex + 3);
				break;
			case '6':
				// 0110
				bits.set(bitsIndex + 1);
				bits.set(bitsIndex + 2);
				break;
			case '7':
				// 0111
				bits.set(bitsIndex + 1);
				bits.set(bitsIndex + 2);
				bits.set(bitsIndex + 3);
				break;
			case '8':
				// 1000
				bits.set(bitsIndex);
				break;
			case '9':
				// 1001
				bits.set(bitsIndex);
				bits.set(bitsIndex + 3);
				break;
			case 'A':
				// 1010
				bits.set(bitsIndex);
				bits.set(bitsIndex + 2);
				break;
			case 'B':
				// 1011
				bits.set(bitsIndex);
				bits.set(bitsIndex + 2);
				bits.set(bitsIndex + 3);
				break;
			case 'C':
				// 1100
				bits.set(bitsIndex);
				bits.set(bitsIndex + 1);
				break;
			case 'D':
				// 1101
				bits.set(bitsIndex);
				bits.set(bitsIndex + 1);
				bits.set(bitsIndex + 3);
				break;
			case 'E':
				// 1110
				bits.set(bitsIndex);
				bits.set(bitsIndex + 1);
				bits.set(bitsIndex + 2);
				break;
			case 'F':
				// 1111
				bits.set(bitsIndex);
				bits.set(bitsIndex + 1);
				bits.set(bitsIndex + 2);
				bits.set(bitsIndex + 3);
				break;
			}
		}

		return bits;
	}

	public String unpackString(Reader packed) throws IOException {
		throw new UnsupportedOperationException();
	}
}
