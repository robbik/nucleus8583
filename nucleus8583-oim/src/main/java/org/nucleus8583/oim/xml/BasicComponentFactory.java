package org.nucleus8583.oim.xml;

import java.util.regex.Pattern;

import org.nucleus8583.oim.component.BasicComponent;
import org.nucleus8583.oim.component.HasFieldsComponent;
import org.nucleus8583.oim.converter.TypeConverter;
import org.nucleus8583.oim.util.ElExpression;
import org.nucleus8583.oim.util.StringUtil;
import org.w3c.dom.Element;

public abstract class BasicComponentFactory {
	private static final Pattern digits = Pattern.compile("^[0-9]+$");

	public static BasicComponent parse(Iso8583MessagesFactory factory,
			HasFieldsComponent parent, Element node) {
		String s_no = node.getAttribute("no");
		String s_name = node.getAttribute("name");
		String s_type = node.getAttribute("type");
		String s_precision = node.getAttribute("precision");
		String s_align = node.getAttribute("align");
		String s_padWith = node.getAttribute("pad-with");
		String s_length = node.getAttribute("length");
		String s_generatedValue = node.getAttribute("generated-value");

		// no
		int no;

		if (StringUtil.hasText(s_no)) {
			s_no = s_no.trim();

			if (!digits.matcher(s_no).matches()) {
				throw new IllegalArgumentException("basic: no must be numeric");
			}

			no = Integer.parseInt(s_no, 10);
		} else {
			no = -1;
		}

		// name
		String name;

		if (StringUtil.hasText(s_name)) {
			name = s_name.trim();
		} else {
			name = null;
		}

		// type and precision
		TypeConverter converter;

		if (!StringUtil.hasText(s_type)) {
			throw new IllegalArgumentException("basic: type is required");
		}

		if (StringUtil.hasText(s_precision)) {
			s_precision = s_precision.trim();
			if (!digits.matcher(s_precision).matches()) {
				throw new IllegalArgumentException(
						"basic: precision must be numeric");
			}
		} else {
			s_precision = null;
		}

		if (s_precision == null) {
			s_type = s_type.trim();
		} else {
			s_type = s_type.trim() + ";precision=" + s_precision;
		}

		converter = factory.findTypeConverter(s_type);
		if (converter == null) {
			throw new IllegalArgumentException("basic: unable to found type "
					+ s_type);
		}

		// align
		char align;

		if (StringUtil.hasText(s_align)) {
			s_align = s_align.trim();

			if ("left".equalsIgnoreCase(s_align)
					|| "l".equalsIgnoreCase(s_align)) {
				align = 'l';
			} else if ("right".equalsIgnoreCase(s_align)
					|| "r".equalsIgnoreCase(s_align)) {
				align = 'r';
			} else if ("none".equalsIgnoreCase(s_align)
					|| "n".equalsIgnoreCase(s_align)) {
				align = 'n';
			} else {
				throw new IllegalArgumentException("basic: unknown alignment "
						+ s_align);
			}
		} else {
			Character def = converter.getDefaultAlignment();

			if (def != null) {
				align = def.charValue();
			} else {
				align = 'n';
			}
		}

		// pad-with
		char padWith;

		if (StringUtil.hasText(s_padWith)) {
			if (s_padWith.length() != 1) {
				throw new IllegalArgumentException(
						"basic: pad-with must be one character length, given "
								+ s_padWith.length() + " character(s) length");
			}

			padWith = s_padWith.charAt(0);
		} else {
			Character def = converter.getDefaultPadWith();

			if (def != null) {
				padWith = def.charValue();
			} else {
				padWith = ' ';
			}
		}

		// length
		int length;

		if (StringUtil.hasText(s_length)) {
			s_length = s_length.trim();
			if (!digits.matcher(s_length).matches()) {
				throw new IllegalArgumentException(
						"basic: length must be numeric");
			}

			length = Integer.parseInt(s_length, 10);
			if (length <= 0) {
				throw new IllegalArgumentException(
						"basic: length must be more than zero");
			}
		} else {
			Integer def = converter.getDefaultLength();

			if (def != null) {
				length = def.intValue();
			} else {
				throw new IllegalArgumentException(
						"basic: length must be defined");
			}
		}

		// generated-value
		ElExpression generatedValue;

		if (StringUtil.hasText(s_generatedValue, false)) {
			generatedValue = new ElExpression(s_generatedValue);
		} else {
			generatedValue = null;
		}

		// consistency check
		if ((generatedValue != null) && (name != null)) {
			throw new IllegalArgumentException(
					"basic: generated-value cannot be used together with name");
		}

		return new BasicComponent(parent, no, name, converter, align, padWith,
				length, generatedValue);
	}
}
