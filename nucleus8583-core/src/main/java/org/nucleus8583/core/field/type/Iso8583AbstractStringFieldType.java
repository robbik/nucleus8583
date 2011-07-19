package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.BitSet;

import org.nucleus8583.core.xml.Iso8583FieldAlignments;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public abstract class Iso8583AbstractStringFieldType extends Iso8583FieldType {
	private static final long serialVersionUID = -5615324004502124085L;

	public Iso8583AbstractStringFieldType(Iso8583FieldDefinition def, Iso8583FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue);
	}

	public boolean isBinary() {
		return false;
	}

	public void read(Reader reader, BitSet bits) throws IOException {
		throw new UnsupportedOperationException();
	}

	public BitSet readBinary(Reader reader) throws IOException {
		throw new UnsupportedOperationException();
	}

	public void write(Writer writer, BitSet value) throws IOException {
		throw new UnsupportedOperationException();
	}
}
