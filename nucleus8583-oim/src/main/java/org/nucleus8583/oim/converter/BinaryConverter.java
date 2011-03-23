package org.nucleus8583.oim.converter;

import java.util.BitSet;

public final class BinaryConverter extends TypeConverter {

	@Override
	public BitSet convertToIsoBinary(Object value) {
		return (BitSet) value;
	}

	@Override
	public Object convertToJavaObject(BitSet value) {
		return value;
	}

	@Override
	public Character getDefaultAlignment() {
		return ALIGN_NONE;
	}

	@Override
	public Character getDefaultPadWith() {
		return PAD_SPACE;
	}
}
