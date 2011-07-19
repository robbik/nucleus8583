package org.nucleus8583.core.util;

import java.io.IOException;
import java.io.Writer;

public abstract class StringUtils {

	public static boolean isNull(String value) {
		return value == null;
	}

	public static boolean isEmpty(String value, boolean trimmed) {
		if (value == null) {
			return true;
		}

		if (trimmed) {
			value = value.trim();
		}

		return value.length() == 0;
	}

	public static String defaultIfNull(String value, String defaultValue) {
		if (value == null) {
			return defaultValue;
		}

		return value;
	}

	public static String defaultIfEmpty(String value, boolean trimmed, String defaultValue) {
		if (isEmpty(value, trimmed)) {
			return defaultValue;
		}

		return value;
	}

	public static void pad(Writer writer, String value, int valueLength, int expectedLength, char align, char[] padder) throws IOException {
		if (valueLength == 0) {
			writer.write(padder, 0, expectedLength);
		} else if (valueLength == expectedLength) {
			writer.write(value, 0, valueLength);
		} else {
			switch (align) {
			case 'l':
				writer.write(value, 0, valueLength);
				writer.write(padder, 0, expectedLength - valueLength);

				break;
			case 'r':
				writer.write(padder, 0, expectedLength - valueLength);
				writer.write(value, 0, valueLength);

				break;
			default: // 'n'
				writer.write(value, 0, valueLength);
				writer.write(padder, 0, expectedLength - valueLength);

				break;
			}
		}
	}

	public static char[] unpad(char[] value, int valueLength, char align, char padder, char[] defaultValue) {
		char[] cbuf;
		int cbufLength;

		switch (align) {
		case 'l':
			cbufLength = 0;

			for (int i = valueLength - 1; i >= 0; --i) {
				if (value[i] != padder) {
					cbufLength = i + 1;
					break;
				}
			}

			if (cbufLength == 0) {
				cbuf = defaultValue;
			} else if (cbufLength == valueLength) {
				cbuf = value;
			} else {
				cbuf = new char[cbufLength];
				System.arraycopy(value, 0, cbuf, 0, cbufLength);
			}

			break;
		case 'r':
			int padLength = valueLength;

			for (int i = 0; i < valueLength; ++i) {
				if (value[i] != padder) {
					padLength = i;
					break;
				}
			}

			if (padLength == 0) {
				cbuf = value;
			} else if (padLength == valueLength) {
				cbuf = defaultValue;
			} else {
				cbufLength = valueLength - padLength;

				cbuf = new char[cbufLength];
				System.arraycopy(value, padLength, cbuf, 0, cbufLength);
			}

			break;
		default: // 'n'
			cbuf = value;
			break;
		}

		return cbuf;
	}
}
