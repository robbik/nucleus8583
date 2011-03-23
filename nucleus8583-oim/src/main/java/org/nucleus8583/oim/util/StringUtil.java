package org.nucleus8583.oim.util;

public final class StringUtil {
	private StringUtil() {
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
}
