package org.nucleus8583.oim.converter;

import java.util.BitSet;

public abstract class TypeConverter {
	protected static final Character ALIGN_LEFT = Character.valueOf('l');

	protected static final Character ALIGN_RIGHT = Character.valueOf('r');

	protected static final Character ALIGN_NONE = Character.valueOf('l');

	protected static final Character PAD_SPACE = Character.valueOf(' ');

	protected static final Character PAD_ZERO = Character.valueOf('0');

	protected static final String EMPTY = "".intern();

	protected String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String convertToIsoString(Object value) {
		throw new UnsupportedOperationException();
	}

	public BitSet convertToIsoBinary(Object value) {
		throw new UnsupportedOperationException();
	}

	public Object convertToJavaObject(char[] value, int off, int len) {
		return convertToJavaObject(new String(value, off, len));
	}

	public Object convertToJavaObject(String value) {
		throw new UnsupportedOperationException();
	}

	public Object convertToJavaObject(BitSet value) {
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
