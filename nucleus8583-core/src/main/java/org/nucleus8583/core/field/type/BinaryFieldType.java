package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.BitSet;

import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.xml.Iso8583FieldAlignments;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public final class BinaryFieldType extends AbstractHexBinFieldType {
	private static final long serialVersionUID = -5615324004502124085L;

	private final int length;

	public BinaryFieldType(Iso8583FieldDefinition def, Iso8583FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue);

		if (def.getLength() <= 0) {
			throw new IllegalArgumentException("length must be greater than zero");
		}

		this.length = def.getLength() << 1;
	}

	public void read(InputStream in, CharsetDecoder dec, BitSet bits) throws IOException {
		super.read(in, dec, bits, length);
	}

	public BitSet readBinary(InputStream in, CharsetDecoder dec) throws IOException {
		BitSet bits = new BitSet();
		super.read(in, dec, bits, length);

		return bits;
	}

	public void write(OutputStream out, CharsetEncoder enc, BitSet value) throws IOException {
		super.write(out, enc, value, length);
	}
}
