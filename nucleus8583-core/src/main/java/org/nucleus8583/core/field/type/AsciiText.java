package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.util.AsciiPadder;
import org.nucleus8583.core.util.StringUtils;
import org.nucleus8583.core.xml.FieldAlignments;
import org.nucleus8583.core.xml.FieldDefinition;

public class AsciiText extends FieldType {
	private static final long serialVersionUID = -5615324004502124085L;

	private int length;

	private AsciiPadder padder;

	public AsciiText(FieldDefinition def, FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue);

		if (def.getLength() <= 0) {
			throw new IllegalArgumentException(
					"length must be greater than zero");
		}

		length = def.getLength();

		padder = new AsciiPadder();
		padder.setLength(length);

		if (def.getAlign() == null) {
			if (defaultAlign == null) {
				throw new IllegalArgumentException("alignment required");
			}

			padder.setAlign(defaultAlign.symbolicValue());
		} else {
			padder.setAlign(def.getAlign().symbolicValue());
		}

		if (padder.getAlign() == 'n') {
			padder.setPadWith(' ');
		} else {
			if (StringUtils.isEmpty(def.getPadWith())) {
				if (StringUtils.isEmpty(defaultPadWith)) {
					throw new IllegalArgumentException("pad-with required");
				}

				padder.setPadWith(defaultPadWith.charAt(0));
			} else {
				padder.setPadWith(def.getPadWith().charAt(0));
			}
		}

		if (def.getEmptyValue() == null) {
			if (defaultEmptyValue == null) {
				padder.setEmptyValue(new char[0]);
			} else {
				padder.setEmptyValue(defaultEmptyValue.toCharArray());
			}
		} else {
			padder.setEmptyValue(def.getEmptyValue().toCharArray());
		}

		padder.initialize();
	}

	@Override
	public boolean isBinary() {
		return false;
	}

	@Override
	public String readString(InputStream in) throws IOException {
		return new String(padder.unpad(in, length));
	}

	@Override
	public void write(OutputStream out, String value) throws IOException {
		int vlen = value.length();
		if (vlen > length) {
			throw new IllegalArgumentException("value of field #" + id
					+ " is too long, expected " + length + " but actual is "
					+ vlen);
		}

		padder.pad(out, value, vlen);
	}
}
