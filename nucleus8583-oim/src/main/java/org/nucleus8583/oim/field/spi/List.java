package org.nucleus8583.oim.field.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.nucleus8583.oim.field.Field;
import org.nucleus8583.oim.field.type.ComplexType;
import org.nucleus8583.oim.field.type.Type;
import org.nucleus8583.oim.util.BeanAccessor;

import rk.commons.inject.annotation.Init;

public class List implements Field {

	private int no;

	private String name;
	
	private Type type;
	
	private boolean partial;
	
	private int capacity;
	
	private String countName;

	private String iteratorName;
	
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

	public void setPartial(boolean partial) {
		this.partial = partial;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	@Init
	public void initialize() throws Exception {
		if (!(type instanceof ComplexType)) {
			throw new Exception("list (" + name + ")'s type must be a complex-type");
		}

		countName = "list:" + name + "____count";
		iteratorName = "list:" + name + "____iterator";
	}

	public boolean supportWriter() {
		return true;
	}

	public boolean supportOutputStream() {
		return true;
	}

	@SuppressWarnings("unchecked")
	private Collection<Object> beforeRead(BeanAccessor ba) throws Exception {
		Collection<Object> c;
		
		if (!partial) {
			c = null;
		} else {
			c = (Collection<Object>) ba.get(name);
		}
		
		if (c == null) {
			c = new ArrayList<Object>();
			ba.set(name, c);
		}

		return c;
	}
	
	public void read(InputStream in, BeanAccessor ba, Map<String, Object> tmp) throws Exception {
		Collection<Object> c = beforeRead(ba);
		
		int count;
		
		if (tmp.containsKey(countName)) {
			count = ((Integer) tmp.get(countName)).intValue();
		} else {
			count = 0;
		}
		
		for (int i = 0; i < count; ++i) {
			c.add(type.read(in, tmp));
		}
	}

	public void read(Reader in, BeanAccessor ba, Map<String, Object> tmp) throws Exception {
		Collection<Object> c = beforeRead(ba);
		
		int count;
		
		if (tmp.containsKey(countName)) {
			count = ((Integer) tmp.get(countName)).intValue();
		} else {
			count = 0;
		}
		
		for (int i = 0; i < count; ++i) {
			c.add(type.read(in, tmp));
		}
	}

	public void write(OutputStream out, BeanAccessor ba, Map<String, Object> tmp) throws Exception {
		Iterable<?> iterable = (Iterable<?>) ba.get(name);
		if (iterable == null) {
			return;
		}
		
		Iterator<?> it;
		
		if (partial) {
			it = (Iterator<?>) tmp.get(iteratorName);
			
			if (it == null) {
				it = iterable.iterator();
				tmp.put(iteratorName, it);
			}
		} else {
			it = iterable.iterator();
		}
		
		for (int i = 0; (i < capacity) && it.hasNext(); ++i) {
			type.write(out, it.next(), tmp);
		}
	}

	public void write(Writer out, BeanAccessor ba, Map<String, Object> tmp) throws Exception {
		Iterable<?> iterable = (Iterable<?>) ba.get(name);
		if (iterable == null) {
			return;
		}
		
		Iterator<?> it;
		
		if (partial) {
			it = (Iterator<?>) tmp.get(iteratorName);
			
			if (it == null) {
				it = iterable.iterator();
				tmp.put(iteratorName, it);
			}
		} else {
			it = iterable.iterator();
		}
		
		for (int i = 0; (i < capacity) && it.hasNext(); ++i) {
			type.write(out, it.next(), tmp);
		}
	}
}
