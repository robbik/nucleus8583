package org.nucleus8583.core;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Arrays;
import java.util.BitSet;

public abstract class Iso8583Field implements Serializable {
	private static final long serialVersionUID = -1162912563090715434L;

	protected final int id;

	protected final int lcount;

	protected final int length;

	protected final char align;

	protected final char padWith;

	protected final char[] padder;

	protected final String emptyValue;

	protected final char[] cEmptyValue;

	public Iso8583Field(int id, int lcount, int length, char align,
			char padWith, String emptyValue) {
		this.id = id;
		this.lcount = lcount;
		this.length = length;

		this.align = align;
		this.padWith = padWith;

		this.padder = new char[length];
		Arrays.fill(this.padder, padWith);

		this.emptyValue = emptyValue;
		this.cEmptyValue = emptyValue.toCharArray();
	}

	protected void pad(Writer writer, String value, int valueLength)
			throws IOException {
		if (valueLength == 0) {
			writer.write(padder, 0, length);
		} else if (valueLength == length) {
			writer.write(value, 0, valueLength);
		} else {
			switch (align) {
			case 'l':
				writer.write(value, 0, valueLength);
				writer.write(padder, 0, length - valueLength);

				break;
			case 'r':
				writer.write(padder, 0, length - valueLength);
				writer.write(value, 0, valueLength);

				break;
			default: // 'n'
				writer.write(value, 0, valueLength);
				writer.write(padder, 0, length - valueLength);

				break;
			}
		}
	}

	protected char[] unpad(char[] value, int valueLength) {
		char[] cbuf;
		int cbufLength;

		switch (align) {
		case 'l':
			cbufLength = 0;

			for (int i = valueLength - 1; i >= 0; --i) {
				if (value[i] != padWith) {
					cbufLength = i + 1;
					break;
				}
			}

			if (cbufLength == 0) {
				cbuf = cEmptyValue;
			} else if (cbufLength == valueLength) {
				cbuf = value;
			} else {
				cbuf = new char[cbufLength];
				System.arraycopy(value, 0, cbuf, 0, cbufLength);
			}

			break;
		case 'r':
			int padLength = valueLength;

			for (int i = 0; i < valueLength; ++i) {
				if (value[i] != padWith) {
					padLength = i;
					break;
				}
			}

			if (padLength == 0) {
				cbuf = value;
			} else if (padLength == valueLength) {
				cbuf = cEmptyValue;
			} else {
				cbufLength = valueLength - padLength;

				cbuf = new char[cbufLength];
				System.arraycopy(value, padLength, cbuf, 0, cbufLength);
			}

			break;
		default: // 'n'
			cbuf = value;
			break;
		}

		return cbuf;
	}

	public int getId() {
		return id;
	}

	public int getLength() {
		return length;
	}

	protected void read(Reader reader, char[] cbuf, int length)
			throws IOException {
		int cbufWriteIndex = 0;
		int remaining = length;

		int nbread;

		while (remaining > 0) {
			nbread = reader.read(cbuf, cbufWriteIndex, remaining);
			if (nbread == -1) {
				throw new EOFException();
			}

			remaining -= nbread;
			cbufWriteIndex += nbread;
		}
	}

	public abstract void pack(Writer writer, String value) throws IOException;

	public abstract void pack(Writer writer, BitSet value) throws IOException;

	public abstract void unpackBinary(Reader reader, BitSet bits)
			throws IOException;

	public abstract BitSet unpackBinary(Reader reader) throws IOException;

	public abstract String unpackString(Reader reader) throws IOException;
}
