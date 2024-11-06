package org.nucleus8583.oim.field.type;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.nucleus8583.oim.Access;
import org.nucleus8583.oim.field.Field;
import org.nucleus8583.oim.util.BeanAccessor;

public abstract class ComplexType implements Type {
	
	protected Access access;
	
	protected Field[] fields;
	
	protected int count;
	
	public ComplexType() {
		fields = new Field[0];
		count = 0;
	}
	
	public ComplexType(ComplexType o) {
		access = o.access;
		
		fields = o.fields;
		count = o.count;
	}
	
	public void setAccess(Access access) {
		this.access = access;
	}

    public void setFields(List<Field> fields) {
        count = fields.size();
        this.fields = fields.toArray(new Field[0]);
    }

	public boolean supportWriter() {
		return true;
	}

	public boolean supportOutputStream() {
		return true;
	}

	public Object read(InputStream in, Map<String, Object> tmp) throws Exception {
		Object value = newBeanInstance();
		
		BeanAccessor ba = new BeanAccessor(value, access);
		
		for (int i = 0; i < count; ++i) {
			fields[i].read(in, ba, tmp);
		}
		
		return value;
	}

	public Object read(Reader in, Map<String, Object> tmp) throws Exception {
		Object value = newBeanInstance();
		
		BeanAccessor ba = new BeanAccessor(value, access);
		
		for (int i = 0; i < count; ++i) {
			fields[i].read(in, ba, tmp);
		}
		
		return value;
	}

	public void write(OutputStream out, Object value, Map<String, Object> tmp) throws Exception {
		BeanAccessor ba = new BeanAccessor(value, access);
		
		for (int i = 0; i < count; ++i) {
			fields[i].write(out, ba, tmp);
		}
	}

	public void write(Writer out, Object value, Map<String, Object> tmp) throws Exception {
		BeanAccessor ba = new BeanAccessor(value, access);
		
		for (int i = 0; i < count; ++i) {
			fields[i].write(out, ba, tmp);
		}
	}
	
	@Override
	public abstract ComplexType clone();

	public abstract Object newBeanInstance();
}
