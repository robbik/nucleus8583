package org.nucleus8583.core.field.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    public void write(OutputStream out, String value) throws IOException {
		if (value == null) {
			out.write("xx".getBytes());
		} else {
			int vlen = value.length();
			switch (vlen) {
			case 0:
				out.write("xx".getBytes());
				break;
			case 1:
				out.write("x".getBytes());
				out.write(value.getBytes());
				break;
			case 2:
				out.write(value.getBytes());
				break;
			default:
				out.write(value.substring(0, 2).getBytes());
				break;
			}
		}
	}

	@Override
    public String readString(InputStream in) throws IOException {
		byte[] buf = new byte[2];
		IOUtils.readFully(in, buf, 2);

		return new String(buf);
	}
}
