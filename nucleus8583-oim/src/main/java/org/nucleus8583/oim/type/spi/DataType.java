package org.nucleus8583.oim.type.spi;

import java.util.BitSet;

public abstract class DataType {
	protected static final Character ALIGN_LEFT = Character.valueOf('l');

	protected static final Character ALIGN_RIGHT = Character.valueOf('r');

	protected static final Character ALIGN_NONE = Character.valueOf('l');

	protected static final Character PAD_SPACE = Character.valueOf(' ');

	protected static final Character PAD_ZERO = Character.valueOf('0');

	protected static final String EMPTY = "".intern();

	public String writeObjectAsString(Object value) {
		throw new UnsupportedOperationException();
	}

	public BitSet writeObjectAsBinary(Object value) {
		throw new UnsupportedOperationException();
	}

	public Object readObject(char[] value, int off, int len) {
		return readObject(new String(value, off, len));
	}

	public Object readObject(String value) {
		throw new UnsupportedOperationException();
	}

	public Object readObject(BitSet value) {
		throw new UnsupportedOperationException();
	}

	public Character getDefaultAlignment() {
		return null;
	}

	public Character getDefaultPadWith() {
		return null;
	}

	public Integer getDefaultLength() {
		return null;
	}
}
