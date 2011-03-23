package org.nucleus8583.oim.xml;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.nucleus8583.oim.component.SkipComponent;
import org.nucleus8583.oim.util.StringUtil;
import org.w3c.dom.Element;

public abstract class SkipComponentFactory {
	private static final Pattern digits = Pattern.compile("^[0-9]+$");

	public static SkipComponent parse(Element node) {
		String sFillWith = node.getAttribute("fill-with");
		String sLength = node.getAttribute("length");

		int length;

		// length
		if (StringUtil.nullOrEmpty(sLength, true)) {
			throw new NullPointerException("length is required");
		} else {
			sLength = sLength.trim();
			if (!digits.matcher(sLength).matches()) {
				throw new IllegalArgumentException(
						"length must be numeric only");
			}

			length = Integer.parseInt(sLength, 10);
			if (length <= 0) {
				throw new IllegalArgumentException(
						"length must be more than zero");
			}
		}

		// fill-with
		if (StringUtil.nullOrEmpty(sFillWith, false)) {
			throw new IllegalArgumentException("fill-with is required");
		}
		if (sFillWith.length() != 1) {
			throw new IllegalArgumentException(
					"fill-with value must be one character only, given '"
							+ sFillWith + "'");
		}

		char[] cbuf = new char[length];
		Arrays.fill(cbuf, sFillWith.charAt(0));

		return new SkipComponent(new String(cbuf), length);
	}
}
