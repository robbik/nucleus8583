package org.nucleus8583.oim.field.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.nucleus8583.oim.field.Field;

public class Record implements Field {

	private int no;

	private String name;
	
	private boolean textMode;

	private Field[] childFields;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setTextMode(boolean textMode) {
		this.textMode = textMode;
	}

	public void setChildFields(List<Field> childFields) {
		this.childFields = childFields.toArray(new Field[0]);
	}

	public boolean supportWriter() {
		return textMode;
	}

	public boolean supportOutputStream() {
		return !textMode;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> beforeRead(Map<String, Object> root) {
		if (name != null) {
			if (root.containsKey(name)) {
				root = (Map<String, Object>) root.get(name);
			} else {
				root = Collections.emptyMap();
			}
		}
		
		return root;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> beforeWrite(Map<String, Object> root) {
		if (name != null) {
			if (root.containsKey(name)) {
				root = (Map<String, Object>) root.get(name);
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				root.put(name, map);
				
				root = map;
			}
		}
		
		return root;
	}

	public void read(InputStream in, Map<String, Object> root) throws Exception {
		root = beforeRead(root);
		
		for (int i = 0, n = childFields.length; i < n; ++i) {
			childFields[i].read(in, root);
		}
	}

	public void read(Reader in, Map<String, Object> root) throws Exception {
		root = beforeRead(root);
		
		for (int i = 0, n = childFields.length; i < n; ++i) {
			childFields[i].read(in, root);
		}
	}

	public void write(OutputStream out, Map<String, Object> root) throws Exception {
		root = beforeWrite(root);
		
		for (int i = 0, n = childFields.length; i < n; ++i) {
			childFields[i].write(out, root);
		}
	}

	public void write(Writer out, Map<String, Object> root) throws Exception {
		root = beforeWrite(root);
		
		for (int i = 0, n = childFields.length; i < n; ++i) {
			childFields[i].write(out, root);
		}
	}
}
