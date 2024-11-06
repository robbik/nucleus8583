package org.nucleus8583.oim.util;

import java.lang.reflect.Field;
import java.util.Map;

import net.sf.cglib.reflect.FastMethod;

import org.nucleus8583.oim.Access;

public class BeanAccessor {
	
	private final Object bean;
	
	private final ClassAccessor c;
	
	private final Access access;
	
	public BeanAccessor(Object bean, Access access) {
		if (bean == null) {
			throw new NullPointerException("bean");
		}
		
		if (access == null) {
			throw new NullPointerException("access");
		}
		
		if ((access == Access.VIRTUAL) && !(bean instanceof Map<?, ?>)) {
			throw new IllegalArgumentException("to use VIRTUAL access, bean must be an instance of java.util.Map");
		}
		
		this.bean = bean;
		this.c = ClassAccessor.getInstance(bean.getClass());
		
		this.access = access;
	}
	
	@SuppressWarnings("unchecked")
	public void set(String name, Object value) throws Exception {
		switch (access) {
		case FIELD:
			Field f = c.fields.get(name);
			if (f == null) {
				throw new IllegalArgumentException("no field " + name + " found in class " + bean.getClass());
			}
			
			try {
				f.set(bean, value);
			} catch (Exception e) {
				throw new Exception("unable to set value of field " + name + " on class " + bean.getClass(), e);
			}
			
			break;
		case PROPERTY:
			FastMethod m = c.setters.get(name);
			if (m == null) {
				throw new IllegalArgumentException("no setter " + name + " found in class " + bean.getClass());
			}
			
			try {
				m.invoke(bean, new Object[] { value });
			} catch (Exception e) {
				throw new Exception("unable to invoke setter " + m.getName() + " on class " + bean.getClass(), e);
			}
			
			break;
		default: // VIRTUAL
			((Map<String, Object>) bean).put(name, value);
			break;
		}
	}
	
	public Object get(String name) throws Exception {
		Object value;
		
		switch (access) {
		case FIELD:
			Field f = c.fields.get(name);
			if (f == null) {
				throw new IllegalArgumentException("no field " + name + " found in class " + bean.getClass());
			}
			
			try {
				value = f.get(bean);
			} catch (Exception e) {
				throw new Exception("unable to get value from field " + name + " on class " + bean.getClass(), e);
			}
			
			break;
		case PROPERTY:
			FastMethod m = c.getters.get(name);
			if (m == null) {
				throw new IllegalArgumentException("no getter " + name + " found in class " + bean.getClass());
			}
			
			try {
				value = m.invoke(bean, null);
			} catch (Exception e) {
				throw new Exception("unable to invoke getter " + m.getName() + " on class " + bean.getClass(), e);
			}
			
			break;
		default: // VIRTUAL
			value = ((Map<?, ?>) bean).get(name);
			break;
		}
		
		return value;
	}
	
	public Object getBean() {
		return bean;
	}
}
