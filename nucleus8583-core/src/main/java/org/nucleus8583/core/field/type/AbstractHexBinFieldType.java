package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.util.IOUtils;
import org.nucleus8583.core.xml.Iso8583FieldAlignments;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public abstract class AbstractHexBinFieldType extends FieldType {
	private static final long serialVersionUID = 3977789121124596289L;

	private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static int hex2int(char ichar) {
        switch (ichar) {
        case '0':
            break;
        case '1':
            // 0001
            return 1;
        case '2':
            // 0010
            return 2;
        case '3':
            // 0011
            return 3;
        case '4':
            // 0100
            return 4;
        case '5':
            // 0101
            return 5;
        case '6':
            // 0110
            return 6;
        case '7':
            // 0111
            return 7;
        case '8':
            // 1000
            return 8;
        case '9':
            // 1001
            return 9;
        case 'A':
            // 1010
            return 10;
        case 'B':
            // 1011
            return 11;
        case 'C':
            // 1100
            return 12;
        case 'D':
            // 1101
            return 13;
        case 'E':
            // 1110
            return 14;
        case 'F':
            // 1111
            return 15;
        }

        return 0;
	}

	public AbstractHexBinFieldType(Iso8583FieldDefinition def, Iso8583FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue);
	}

	@Override
    public boolean isBinary() {
		return true;
	}

	@Override
    public String readString(InputStream in, CharsetDecoder dec) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
    public void write(OutputStream out, CharsetEncoder enc, String value) throws IOException {
		throw new UnsupportedOperationException();
	}

	protected void read(InputStream in, CharsetDecoder dec, byte[] value, int streamLength) throws IOException {
        char[] cbuf = new char[streamLength];
        IOUtils.readFully(in, dec, cbuf, streamLength);

		for (int i = 0, j = 0; i < streamLength; i += 2, ++j) {
            value[j] = (byte) ((hex2int(cbuf[i]) << 4) | hex2int(cbuf[i + 1]));
		}
	}

	protected void write(OutputStream out, CharsetEncoder enc, byte[] value, int valueLength) throws IOException {
        for (int i = 0; i < valueLength; ++i) {
            enc.write(out, HEX[(value[i] & 0xF0) >> 4]); // hi
            enc.write(out, HEX[value[i] & 0x0F]); // lo
        }
	}
}
