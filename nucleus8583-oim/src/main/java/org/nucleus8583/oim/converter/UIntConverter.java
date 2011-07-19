package org.nucleus8583.oim.converter;

import org.nucleus8583.util.FastInteger;

public class IntegerConverter extends TypeConverter {
	private static final Integer ZERO = Integer.valueOf(0);

	@Override
	public Object convertToJavaObject(char[] value, int off, int len) {
		if (len == 0) {
			return ZERO;
		}

		return FastInteger.parseUint(value, off, len);
	}

	@Override
	public Object convertToJavaObject(String value) {
		int vlen = value.length();
		if (vlen == 0) {
			return ZERO;
		}

		return FastInteger.parseUint(value, vlen);
	}

	@Override
	public String convertToIsoString(Object value) {
		if (value == null) {
			return EMPTY;
		}

		return ((Integer) value).toString();
	}

	@Override
	public Character getDefaultAlignment() {
		return ALIGN_RIGHT;
	}

	@Override
	public Character getDefaultPadWith() {
		return PAD_ZERO;
	}
}
