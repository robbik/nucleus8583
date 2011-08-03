package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.xml.FieldAlignments;
import org.nucleus8583.core.xml.FieldDefinition;

public abstract class AbstractStringFieldType extends FieldType {
	private static final long serialVersionUID = -5615324004502124085L;

	public AbstractStringFieldType(FieldDefinition def, FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue);
	}

	@Override
    public boolean isBinary() {
		return false;
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
    public void write(OutputStream out, CharsetEncoder enc, byte[] value) throws IOException {
		throw new UnsupportedOperationException();
	}

    @Override
    public void write(OutputStream out, CharsetEncoder enc, byte[] value, int off, int len) throws IOException {
        throw new UnsupportedOperationException();
    }
}
