package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.BitSet;

import org.nucleus8583.core.xml.Iso8583FieldAlignments;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public abstract class Iso8583FieldType implements Serializable {
	private static final long serialVersionUID = -1162912563090715434L;

	protected final int id;

	public Iso8583FieldType(Iso8583FieldDefinition def, Iso8583FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue) {
		this.id = def.getId();
	}

	public int getId() {
		return id;
	}

	public abstract boolean isBinary();

	public abstract void write(Writer writer, String value) throws IOException;

	public abstract void write(Writer writer, BitSet value) throws IOException;

	public abstract void read(Reader reader, BitSet bits) throws IOException;

	public abstract BitSet readBinary(Reader reader) throws IOException;

	public abstract String readString(Reader reader) throws IOException;
}
