package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.xml.FieldAlignments;
import org.nucleus8583.core.xml.FieldDefinition;

public abstract class AbstractBinFieldType extends FieldType {
	private static final long serialVersionUID = 3977789121124596289L;

	public AbstractBinFieldType(FieldDefinition def, FieldAlignments defaultAlign,
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
}
