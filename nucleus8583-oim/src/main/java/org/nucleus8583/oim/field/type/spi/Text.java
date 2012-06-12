package org.nucleus8583.oim.field.type.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import org.nucleus8583.oim.field.Alignment;
import org.nucleus8583.oim.field.type.Type;
import org.nucleus8583.oim.util.TextPadder;

import rk.commons.inject.annotation.Init;
import rk.commons.util.IOHelper;
import rk.commons.util.StringHelper;

public class Text implements Type {

	private static final long serialVersionUID = -5615324004502124085L;

	protected final TextPadder padder;

	protected int length;

	public Text() {
		padder = new TextPadder();
		
		padder.setAlign(Alignment.TRIMMED_LEFT);
		padder.setPadWith(' ');
		
		padder.setEmptyValue(new char[0]);
	}
	
	public Text(Text o) {
		padder = new TextPadder(o.padder);
		
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
		if (!StringHelper.hasText(padWith, false)) {
			throw new IllegalArgumentException("pad-with required");
		}

		padder.setPadWith(StringHelper.escapeJava(padWith).charAt(0));
	}

	public void setEmptyValue(String emptyValue) {
		if (emptyValue == null) {
			padder.setEmptyValue(new char[0]);
		} else {
			padder.setEmptyValue(StringHelper.escapeJava(emptyValue).toCharArray());
		}
	}

	@Init
	public void initialize() throws Exception {
		padder.initialize();
	}

	public boolean supportWriter() {
		return true;
	}

	public boolean supportOutputStream() {
		return false;
	}

	public Object read(InputStream in) throws Exception {
		throw new UnsupportedOperationException();
	}

	public Object read(Reader in) throws Exception {
		if (length > 0) {
			char[] buf = new char[length];
			IOHelper.readFully(in, buf, 0, length);
			
			return new String(padder.unpad(buf, length));
		} else {
			char[] buf = IOHelper.readUntilEof(in);
			
			return new String(padder.unpad(buf, buf.length));
		}
	}

	public void write(OutputStream out, Object o) throws Exception {
		throw new UnsupportedOperationException();
	}

	public void write(Writer out, Object o) throws Exception {
		String value = (String) o;
		
		if (value == null) {
			value = "";
		}
		
		if (length > 0) {
			int vlen = value.length();

			if (vlen > length) {
				vlen = length;
			}
			
			padder.pad(out, value, vlen);
		} else {
			out.write(value);
		}
	}
	
	public Type clone() {
		return new Text(this);
	}
}
