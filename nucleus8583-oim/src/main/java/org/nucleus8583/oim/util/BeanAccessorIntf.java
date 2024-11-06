package org.nucleus8583.oim.util;

public interface BeanAccessorIntf {

	void set(String name, Object value) throws Exception;
	
	Object get(String name) throws Exception;
	
	Object getBean();
}
