package org.nucleus8583.core.field.spi;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.field.Type;
import org.nucleus8583.core.util.BcdPadder;
import org.nucleus8583.core.xml.Alignment;

import rk.commons.beans.factory.support.InitializingBean;
import rk.commons.util.StringEscapeUtils;
import rk.commons.util.StringUtils;

public class BcdAmount implements Type<String>, InitializingBean {

	private static final long serialVersionUID = -5615324004502124085L;

	protected final BcdPadder padder;

	protected int length;

    public BcdAmount() {
        padder = new BcdPadder();
        
        padder.setAlign(Alignment.UNTRIMMED_RIGHT);
        padder.setPadWith('0');
        
        padder.setEmptyValue(new char[] { '0' });
    }
    
    public BcdAmount(BcdAmount o) {
    	padder = new BcdPadder(o.padder);
    	length = o.length;
    }

	public void setLength(int length) {
		if (length <= 0) {
			throw new IllegalArgumentException("length must greater than zero");
		}

        padder.setLength(length - 1);

		this.length = length;
	}

	public void setAlignment(Alignment align) {
		if (align == null) {
			throw new IllegalArgumentException("alignment required");
		}

		padder.setAlign(align);

		if (align == Alignment.NONE) {
			padder.setPadWith('0');
		}
	}

	public void setPadWith(String padWith) {
		if (!StringUtils.hasText(padWith, false)) {
			throw new IllegalArgumentException("pad-with required");
		}

		padder.setPadWith(StringEscapeUtils.escapeJava(padWith).charAt(0));
	}

	public void setEmptyValue(String emptyValue) {
		if (emptyValue == null) {
			padder.setEmptyValue(new char[0]);
		} else {
			padder.setEmptyValue(StringEscapeUtils.escapeJava(emptyValue).toCharArray());
		}
	}

	public void initialize() throws Exception {
		padder.initialize();
	}

    public String read(InputStream in) throws IOException {
        int first = in.read();
        if (first < 0) {
            throw new EOFException();
        }

        return ((char) first) + new String(padder.unpad(in));
    }

    public void write(OutputStream out, String value) throws IOException {
        int vlen = value.length();
        if (vlen > length) {
            throw new IllegalArgumentException("value too long, expected " + length + " but actual is " + vlen);
        }

        out.write(value.charAt(0) & 0xFF);
        padder.pad(out, value.substring(1), vlen - 1);
    }

	public void writeBitmap(OutputStream out, byte[] bitmap, int off, int len)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	public void readBitmap(InputStream in, byte[] bitmap, int off, int len)
			throws IOException {
		throw new UnsupportedOperationException();
	}
	
	public Type<String> clone() {
		return new BcdAmount(this);
	}
}
