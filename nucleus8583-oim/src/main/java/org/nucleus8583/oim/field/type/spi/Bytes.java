package org.nucleus8583.oim.field.type.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import org.nucleus8583.oim.field.Alignment;
import org.nucleus8583.oim.field.type.Type;
import org.nucleus8583.oim.util.BytesPadder;

import rk.commons.ioc.factory.support.InitializingObject;
import rk.commons.util.IOUtils;
import rk.commons.util.StringEscapeUtils;
import rk.commons.util.StringUtils;

public class Bytes implements Type, InitializingObject {
	
	protected final BytesPadder padder;
	
	protected int length;
	
	public Bytes() {
		padder = new BytesPadder();
		
		padder.setAlign(Alignment.NONE);
		padder.setPadWith((byte) 0);
		
		padder.setEmptyValue(new byte[0]);
	}
	
	public Bytes(Bytes o) {
		padder = new BytesPadder(o.padder);
		
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

	public boolean supportWriter() {
		return false;
	}

	public boolean supportOutputStream() {
		return true;
	}

	public Object read(InputStream in) throws Exception {
		if (length > 0) {
			byte[] buf = new byte[length];
			IOUtils.readFully(in, buf, 0, length);
			
			return padder.unpad(buf, length);
		} else {
			byte[] buf = IOUtils.readUntilEof(in);
			
			return new String(padder.unpad(buf, buf.length));
		}
	}

	public void write(OutputStream out, Object o) throws Exception {
		byte[] value = (byte[]) o;
		
		if (length > 0) {
			int vlen = value.length;
			if (vlen > length) {
				vlen = length;
			}
			
			padder.pad(out, value, 0, vlen);
		} else {
			out.write(value);
		}
	}

	public Object read(Reader in) throws Exception {
		throw new UnsupportedOperationException();
	}

	public void write(Writer out, Object value) throws Exception {
		throw new UnsupportedOperationException();
	}
	
	public Type clone() {
		return new Bytes(this);
	}
}
