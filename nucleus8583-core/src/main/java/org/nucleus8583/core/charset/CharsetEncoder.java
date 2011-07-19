package org.nucleus8583.core.charset;

import java.io.IOException;
import java.io.OutputStream;

public interface CharsetEncoder {

	void write(OutputStream out, String str) throws IOException;

	void write(OutputStream out, String str, int off, int len) throws IOException;

	void write(OutputStream out, int ichar) throws IOException;

	void write(OutputStream out, char[] cbuf) throws IOException;

	void write(OutputStream out, char[] cbuf, int off, int len) throws IOException;

	byte[] toBytes(char[] cbuf, int off, int len);
}
