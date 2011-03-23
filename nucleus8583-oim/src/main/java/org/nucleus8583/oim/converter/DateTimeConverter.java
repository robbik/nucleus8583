package org.nucleus8583.oim.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeConverter extends TypeConverter {
	private final String pattern;

	private final Integer length;

	private final ThreadLocal<SimpleDateFormat> formatter;

	public DateTimeConverter(String pattern) {
		this.pattern = pattern;
		this.length = Integer.valueOf(pattern.length());

		this.formatter = new ThreadLocal<SimpleDateFormat>();
	}

	private String format(Date value) {
		SimpleDateFormat fmt = formatter.get();
		if (fmt == null) {
			fmt = new SimpleDateFormat(pattern);
			formatter.set(fmt);
		}

		return fmt.format(value);
	}

	private Date parse(String value) throws ParseException {
		SimpleDateFormat fmt = formatter.get();
		if (fmt == null) {
			fmt = new SimpleDateFormat(pattern);
			formatter.set(fmt);
		}

		return fmt.parse(value);
	}

	@Override
	public Object convertToJavaObject(char[] value, int off, int len) {
		if (len == 0) {
			return null;
		}

		try {
			return parse(new String(value, off, off + len));
		} catch (ParseException ex) {
			throw new RuntimeException("unable to parse date/time string "
					+ new String(value, off, off + len) + ", pattern used is "
					+ pattern);
		}
	}

	@Override
	public Object convertToJavaObject(String value) {
		if (value.length() == 0) {
			return null;
		}

		try {
			return parse(value);
		} catch (ParseException ex) {
			throw new RuntimeException("unable to parse date/time string "
					+ value + ", pattern used is " + pattern);
		}
	}

	@Override
	public String convertToIsoString(Object value) {
		if (value == null) {
			return EMPTY;
		}

		return format((Date) value);
	}

	@Override
	public Character getDefaultAlignment() {
		return ALIGN_LEFT;
	}

	@Override
	public Integer getDefaultLength() {
		return length;
	}

	@Override
	public Character getDefaultPadWith() {
		return PAD_SPACE;
	}
}
