package org.nucleus8583.core.types;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.BitSet;

import org.nucleus8583.core.Iso8583Field;

public class DummyField extends Iso8583Field {
	private static final long serialVersionUID = 3050266879047299109L;

	public DummyField(int id, int lcount, int length, char align, char padWith,
			String emptyValue) {
		super(id, lcount, length, align, padWith, emptyValue);
	}

	public void pack(Writer writer, String value) throws IOException {
		if (value == null) {
			writer.write("xx");
		} else {
			int vlen = value.length();
			switch (vlen) {
			case 0:
				writer.write("xx");
				break;
			case 1:
				writer.write("x");
				writer.write(value);
				break;
			case 2:
				writer.write(value);
				break;
			default:
				writer.write(value.substring(0, 2));
				break;
			}
		}
	}

	public void pack(Writer writer, BitSet value) throws IOException {
		throw new UnsupportedOperationException();
	}

	public void unpackBinary(Reader reader, BitSet bits) throws IOException {
		throw new UnsupportedOperationException();
	}

	public BitSet unpackBinary(Reader reader) throws IOException {
		throw new UnsupportedOperationException();
	}

	public String unpackString(Reader reader) throws IOException {
		char[] cbuf = new char[2];
		read(reader, cbuf, 2);

		return new String(cbuf);
	}

	public void pack(Writer writer, BitSet value, int length)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	public void unpackBinary(Reader reader, BitSet bits, int off, int length)
			throws IOException {
		throw new UnsupportedOperationException();
	}
}
