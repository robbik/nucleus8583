package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.util.FastInteger;
import org.nucleus8583.core.xml.FieldAlignments;
import org.nucleus8583.core.xml.FieldDefinition;

public abstract class UnicodeVarHexBinFieldType extends AbstractHexBinFieldType {
	private static final long serialVersionUID = -5615324004502124085L;

	private final int lcount;

	private final int length;

	public UnicodeVarHexBinFieldType(FieldDefinition def, FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue, int lcount, int length) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue);

		this.lcount = lcount;
		this.length = length;
	}

    @Override
    public void read(InputStream in, CharsetDecoder dec, byte[] value) throws IOException {
        int vlen = FastInteger.readUint(in, dec, lcount);
        if (vlen > 0) {
            super._read(in, dec, value, 0, vlen << 1);
        }
    }

    @Override
    public void read(InputStream in, CharsetDecoder dec, byte[] value, int off, int vlen) throws IOException {
        read(in, dec, value);
    }

    @Override
    public byte[] readBinary(InputStream in, CharsetDecoder dec) throws IOException {
        int vlen = FastInteger.readUint(in, dec, lcount);
        if (vlen == 0) {
            return new byte[0];
        }

        byte[] value = new byte[vlen];
        super._read(in, dec, value, 0, vlen << 1);

        return value;
    }

    @Override
    public void write(OutputStream out, CharsetEncoder enc, byte[] value) throws IOException {
        int vlen = value.length;
        if (vlen > length) {
            throw new IllegalArgumentException("value of field #" + id + " is too long, expected 0-" + length + " but actual is " + vlen);
        }

        FastInteger.writeUint(out, enc, vlen, lcount);
        super._write(out, enc, value, 0, vlen);
    }

    @Override
    public void write(OutputStream out, CharsetEncoder enc, byte[] value, int off, int len) throws IOException {
        if (len > length) {
            throw new IllegalArgumentException("value of field #" + id + " is too long, expected 0-" + length + " but actual is " + len);
        }

        FastInteger.writeUint(out, enc, len, lcount);
        super._write(out, enc, value, off, len);
    }
}
