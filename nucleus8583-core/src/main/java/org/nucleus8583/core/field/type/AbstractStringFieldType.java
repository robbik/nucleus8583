package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.BitSet;

import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.xml.Iso8583FieldAlignments;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public abstract class AbstractStringFieldType extends FieldType {
	private static final long serialVersionUID = -5615324004502124085L;

	public AbstractStringFieldType(Iso8583FieldDefinition def, Iso8583FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue);
	}

	public boolean isBinary() {
		return false;
	}

	public void read(InputStream in, CharsetDecoder dec, BitSet bits) throws IOException {
		throw new UnsupportedOperationException();
	}

	public BitSet readBinary(InputStream in, CharsetDecoder dec) throws IOException {
		throw new UnsupportedOperationException();
	}

	public void write(OutputStream out, CharsetEncoder enc, BitSet value) throws IOException {
		throw new UnsupportedOperationException();
	}
}
