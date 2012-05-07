package org.nucleus8583.oim.field.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.nucleus8583.oim.field.Field;

public class BinaryPad implements Field {

	private int no;

	private byte[] padWith;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public void setPadWith(byte[] padWith) {
		this.padWith = padWith;
	}

	public boolean supportReader() {
		return false;
	}

	public boolean supportInputStream() {
		return false;
	}

	public boolean supportWriter() {
		return true;
	}

	public boolean supportOutputStream() {
		return true;
	}

	public void read(InputStream in, Map<String, Object> root) throws Exception {
		throw new UnsupportedOperationException();
	}

	public void read(Reader in, Map<String, Object> root) throws Exception {
		throw new UnsupportedOperationException();
	}

	public void write(OutputStream out, Map<String, Object> root) throws Exception {
		if (padWith != null) {
			out.write(padWith);
		}
	}

	public void write(Writer out, Map<String, Object> root) throws Exception {
		throw new UnsupportedOperationException();
	}
}
