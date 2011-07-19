package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.nucleus8583.core.util.FastInteger;
import org.nucleus8583.core.util.ReaderUtils;
import org.nucleus8583.core.xml.Iso8583FieldAlignments;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public abstract class Iso8583UnicodeVarStringFieldType extends Iso8583AbstractStringFieldType {
	private static final long serialVersionUID = -5615324004502124085L;

	private final int lcount;

	private final int length;

	private final String emptyValue;

	public Iso8583UnicodeVarStringFieldType(Iso8583FieldDefinition def, Iso8583FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue, int lcount, int length) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue);

		this.lcount = lcount;
		this.length = length;

		if (def.getEmptyValue() == null) {
			if (defaultEmptyValue == null) {
				this.emptyValue = "";
			} else {
				this.emptyValue = defaultEmptyValue;
			}
		} else {
			this.emptyValue = def.getEmptyValue();
		}
	}

	public String readString(Reader reader) throws IOException {
		int vlen = FastInteger.readUint(reader, lcount);

		if (vlen == 0) {
			return emptyValue;
		}

		char[] cbuf = new char[vlen];
		ReaderUtils.readFully(reader, cbuf, vlen);

		return new String(cbuf);
	}

	public void write(Writer writer, String value) throws IOException {
		int vlen = value.length();
		if (vlen > length) {
			throw new IllegalArgumentException("value of field #" + id + " is too long, expected 0-" + length + " but actual is " + vlen);
		}

		FastInteger.writeUint(writer, vlen, lcount);
		writer.write(value, 0, vlen);
	}
}
