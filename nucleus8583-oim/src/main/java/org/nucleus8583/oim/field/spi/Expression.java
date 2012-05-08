package org.nucleus8583.oim.field.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

public abstract class Expression extends Basic {

	public void read(InputStream in, Map<String, Object> root) throws Exception {
		throw new UnsupportedOperationException();
	}

	public void read(Reader in, Map<String, Object> root) throws Exception {
		throw new UnsupportedOperationException();
	}

	public void write(OutputStream out, Map<String, Object> root) throws Exception {
		type.write(out, eval(root));
	}

	public void write(Writer out, Map<String, Object> root) throws Exception {
		type.write(out, eval(root));
	}
	
	protected abstract Object eval(Map<String, Object> root) throws Exception;
}
