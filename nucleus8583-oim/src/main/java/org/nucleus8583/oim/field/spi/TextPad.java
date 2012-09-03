package org.nucleus8583.oim.field.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.nucleus8583.oim.field.Field;

public class TextPad implements Field {

	private int no;
	
	private char[] padWith;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public void setPadWith(char[] padWith) {
		this.padWith = padWith;
	}

	public boolean supportWriter() {
		return true;
	}

	public boolean supportOutputStream() {
		return false;
	}

	public void read(InputStream in, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		throw new UnsupportedOperationException();
	}

	public void read(Reader in, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		throw new UnsupportedOperationException();
	}

	public void write(OutputStream out, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		throw new UnsupportedOperationException();
	}

	public void write(Writer out, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		if (padWith != null) {
			out.write(padWith);
		}
	}
}
