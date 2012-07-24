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
		countName = "transient:" + name + "____count";
	}

	public void read(InputStream in, Map<String, Object> root) throws Exception {
		root.put(countName, type.read(in));
	}

	public void read(Reader in, Map<String, Object> root) throws Exception {
		root.put(countName, type.read(in));
	}

	public void write(OutputStream out, Map<String, Object> root) throws Exception {
		type.write(out, ((Collection<?>) root.get(name)).size());
	}

	public void write(Writer out, Map<String, Object> root) throws Exception {
		type.write(out, ((Collection<?>) root.get(name)).size());
	}
}
