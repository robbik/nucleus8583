package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.BitSet;

import org.nucleus8583.core.xml.Iso8583FieldAlignments;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public final class Iso8583BinaryFieldType extends Iso8583AbstractBinaryFieldType {
	private static final long serialVersionUID = -5615324004502124085L;

	private final int length;

	public Iso8583BinaryFieldType(Iso8583FieldDefinition def, Iso8583FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue);

		if (def.getLength() <= 0) {
			throw new IllegalArgumentException("length must be greater than zero");
		}

		this.length = def.getLength() << 1;
	}

	public void read(Reader reader, BitSet bits) throws IOException {
		super.read(reader, bits, length);
	}

	public BitSet readBinary(Reader reader) throws IOException {
		BitSet bits = new BitSet();
		super.read(reader, bits, length);

		return bits;
	}

	public void write(Writer writer, BitSet value) throws IOException {
		super.write(writer, value, length);
	}
}
