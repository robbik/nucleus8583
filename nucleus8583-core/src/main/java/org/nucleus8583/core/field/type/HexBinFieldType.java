package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.xml.FieldAlignments;
import org.nucleus8583.core.xml.FieldDefinition;

public final class HexBinFieldType extends AbstractHexBinFieldType {
	private static final long serialVersionUID = -5615324004502124085L;

	private final int streamLength;

	private final int length;

    private final char[] padder;

	public HexBinFieldType(FieldDefinition def, FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue);

        if (def.getLength() <= 0) {
            throw new IllegalArgumentException("length must be greater than zero");
        }

        length = def.getLength();
        streamLength = length << 1;

        padder = new char[streamLength];
        Arrays.fill(padder, '0');
	}

	@Override
    public void read(InputStream in, CharsetDecoder dec, byte[] value) throws IOException {
		super._read(in, dec, value, 0, streamLength);
	}

    @Override
    public void read(InputStream in, CharsetDecoder dec, byte[] value, int off, int len) throws IOException {
        super._read(in, dec, value, off, len << 1);
    }

	@Override
    public byte[] readBinary(InputStream in, CharsetDecoder dec) throws IOException {
		byte[] value = new byte[length];
		super._read(in, dec, value, 0, streamLength);

		return value;
	}

    @Override
    public void write(OutputStream out, CharsetEncoder enc, byte[] value) throws IOException {
        int vlen = value.length;
        if (vlen > length) {
            throw new IllegalArgumentException("value of field #" + id + " is too long, expected " + length + " but actual is " + vlen);
        }

        // 1 binary byte = 2 hex bytes
        if (vlen == 0) {
            enc.write(out, padder, 0, streamLength);
        } else if (vlen == length) {
            super._write(out, enc, value, 0, vlen);
        } else {
            super._write(out, enc, value, 0, vlen);
            enc.write(out, padder, 0, streamLength - (vlen << 1));
        }
    }

    @Override
    public void write(OutputStream out, CharsetEncoder enc, byte[] value, int off, int vlen) throws IOException {
        super._write(out, enc, value, off, vlen);
	}
}
