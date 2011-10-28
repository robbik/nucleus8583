package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.util.AsciiPrefixer;
import org.nucleus8583.core.util.Base16Padder;
import org.nucleus8583.core.xml.FieldAlignments;
import org.nucleus8583.core.xml.FieldDefinition;

public abstract class AsciiPrefixedBase16Binary extends FieldType {
	private static final long serialVersionUID = -5615324004502124085L;

	private AsciiPrefixer prefixer;

	private Base16Padder padder;

	private int maxLength;

	private byte[] emptyValue;

	public AsciiPrefixedBase16Binary(FieldDefinition def,
			FieldAlignments defaultAlign, String defaultPadWith,
			String defaultEmptyValue, int prefixLength, int maxLength) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue);

		this.maxLength = maxLength >> 1;

		if (def.getEmptyValue() == null) {
			if (defaultEmptyValue == null) {
				setEmptyValue("");
			} else {
				setEmptyValue(defaultEmptyValue);
			}
		} else {
			setEmptyValue(def.getEmptyValue());
		}

		prefixer = new AsciiPrefixer(prefixLength);
		padder = new Base16Padder();
	}

	public void setEmptyValue(String emptyValue) {
		int len = emptyValue.length();
		if ((len % 2) != 0) {
			emptyValue = "0" + emptyValue;
		}

		this.emptyValue = new byte[emptyValue.length() >> 1];
		for (int i = 0, j = 0; i < this.emptyValue.length; ++i, j += 2) {
			this.emptyValue[i] = (byte) ((Base16Padder.hex2int(emptyValue
					.charAt(j)) << 4) | Base16Padder.hex2int(emptyValue
					.charAt(j + 1)));
		}
	}

	@Override
	public boolean isBinary() {
		return true;
	}

	@Override
	public byte[] readBinary(InputStream in) throws IOException {
		int vlen = (prefixer.readUint(in) + 1) >> 1;
		if (vlen == 0) {
			return emptyValue;
		}

		byte[] buf = new byte[vlen];
		padder.read(in, buf, 0, vlen);

		return buf;
	}

	@Override
	public void read(InputStream in, byte[] value, int off, int len)
			throws IOException {
		super.read(in, value, off, len);
	}

	@Override
	public void write(OutputStream out, byte[] value) throws IOException {
		int vlen = value.length;
		if (vlen > maxLength) {
			throw new IllegalArgumentException("value of field #" + id
					+ " is too long, expected 0-" + maxLength
					+ " but actual is " + vlen);
		}

		prefixer.writeUint(out, vlen << 1);
		padder.write(out, value, 0, vlen);
	}

	@Override
	public void write(OutputStream out, byte[] value, int off, int len)
			throws IOException {
		if (len > maxLength) {
			throw new IllegalArgumentException("value of field #" + id
					+ " is too long, expected 0-" + maxLength
					+ " but actual is " + len);
		}

		prefixer.writeUint(out, len << 1);
		padder.write(out, value, off, len);
	}
}
