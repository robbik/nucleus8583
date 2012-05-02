package org.nucleus8583.core.field.spi;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.field.Type;
import org.nucleus8583.core.xml.Alignment;

public class AsciiAmount extends AsciiText {
	private static final long serialVersionUID = -5615324004502124085L;

	public AsciiAmount() {
		super();
		
		padder.setAlign(Alignment.TRIMMED_RIGHT);
		padder.setPadWith('0');
		
		padder.setEmptyValue(new char[] { '0' });
	}
	
	public AsciiAmount(AsciiAmount o) {
		super(o);
	}
	
	public void setLength(int length) {
		if (length <= 0) {
			throw new IllegalArgumentException("length must greater than zero");
		}

		padder.setLength(length - 1);

		this.length = length;
	}

	@Override
	public String read(InputStream in) throws IOException {
        int first = in.read();
        if (first < 0) {
            throw new EOFException();
        }
        
        if (length > 1) {
        	return ((char) first) + new String(padder.unpad(in, length - 1));
        } else {
        	return String.valueOf((char) first);
        }
	}

	@Override
	public void write(OutputStream out, String value) throws IOException {
		int vlen = value.length();
		if (vlen > length) {
			throw new IllegalArgumentException("value too long, expected " + length + " but actual is " + vlen);
		}

		if (vlen > 0) {
			out.write(value.charAt(0) & 0xFF);

        	padder.pad(out, value.substring(1), vlen - 1);
		} else {
			padder.pad(out, "", 0);
		}
	}

	public Type<String> clone() {
		return new AsciiAmount(this);
	}
}
