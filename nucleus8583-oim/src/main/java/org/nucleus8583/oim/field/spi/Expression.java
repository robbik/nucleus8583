package org.nucleus8583.oim.field.spi;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

public abstract class Expression extends Basic {

	@Override
	public void read(InputStream in, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public void read(Reader in, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public void write(OutputStream out, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		type.write(out, eval(root, tmp));
	}

	@Override
	public void write(Writer out, Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		type.write(out, eval(root, tmp));
	}
	
	protected abstract Object eval(Map<String, Object> root, Map<String, Object> tmp) throws Exception;
}
