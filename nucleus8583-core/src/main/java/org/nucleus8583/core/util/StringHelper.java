package org.nucleus8583.core.util;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

public final class StringHelper {
	
	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	private StringHelper() {
		// do nothing
	}
	
	public static boolean hasText(String s) {
		if (s == null) {
			return false;
		}

		s = s.trim();
		return !s.isEmpty();
	}

	public static boolean hasText(String s, boolean trim) {
		if (s == null) {
			return false;
		}

		if (trim) {
			s = s.trim();
		}

		return !s.isEmpty();
	}

	public static String valueOf(UUID uuid) {
		return uuid.toString().replace("-", "");
	}
	
	public static String newUTF8(byte[] bytes) {
		return new String(bytes, StandardCharsets.UTF_8);
	}
	
	public static String newUTF8(byte[] bytes, int offset, int length) {
		return new String(bytes, offset, length, StandardCharsets.UTF_8);
	}
	
	public static String escapeJava(String str) {
		return StringEscapeUtils.escapeJava(str);
	}
	
	public static String unescapeJava(String str) {
		return StringEscapeUtils.unescapeJava(str);
	}

	public static int asInt(String s, int defaultValue) {
		if ((s == null) || s.isEmpty()) {
			return defaultValue;
		}

		try {
			return Integer.parseInt(s.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static boolean ifHasText(String value, Consumer<? super String> dst) {
		if (value == null) {
			return false;
		} else {
			dst.accept(value);
			return true;
		}
	}

	public static boolean ifInt(String value, IntConsumer dst) {
		if ((value == null) || value.isEmpty()) {
			return false;
		}

		try {
			dst.accept(Integer.parseInt(value.trim()));
		} catch (Exception e) {
			return false;
		}

		return true;
	}
}
