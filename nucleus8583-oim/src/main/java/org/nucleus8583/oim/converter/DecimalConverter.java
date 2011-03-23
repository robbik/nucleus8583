package org.nucleus8583.oim.converter;

import java.math.BigDecimal;

public class DecimalConverter extends TypeConverter {
	private final int precision;

	public DecimalConverter(int precision) {
		this.precision = precision;
	}

	@Override
	public Object convertToJavaObject(char[] value, int off, int len) {
		if (len == 0) {
			return BigDecimal.ZERO;
		}

		BigDecimal result = new BigDecimal(new String(value, off, len));

		if (precision > 0) {
			return result.movePointLeft(precision);
		}

		return result;
	}

	@Override
	public Object convertToJavaObject(String value) {
		if (value.length() == 0) {
			return BigDecimal.ZERO;
		}

		BigDecimal result = new BigDecimal(value);

		if (precision > 0) {
			return result.movePointLeft(precision);
		}

		return result;
	}

	@Override
	public String convertToIsoString(Object value) {
		if (value == null) {
			return EMPTY;
		}

		if (precision > 0) {
			return ((BigDecimal) value).movePointRight(precision)
					.toPlainString();
		}

		return ((BigDecimal) value).toPlainString();
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
