package org.nucleus8583.core;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.BitSet;

import org.nucleus8583.core.util.FastInteger;

public final class Iso8583String extends Iso8583Field {
	private static final long serialVersionUID = -5615324004502124085L;

	public Iso8583String(int id, int lcount, int length, char align,
			char padWith, String emptyValue) {
		super(id, lcount, length, align, padWith, emptyValue);
	}

	public void pack(Writer writer, BitSet value) throws IOException {
		throw new UnsupportedOperationException();
	}
	
	public void pack(Writer writer, BitSet value, int length) throws IOException {
		throw new UnsupportedOperationException();
	}

	public void pack(Writer writer, String value) throws IOException {
		int vlen = value.length();
		if (vlen > length) {
			throw new IllegalArgumentException("value of field #" + id + " is too long, expected "
					+ length + " but actual is " + vlen);
		}

		if (lcount > 0) {
			FastInteger.writeUint(writer, vlen, lcount);
			writer.write(value, 0, vlen);
		} else {
			pad(writer, value, vlen);
		}
	}

	public void unpackBinary(Reader reader, BitSet bits) throws IOException {
		throw new UnsupportedOperationException();
	}
	
	public void unpackBinary(Reader reader, BitSet bits, int off, int length) throws IOException {
		throw new UnsupportedOperationException();
	}

	public BitSet unpackBinary(Reader reader) throws IOException {
		throw new UnsupportedOperationException();
	}

	public String unpackString(Reader reader) throws IOException {
		int unpackedLength;

		if (lcount > 0) {
			unpackedLength = FastInteger.readUint(reader, lcount);
		} else {
			unpackedLength = length;
		}

		char[] cbuf = new char[unpackedLength];
		read(reader, cbuf, unpackedLength);

		return lcount > 0 ? new String(cbuf) : new String(unpad(cbuf,
				unpackedLength));
	}
}
