package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;

import org.nucleus8583.core.util.ReaderUtils;
import org.nucleus8583.core.util.StringUtils;
import org.nucleus8583.core.xml.Iso8583FieldAlignments;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public final class Iso8583StringFieldType extends Iso8583AbstractStringFieldType {
	private static final long serialVersionUID = -5615324004502124085L;

	private final int length;

	private final char align;

	private final char padWith;

	private final char[] padder;

	private final char[] emptyValue;

	public Iso8583StringFieldType(Iso8583FieldDefinition def, Iso8583FieldAlignments defaultAlign,
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
			if (StringUtils.isEmpty(def.getPadWith(), false)) {
				if (StringUtils.isEmpty(defaultPadWith, false)) {
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

	public String readString(Reader reader) throws IOException {
		char[] cbuf = new char[length];
		ReaderUtils.readFully(reader, cbuf, length);

		return new String(StringUtils.unpad(cbuf, length, align, padWith, emptyValue));
	}

	public void write(Writer writer, String value) throws IOException {
		int vlen = value.length();
		if (vlen > length) {
			throw new IllegalArgumentException("value of field #" + id + " is too long, expected " + length + " but actual is " + vlen);
		}

		StringUtils.pad(writer, value, vlen, length, align, padder);
	}
}
