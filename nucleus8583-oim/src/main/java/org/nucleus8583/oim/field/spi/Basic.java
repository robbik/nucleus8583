package org.nucleus8583.oim.field.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.nucleus8583.oim.field.Field;
import org.nucleus8583.oim.field.type.Type;

public class Basic implements Field {

	protected int no;

	protected String name;

	protected Type type;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean supportWriter() {
		return type.supportWriter();
	}

	public boolean supportOutputStream() {
		return type.supportOutputStream();
	}

	public void read(InputStream in, Map<String, Object> root) throws Exception {
		root.put(name, type.read(in));
	}

	public void read(Reader in, Map<String, Object> root) throws Exception {
		root.put(name, type.read(in));
	}

	public void write(OutputStream out, Map<String, Object> root) throws Exception {
		type.write(out, root.get(name));
	}

	public void write(Writer out, Map<String, Object> root) throws Exception {
		type.write(out, root.get(name));
	}
}
