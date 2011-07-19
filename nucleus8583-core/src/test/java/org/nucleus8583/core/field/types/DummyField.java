package org.nucleus8583.core.field.types;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.BitSet;

import org.nucleus8583.core.field.type.Iso8583FieldType;
import org.nucleus8583.core.util.ReaderUtils;
import org.nucleus8583.core.xml.Iso8583FieldAlignments;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public class DummyField extends Iso8583FieldType {
	private static final long serialVersionUID = 3050266879047299109L;

	public DummyField(Iso8583FieldDefinition def, Iso8583FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue);
	}

	public boolean isBinary() {
		return false;
	}

	public void write(Writer writer, String value) throws IOException {
		if (value == null) {
			writer.write("xx");
		} else {
			int vlen = value.length();
			switch (vlen) {
			case 0:
				writer.write("xx");
				break;
			case 1:
				writer.write("x");
				writer.write(value);
				break;
			case 2:
				writer.write(value);
				break;
			default:
				writer.write(value.substring(0, 2));
				break;
			}
		}
	}

	public void write(Writer writer, BitSet value) throws IOException {
		throw new UnsupportedOperationException();
	}

	public void read(Reader reader, BitSet bits) throws IOException {
		throw new UnsupportedOperationException();
	}

	public BitSet readBinary(Reader reader) throws IOException {
		throw new UnsupportedOperationException();
	}

	public String readString(Reader reader) throws IOException {
		char[] cbuf = new char[2];
		ReaderUtils.readFully(reader, cbuf, 2);

		return new String(cbuf);
	}
}
