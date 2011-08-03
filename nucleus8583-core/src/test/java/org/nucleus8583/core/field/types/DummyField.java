package org.nucleus8583.core.field.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.field.type.FieldType;
import org.nucleus8583.core.util.IOUtils;
import org.nucleus8583.core.xml.FieldAlignments;
import org.nucleus8583.core.xml.FieldDefinition;

public class DummyField extends FieldType {
	private static final long serialVersionUID = 3050266879047299109L;

	public DummyField(FieldDefinition def, FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue);
	}

	@Override
    public boolean isBinary() {
		return false;
	}

	@Override
    public void write(OutputStream out, CharsetEncoder enc, String value) throws IOException {
		if (value == null) {
			enc.write(out, "xx");
		} else {
			int vlen = value.length();
			switch (vlen) {
			case 0:
				enc.write(out, "xx");
				break;
			case 1:
				enc.write(out, "x");
				enc.write(out, value);
				break;
			case 2:
				enc.write(out, value);
				break;
			default:
				enc.write(out, value.substring(0, 2));
				break;
			}
		}
	}

	@Override
    public void write(OutputStream out, CharsetEncoder enc, byte[] value) throws IOException {
		throw new UnsupportedOperationException();
	}

    @Override
    public void write(OutputStream out, CharsetEncoder enc, byte[] value, int off, int len) throws IOException {
        throw new UnsupportedOperationException();
    }

	@Override
    public void read(InputStream in, CharsetDecoder dec, byte[] value) throws IOException {
		throw new UnsupportedOperationException();
	}

    @Override
    public void read(InputStream in, CharsetDecoder dec, byte[] value, int off, int len) throws IOException {
        throw new UnsupportedOperationException();
    }

	@Override
    public byte[] readBinary(InputStream in, CharsetDecoder dec) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
    public String readString(InputStream in, CharsetDecoder dec) throws IOException {
		char[] cbuf = new char[2];
		IOUtils.readFully(in, dec, cbuf, 2);

		return new String(cbuf);
	}
}
