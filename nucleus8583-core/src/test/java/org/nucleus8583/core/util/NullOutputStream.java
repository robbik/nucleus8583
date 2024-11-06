package org.nucleus8583.core.util;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream {

	public void write(int b) throws IOException {
		// do nothing
	}

	@Override
	public void close() throws IOException {
		// do nothing
	}

	@Override
	public void flush() throws IOException {
		// do nothing
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		// do nothing
	}

	@Override
	public void write(byte[] b) throws IOException {
		// do nothing
	}
}
