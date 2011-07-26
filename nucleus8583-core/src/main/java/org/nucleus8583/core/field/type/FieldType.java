package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.xml.Iso8583FieldAlignments;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public abstract class FieldType implements Serializable {
	private static final long serialVersionUID = -1162912563090715434L;

	protected final int id;

	public FieldType(Iso8583FieldDefinition def, Iso8583FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue) {
		this.id = def.getId();
	}

	public int getId() {
		return id;
	}

	public abstract boolean isBinary();

	public abstract void write(OutputStream out, CharsetEncoder enc, String value) throws IOException;

	public abstract void write(OutputStream out, CharsetEncoder enc, byte[] value) throws IOException;

	public abstract void read(InputStream in, CharsetDecoder dec, byte[] value) throws IOException;

	public abstract byte[] readBinary(InputStream in, CharsetDecoder dec) throws IOException;

	public abstract String readString(InputStream in, CharsetDecoder dec) throws IOException;
}
