package org.nucleus8583.oim.converter;

public class StringConverter extends TypeConverter {
	@Override
	public Object convertToJavaObject(char[] value, int off, int len) {
		if (len == 0) {
			return EMPTY;
		}

		return new String(value, off, off + len);
	}

	@Override
	public Object convertToJavaObject(String value) {
		return value;
	}

	@Override
	public String convertToIsoString(Object value) {
		if (value == null) {
			return EMPTY;
		}

		return (String) value;
	}

	@Override
	public Character getDefaultAlignment() {
		return ALIGN_LEFT;
	}

	@Override
	public Character getDefaultPadWith() {
		return PAD_SPACE;
	}
}
