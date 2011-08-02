package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.xml.Iso8583FieldAlignments;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public final class HexBinFieldType extends AbstractHexBinFieldType {
	private static final long serialVersionUID = -5615324004502124085L;

	private final int streamLength;

	private final int length;

    private final char[] padder;

	public HexBinFieldType(Iso8583FieldDefinition def, Iso8583FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue);

        if (def.getLength() <= 0) {
            throw new IllegalArgumentException("length must be greater than zero");
        }

        length = def.getLength() >> 1;
        streamLength = length << 1;

        padder = new char[streamLength];
        Arrays.fill(padder, '0');
	}

	@Override
    public void read(InputStream in, CharsetDecoder dec, byte[] value) throws IOException {
		super.read(in, dec, value, streamLength);
	}

	@Override
    public byte[] readBinary(InputStream in, CharsetDecoder dec) throws IOException {
		byte[] value = new byte[length];
		super.read(in, dec, value, streamLength);

		return value;
	}

	@Override
    public void write(OutputStream out, CharsetEncoder enc, byte[] value) throws IOException {
	    int vlen = value.length;
//	    if (vlen > length) {
//	        throw new IllegalArgumentException("value of field #" + id + " is too long, expected " + length + " but actual is " + vlen);
//	    }

	    // 1 binary byte = 2 hex bytes
        if (vlen == 0) {
            enc.write(out, padder, 0, streamLength);
        } else if (vlen > length) {
            super.write(out, enc, value, length);
        } else if (vlen == streamLength) {
            super.write(out, enc, value, vlen);
        } else {
            super.write(out, enc, value, vlen);
            enc.write(out, padder, 0, streamLength - (vlen << 1));
        }
	}
}
