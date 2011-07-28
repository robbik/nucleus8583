package org.nucleus8583.oim.util;

public final class StringUtils {
	private StringUtils() {
		// do nothing
	}

	public static boolean hasText(String str) {
		if (str == null) {
			return false;
		}

		return str.trim().length() > 0;
	}

	public static boolean hasText(String str, boolean trimmed) {
		if (str == null) {
			return false;
		}

		if (trimmed) {
			str = str.trim();
		}

		return str.length() > 0;
	}

	public static boolean nullOrEmpty(String str, boolean trimmed) {
		if (str == null) {
			return true;
		}

		if (trimmed) {
			str = str.trim();
		}

		return str.length() == 0;
	}

	public static String substring(String s, int start, char untilBefore) {
		int end = s.indexOf(untilBefore, start);
		if (end < 0) {
			return s.substring(start);
		}
		return s.substring(start, end);
	}

	public static String substring(String s, char startAfter) {
		int start = s.indexOf(startAfter);
		if (start < 0) {
			return "";
		}
		return s.substring(start + 1);
	}
}
