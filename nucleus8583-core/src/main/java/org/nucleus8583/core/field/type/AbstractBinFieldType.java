package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.BitSet;

import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.util.IOUtils;
import org.nucleus8583.core.xml.Iso8583FieldAlignments;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public abstract class AbstractBinFieldType extends FieldType {
	private static final long serialVersionUID = 3977789121124596289L;

	public AbstractBinFieldType(Iso8583FieldDefinition def, Iso8583FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue);
	}

	public boolean isBinary() {
		return true;
	}

	public String readString(InputStream in, CharsetDecoder dec) throws IOException {
		throw new UnsupportedOperationException();
	}

	public void write(OutputStream out, CharsetEncoder enc, String value) throws IOException {
		throw new UnsupportedOperationException();
	}

	private void set(BitSet bits, int start, int value) {
		switch (value) {
		case 0:
			break;
		case 1:
			// 0001
			bits.set(start + 3);
			break;
		case 2:
			// 0010
			bits.set(start + 2);
			break;
		case 3:
			// 0011
			bits.set(start + 2);
			bits.set(start + 3);
			break;
		case 4:
			// 0100
			bits.set(start + 1);
			break;
		case 5:
			// 0101
			bits.set(start + 1);
			bits.set(start + 3);
			break;
		case 6:
			// 0110
			bits.set(start + 1);
			bits.set(start + 2);
			break;
		case 7:
			// 0111
			bits.set(start + 1);
			bits.set(start + 2);
			bits.set(start + 3);
			break;
		case 8:
			// 1000
			bits.set(start);
			break;
		case 9:
			// 1001
			bits.set(start);
			bits.set(start + 3);
			break;
		case 0xA:
			// 1010
			bits.set(start);
			bits.set(start + 2);
			break;
		case 0xB:
			// 1011
			bits.set(start);
			bits.set(start + 2);
			bits.set(start + 3);
			break;
		case 0xC:
			// 1100
			bits.set(start);
			bits.set(start + 1);
			break;
		case 0xD:
			// 1101
			bits.set(start);
			bits.set(start + 1);
			bits.set(start + 3);
			break;
		case 0xE:
			// 1110
			bits.set(start);
			bits.set(start + 1);
			bits.set(start + 2);
			break;
		case 0xF:
			// 1111
			bits.set(start);
			bits.set(start + 1);
			bits.set(start + 2);
			bits.set(start + 3);
			break;
		}
	}

	protected void read(InputStream in, CharsetDecoder dec, BitSet bits, int length) throws IOException {
		byte[] buf = new byte[length];
		IOUtils.readFully(in, buf, length);

		for (int i = 0, j = 0; i < length; ++i, j += 8) {
			int buf1 = buf[i] & 0xFF;

			set(bits, j, buf1 >> 4);
			set(bits, j + 4, buf1 & 0x0F);
		}
	}

	protected void write(OutputStream out, CharsetEncoder enc, BitSet value, int length) throws IOException {
		int j = 0;
		int ivalue;

		for (int i = 0; i < length; ++i) {
			ivalue = 0;

			if (value.get(j++)) {
				ivalue |= 128;
			}
			if (value.get(j++)) {
				ivalue |= 64;
			}
			if (value.get(j++)) {
				ivalue |= 32;
			}
			if (value.get(j++)) {
				ivalue |= 16;
			}
			if (value.get(j++)) {
				ivalue |= 8;
			}
			if (value.get(j++)) {
				ivalue |= 4;
			}
			if (value.get(j++)) {
				ivalue |= 2;
			}
			if (value.get(j++)) {
				ivalue |= 1;
			}

			out.write(ivalue);
		}
	}
}
