package org.nucleus8583.oim.field.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.nucleus8583.oim.field.Field;

public class Skip implements Field {

	private int no;
	
	private int length;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}
	
	public void setLength(int length) {
		this.length = length;
	}

	public boolean supportWriter() {
		return false;
	}

	public boolean supportOutputStream() {
		return false;
	}

	public void read(InputStream in, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		in.skip(length);
	}

	public void read(Reader in, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		in.skip(length);
	}

	public void write(OutputStream out, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		throw new UnsupportedOperationException();
	}

	public void write(Writer out, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		throw new UnsupportedOperationException();
	}
}
