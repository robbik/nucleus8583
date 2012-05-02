package org.nucleus8583.core.field.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.field.Type;
import org.nucleus8583.core.util.LiteralBinaryPadder;
import org.nucleus8583.core.xml.Alignment;

import rk.commons.beans.factory.support.InitializingBean;
import rk.commons.util.StringEscapeUtils;
import rk.commons.util.StringUtils;

public class LiteralBinary implements Type<byte[]>, InitializingBean {
	private static final long serialVersionUID = -5615324004502124085L;

	protected final LiteralBinaryPadder padder;

    protected int length;

	public LiteralBinary() {
		padder = new LiteralBinaryPadder();
		
		padder.setAlign(Alignment.NONE);
		padder.setPadWith((byte) 0);
		
		padder.setEmptyValue(new byte[0]);
	}
	
	public LiteralBinary(LiteralBinary o) {
		padder = new LiteralBinaryPadder(o.padder);
		
		length = o.length;
	}

	public void setLength(int length) {
		if (length <= 0) {
			throw new IllegalArgumentException("length must greater than zero");
		}

		padder.setLength(length);

		this.length = length;
	}

	public void setAlignment(Alignment align) {
		if (align == null) {
			throw new IllegalArgumentException("alignment required");
		}

		padder.setAlign(align);

		if (align == Alignment.NONE) {
			padder.setPadWith((byte) 0);
		}
	}

	public void setPadWith(String padWith) {
		if (!StringUtils.hasText(padWith, false)) {
			throw new IllegalArgumentException("pad-with required");
		}

		padder.setPadWith(StringEscapeUtils.escapeJava(padWith).getBytes()[0]);
	}

	public void setEmptyValue(String emptyValue) {
		if (emptyValue == null) {
			padder.setEmptyValue(new byte[0]);
		} else {
			padder.setEmptyValue(StringEscapeUtils.escapeJava(emptyValue).getBytes());
		}
	}

	public void initialize() throws Exception {
		padder.initialize();
	}

	public byte[] read(InputStream in) throws IOException {
		return padder.unpad(in);
	}

	public void write(OutputStream out, byte[] value) throws IOException {
		int vlen = value.length;
        if (vlen != length) {
            throw new IllegalArgumentException("value length is not equals to " + length + ", actual is " + vlen);
        }

        padder.pad(out, value, 0, vlen);
	}

	public void readBitmap(InputStream in, byte[] bitmap, int off, int len)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	public void writeBitmap(OutputStream out, byte[] bitmap, int off, int len)
			throws IOException {
		throw new UnsupportedOperationException();
	}
	
	public Type<byte[]> clone() {
		return new LiteralBinary(this);
	}
}
