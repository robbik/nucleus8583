package org.nucleus8583.oim.field.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import org.nucleus8583.oim.util.BeanAccessor;

public class ListCount extends Basic {
	
	private String countName;

	public void initialize() {
		countName = "list:" + name + "____count";
	}

	@Override
	public void read(InputStream in, BeanAccessor ba, Map<String, Object> tmp) throws Exception {
		tmp.put(countName, (Integer) type.read(in, tmp));
	}

	@Override
	public void read(Reader in, BeanAccessor ba, Map<String, Object> tmp) throws Exception {
		tmp.put(countName, (Integer) type.read(in, tmp));
	}

	@Override
	public void write(OutputStream out, BeanAccessor ba, Map<String, Object> tmp) throws Exception {
		type.write(out, Integer.valueOf(((Collection<?>) ba.get(name)).size()), tmp);
	}

	@Override
	public void write(Writer out, BeanAccessor ba, Map<String, Object> tmp) throws Exception {
		type.write(out, Integer.valueOf(((Collection<?>) ba.get(name)).size()), tmp);
	}
}
