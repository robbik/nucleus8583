package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.util.IOUtils;
import org.nucleus8583.core.util.StringUtils;
import org.nucleus8583.core.xml.FieldAlignments;
import org.nucleus8583.core.xml.FieldDefinition;

public final class StringFieldType extends AbstractStringFieldType {
	private static final long serialVersionUID = -5615324004502124085L;

	private final int length;

	private final char align;

	private final char padWith;

	private final char[] padder;

	private final char[] emptyValue;

	public StringFieldType(FieldDefinition def, FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue);

		if (def.getLength() <= 0) {
			throw new IllegalArgumentException("length must be greater than zero");
		}

		this.length = def.getLength();

		if (def.getAlign() == null) {
			if (defaultAlign == null) {
				throw new IllegalArgumentException("alignment required");
			}

			this.align = defaultAlign.symbolicValue();
		} else {
			this.align = def.getAlign().symbolicValue();
		}

		if (this.align == 'n') {
			this.padWith = ' ';
		} else {
			if (StringUtils.isEmpty(def.getPadWith())) {
				if (StringUtils.isEmpty(defaultPadWith)) {
					throw new IllegalArgumentException("pad-with required");
				}

				this.padWith = defaultPadWith.charAt(0);
			} else {
				this.padWith = def.getPadWith().charAt(0);
			}
		}

		this.padder = new char[this.length];

		Arrays.fill(this.padder, this.padWith);

		if (def.getEmptyValue() == null) {
			if (defaultEmptyValue == null) {
				this.emptyValue = new char[0];
			} else {
				this.emptyValue = defaultEmptyValue.toCharArray();
			}
		} else {
			this.emptyValue = def.getEmptyValue().toCharArray();
		}
	}

	public String readString(InputStream in, CharsetDecoder dec) throws IOException {
		char[] cbuf = new char[length];
		IOUtils.readFully(in, dec, cbuf, length);

		return new String(StringUtils.unpad(cbuf, length, align, padWith, emptyValue));
	}

	public void write(OutputStream out, CharsetEncoder enc, String value) throws IOException {
		int vlen = value.length();
		if (vlen > length) {
			throw new IllegalArgumentException("value of field #" + id + " is too long, expected " + length + " but actual is " + vlen);
		}

		StringUtils.pad(out, enc, value, vlen, length, align, padder);
	}
}
