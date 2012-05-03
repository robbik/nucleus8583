package org.nucleus8583.core.field.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.field.Type;
import org.nucleus8583.core.util.AsciiPadder;
import org.nucleus8583.core.xml.Alignment;

import rk.commons.ioc.factory.support.InitializingObject;
import rk.commons.util.StringEscapeUtils;
import rk.commons.util.StringUtils;

public class AsciiText implements Type<String>, InitializingObject {

	private static final long serialVersionUID = -5615324004502124085L;

	protected final AsciiPadder padder;

	protected int length;

	public AsciiText() {
		padder = new AsciiPadder();
		
		padder.setAlign(Alignment.TRIMMED_LEFT);
		padder.setPadWith(' ');
		
		padder.setEmptyValue(new char[0]);
	}
	
	public AsciiText(AsciiText o) {
		padder = new AsciiPadder(o.padder);
		
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
			padder.setPadWith(' ');
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
		return new String(padder.unpad(in, length));
	}

	public void write(OutputStream out, String value) throws IOException {
		int vlen = value.length();
		if (vlen > length) {
			throw new IllegalArgumentException("value too long, expected " + length + " but actual is " + vlen);
		}

		padder.pad(out, value, vlen);
	}

	public void readBitmap(InputStream in, byte[] bitmap, int off, int len)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	public void writeBitmap(OutputStream out, byte[] bitmap, int off, int len)
			throws IOException {
		throw new UnsupportedOperationException();
	}
	
	public Type<String> clone() {
		return new AsciiText(this);
	}
}
