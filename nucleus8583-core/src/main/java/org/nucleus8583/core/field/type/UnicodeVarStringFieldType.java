package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.util.FastInteger;
import org.nucleus8583.core.util.IOUtils;
import org.nucleus8583.core.xml.Iso8583FieldAlignments;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public abstract class UnicodeVarStringFieldType extends AbstractStringFieldType {
	private static final long serialVersionUID = -5615324004502124085L;

	private final int lcount;

	private final int length;

	private final String emptyValue;

	public UnicodeVarStringFieldType(Iso8583FieldDefinition def, Iso8583FieldAlignments defaultAlign,
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

	public String readString(InputStream in, CharsetDecoder dec) throws IOException {
		int vlen = FastInteger.readUint(in, dec, lcount);

		if (vlen == 0) {
			return emptyValue;
		}

		char[] cbuf = new char[vlen];
		IOUtils.readFully(in, dec, cbuf, vlen);

		return new String(cbuf);
	}

	public void write(OutputStream out, CharsetEncoder enc, String value) throws IOException {
		int vlen = value.length();
		if (vlen > length) {
			throw new IllegalArgumentException("value of field #" + id + " is too long, expected 0-" + length + " but actual is " + vlen);
		}

		FastInteger.writeUint(out, enc, vlen, lcount);
		enc.write(out, value, 0, vlen);
	}
}
