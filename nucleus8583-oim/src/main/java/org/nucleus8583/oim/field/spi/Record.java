package org.nucleus8583.oim.field.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.nucleus8583.oim.field.Field;
import org.nucleus8583.oim.util.BeanAccessor;

public class Record implements Field {

	private int no;
	
	private boolean textMode;

	private Field[] fields;
	
	private int count;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}
	
	public void setTextMode(boolean textMode) {
		this.textMode = textMode;
	}

	public void setFields(List<Field> fields) {
		count = fields.size();
		this.fields = fields.toArray(new Field[0]);
	}

	public boolean supportWriter() {
		return textMode;
	}

	public boolean supportOutputStream() {
		return !textMode;
	}

	public void read(InputStream in, BeanAccessor ba, Map<String, Object> tmp) throws Exception {
		for (int i = 0; i < count; ++i) {
			fields[i].read(in, ba, tmp);
		}
	}

	public void read(Reader in, BeanAccessor ba, Map<String, Object> tmp) throws Exception {
		for (int i = 0; i < count; ++i) {
			fields[i].read(in, ba, tmp);
		}
	}

	public void write(OutputStream out, BeanAccessor ba, Map<String, Object> tmp) throws Exception {
		for (int i = 0; i < count; ++i) {
			fields[i].write(out, ba, tmp);
		}
	}

	public void write(Writer out, BeanAccessor ba, Map<String, Object> tmp) throws Exception {
		for (int i = 0; i < count; ++i) {
			fields[i].write(out, ba, tmp);
		}
	}
}
