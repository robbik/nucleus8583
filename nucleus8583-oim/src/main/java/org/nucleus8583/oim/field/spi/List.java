package org.nucleus8583.oim.field.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.nucleus8583.oim.field.Field;

import rk.commons.inject.annotation.Init;

public class List implements Field {

	private int no;

	private String name;
	
	private String countName;

	private String iteratorName;
	
	private boolean partial;
	
	private int capacity;

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

	public void setPartial(boolean partial) {
		this.partial = partial;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public void setChildFields(java.util.List<Field> childFields) {
		this.childFields = childFields.toArray(new Field[0]);
	}
	
	public void setTextMode(boolean textMode) {
		this.textMode = textMode;
	}
	
	@Init
	public void initialize() throws Exception {
		countName = "list:" + name + "____count";
		
		iteratorName = "list:" + name + "____iterator";
	}

	public boolean supportWriter() {
		return textMode;
	}

	public boolean supportOutputStream() {
		return !textMode;
	}

	@SuppressWarnings("unchecked")
	private Collection<Map<String, Object>> beforeRead(Map<String, Object> root) {
		Collection<Map<String, Object>> list;
		
		if (!partial || !root.containsKey(name)) {
			list = new ArrayList<Map<String,Object>>();
			root.put(name, list);
		} else {
			list = (Collection<Map<String, Object>>) root.get(name);
		}
		
		return list;
	}

	@SuppressWarnings("unchecked")
	private Iterable<Map<String, Object>> beforeWrite(Map<String, Object> root) {
		Iterable<Map<String, Object>> iterable;
		
		if (!root.containsKey(name)) {
			return null;
		} else {
			iterable = (Iterable<Map<String, Object>>) root.get(name);
		}
		
		return iterable;
	}
	
	public void read(InputStream in, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		Collection<Map<String, Object>> c = beforeRead(root);
		
		int count;
		
		if (tmp.containsKey(countName)) {
			count = ((Integer) tmp.get(countName)).intValue();
		} else {
			count = 0;
		}
		
		for (int i = 0; i < count; ++i) {
			Map<String, Object> e = new HashMap<String, Object>();
			
			for (int j = 0, n = childFields.length; j < n; ++j) {
				childFields[j].read(in, e, tmp);
			}
			
			c.add(e);
		}
	}

	public void read(Reader in, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		Collection<Map<String, Object>> c = beforeRead(root);
		
		int count;
		
		if (tmp.containsKey(countName)) {
			count = ((Integer) tmp.get(countName)).intValue();
		} else {
			count = 0;
		}
		
		for (int i = 0; i < count; ++i) {
			Map<String, Object> e = new HashMap<String, Object>();
			
			for (int j = 0, n = childFields.length; j < n; ++j) {
				childFields[j].read(in, e, tmp);
			}
			
			c.add(e);
		}
	}

	@SuppressWarnings("unchecked")
	public void write(OutputStream out, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		Iterable<Map<String, Object>> iterable = beforeWrite(root);
		if (iterable == null) {
			return;
		}
		
		Iterator<Map<String, Object>> it;
		
		if (partial) {
			it = (Iterator<Map<String, Object>>) tmp.get(iteratorName);
			
			if (it == null) {
				it = iterable.iterator();
				
				tmp.put(iteratorName, it);
			}
		} else {
			it = iterable.iterator();
		}
		
		for (int i = 0; (i < capacity) && it.hasNext(); ++i) {
			Map<String, Object> e = it.next();
			
			for (int j = 0, n = childFields.length; j < n; ++j) {
				childFields[j].write(out, e, tmp);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void write(Writer out, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		Iterable<Map<String, Object>> iterable = beforeWrite(root);
		if (iterable == null) {
			return;
		}
		
		Iterator<Map<String, Object>> it;
		
		if (partial) {
			it = (Iterator<Map<String, Object>>) tmp.get(iteratorName);
			
			if (it == null) {
				it = iterable.iterator();
				
				tmp.put(iteratorName, it);
			}
		} else {
			it = iterable.iterator();
		}
		
		for (int i = 0; (i < capacity) && it.hasNext(); ++i) {
			Map<String, Object> e = it.next();
			
			for (int j = 0, n = childFields.length; j < n; ++j) {
				childFields[j].write(out, e, tmp);
			}
		}
	}
}
