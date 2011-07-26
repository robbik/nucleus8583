package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.util.FastInteger;
import org.nucleus8583.core.xml.Iso8583FieldAlignments;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public abstract class UnicodeVarHexBinFieldType extends AbstractHexBinFieldType {
	private static final long serialVersionUID = -5615324004502124085L;

	private final int lcount;

	private final int length;

	public UnicodeVarHexBinFieldType(Iso8583FieldDefinition def, Iso8583FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue, int lcount, int length) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue);

		this.lcount = lcount;
		this.length = length;
	}

    @Override
    public void read(InputStream in, CharsetDecoder dec, byte[] value) throws IOException {
        int vlen = FastInteger.readUint(in, dec, lcount);
        if (vlen > 0) {
            super.read(in, dec, value, vlen << 1);
        }
    }

    @Override
    public byte[] readBinary(InputStream in, CharsetDecoder dec) throws IOException {
        int vlen = FastInteger.readUint(in, dec, lcount);
        if (vlen == 0) {
            return new byte[0];
        }

        byte[] value = new byte[vlen];
        super.read(in, dec, value, vlen << 1);

        return value;
    }

    @Override
    public void write(OutputStream out, CharsetEncoder enc, byte[] value) throws IOException {
        int vlen = value.length;
        if (vlen > length) {
            throw new IllegalArgumentException("value of field #" + id + " is too long, expected 0-" + length + " but actual is " + vlen);
        }

        FastInteger.writeUint(out, enc, vlen, lcount);
        super.write(out, enc, value, vlen);
    }
}
