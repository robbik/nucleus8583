package org.nucleus8583.oim.field.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

public class ListCount extends Basic {
	
	private String countName;

	public void initialize() {
		countName = "list:" + name + "____count";
	}

	@Override
	public void read(InputStream in, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		tmp.put(countName, type.read(in));
	}

	@Override
	public void read(Reader in, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		tmp.put(countName, type.read(in));
	}

	@Override
	public void write(OutputStream out, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		type.write(out, ((Collection<?>) root.get(name)).size());
	}

	@Override
	public void write(Writer out, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		type.write(out, ((Collection<?>) root.get(name)).size());
	}
}
