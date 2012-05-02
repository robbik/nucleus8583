package rk.commons.util;

import java.util.UUID;

public abstract class StringUtils {
	
	public static final String[] EMPTY_STRING_ARRAY = new String[0];

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
}
