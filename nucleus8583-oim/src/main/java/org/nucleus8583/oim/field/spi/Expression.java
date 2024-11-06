package org.nucleus8583.oim.field.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.nucleus8583.oim.util.BeanAccessor;

public abstract class Expression extends Basic {

	@Override
	public void read(InputStream in, BeanAccessor ba, Map<String, Object> tmp) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public void read(Reader in, BeanAccessor ba, Map<String, Object> tmp) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public void write(OutputStream out, BeanAccessor ba, Map<String, Object> tmp) throws Exception {
		type.write(out, eval(ba.getBean()), tmp);
	}

	@Override
	public void write(Writer out, BeanAccessor ba, Map<String, Object> tmp) throws Exception {
		type.write(out, eval(ba.getBean()), tmp);
	}
	
	protected abstract Object eval(Object object) throws Exception;
}
