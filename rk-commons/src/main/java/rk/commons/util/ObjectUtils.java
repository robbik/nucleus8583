package rk.commons.util;

import java.util.Arrays;

public abstract class ObjectUtils {

	public static boolean isArray(Object a) {
		return a.getClass().getName().startsWith("[");
	}

	public static boolean equals(byte[] a, byte[] b) {
		if (a == b) {
			return true;
		}

		if ((a == null) || (b == null)) {
			return false;
		}

		return Arrays.equals(a, b);
	}

	public static boolean equals(Object a, Object b) {
		if (a == b) {
			return true;
		}

		if ((a == null) || (b == null)) {
			return false;
		}

		return a.equals(b);
	}
	
	public static Class<?> getPrimitiveType(Class<?> type) {
		if (Integer.class.equals(type)) {
			return int.class;
		} else if (Long.class.equals(type)) {
			return long.class;
		} else if (Short.class.equals(type)) {
			return short.class;
		} else if (Byte.class.equals(type)) {
			return byte.class;
		} else if (Float.class.equals(type)) {
			return float.class;
		} else if (Double.class.equals(type)) {
			return double.class;
		} else if (Void.class.equals(type)) {
			return void.class;
		} else {
			return null;
		}
	}
}
