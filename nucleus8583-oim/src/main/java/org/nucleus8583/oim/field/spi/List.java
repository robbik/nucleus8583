package org.nucleus8583.oim.field.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.nucleus8583.oim.field.Field;

public class List implements Field {

	private int no;

	private String name;

	private String countName;
	
	private boolean append;
	
	private int maxCount;

	private Field[] childFields;
	
	private boolean textMode;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setCountName(String countName) {
		this.countName = countName;
	}

	public void setAppend(boolean append) {
		this.append = append;
	}
	
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public void setChildFields(java.util.List<Field> childFields) {
		this.childFields = childFields.toArray(new Field[0]);
	}
	
	public void setTextMode(boolean textMode) {
		this.textMode = textMode;
	}

	public boolean supportWriter() {
		return textMode;
	}

	public boolean supportOutputStream() {
		return !textMode;
	}

	@SuppressWarnings("unchecked")
	private java.util.List<Map<String, Object>> beforeRead(Map<String, Object> root) {
		java.util.List<Map<String, Object>> list;
		
		if (!append || !root.containsKey(name)) {
			list = new ArrayList<Map<String,Object>>();
			root.put(name, list);
		} else {
			list = (java.util.List<Map<String, Object>>) root.get(name);
		}
		
		return list;
	}

	@SuppressWarnings("unchecked")
	private java.util.List<Map<String, Object>> beforeWrite(Map<String, Object> root) {
		java.util.List<Map<String, Object>> list;
		
		if (!root.containsKey(name)) {
			return null;
		} else {
			list = (java.util.List<Map<String, Object>>) root.get(name);
		}
		
		return list;
	}
	
	public void read(InputStream in, Map<String, Object> root) throws Exception {
		java.util.List<Map<String, Object>> list = beforeRead(root);
		int count;
		
		if (root.containsKey(countName)) {
			count = ((Integer) root.get(countName)).intValue();
		} else {
			count = list.size();
		}
		
		if (count > maxCount) {
			count = maxCount;
		}
		
		for (int i = 0; i < count; ++i) {
			Map<String, Object> e = new HashMap<String, Object>();
			
			for (int j = 0, n = childFields.length; j < n; ++j) {
				childFields[j].read(in, e);
			}
			
			list.add(e);
		}
	}

	public void read(Reader in, Map<String, Object> root) throws Exception {
		java.util.List<Map<String, Object>> list = beforeRead(root);
		int count;
		
		if (root.containsKey(countName)) {
			count = ((Integer) root.get(countName)).intValue();
		} else {
			count = list.size();
		}
		
		if (count > maxCount) {
			count = maxCount;
		}
		
		for (int i = 0; i < count; ++i) {
			Map<String, Object> e = new HashMap<String, Object>();
			
			for (int j = 0, n = childFields.length; j < n; ++j) {
				childFields[j].read(in, e);
			}
			
			list.add(e);
		}
	}

	public void write(OutputStream out, Map<String, Object> root) throws Exception {
		java.util.List<Map<String, Object>> list = beforeWrite(root);
		if (list == null) {
			return;
		}
		
		int i;
		
		if (append && root.containsKey(countName)) {
			i = ((Integer) root.get(countName)).intValue();
		} else {
			i = 0;
		}
		
		int count = list.size();
		if (count > maxCount) {
			count = maxCount;
		}
		
		for (; i < count; ++i) {
			Map<String, Object> e = (Map<String, Object>) list.get(i);
			
			for (int j = 0, n = childFields.length; j < n; ++j) {
				childFields[i].write(out, e);
			}
		}
		
		if (append) {
			root.put(countName, Integer.valueOf(i));
		}
	}

	public void write(Writer out, Map<String, Object> root) throws Exception {
		java.util.List<Map<String, Object>> list = beforeWrite(root);
		if (list == null) {
			return;
		}
		
		int i;
		
		if (append && root.containsKey(countName)) {
			i = int.class.cast(root.get(countName));
		} else {
			i = 0;
		}
		
		int count = list.size();
		if (count > maxCount) {
			count = maxCount;
		}
		
		for (; i < count; ++i) {
			Map<String, Object> e = (Map<String, Object>) list.get(i);
			
			for (int j = 0, n = childFields.length; j < n; ++j) {
				childFields[j].write(out, e);
			}
		}
		
		if (append) {
			root.put(countName, i);
		}
	}
}
