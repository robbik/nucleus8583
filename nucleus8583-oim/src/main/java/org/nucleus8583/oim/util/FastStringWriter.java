package org.nucleus8583.oim.util;

import java.io.IOException;
import java.io.Writer;

public class FastStringWriter extends Writer {
	
	private final StringBuilder sb;
	
	public FastStringWriter(StringBuilder sb) {
		this.sb = sb;
	}
	
	public FastStringWriter(int capacity) {
		this(new StringBuilder(capacity));
	}
	
	public FastStringWriter() {
		this(1024);
	}
	
	public void reset() {
		sb.setLength(0);
	}
	
	@Override
	public String toString() {
		return sb.toString();
	}

	public void flush() throws IOException {
		// do nothing
	}

	public void close() throws IOException {
		reset();
	}

	@Override
	public Writer append(char c) throws IOException {
		sb.append(c);
		return this;
	}

	@Override
	public Writer append(CharSequence csq, int start, int end) throws IOException {
		sb.append(csq, start, end);
		return this;
	}

	@Override
	public Writer append(CharSequence csq) throws IOException {
		sb.append(csq);
		return this;
	}

	@Override
	public void write(char[] cbuf) throws IOException {
		sb.append(cbuf);
	}

	@Override
	public void write(int c) throws IOException {
		sb.append((char) c);
	}

	@Override
	public void write(String str, int off, int len) throws IOException {
		sb.append(str, off, len);
	}

	@Override
	public void write(String str) throws IOException {
		sb.append(str);
	}

	public void write(char[] buf, int offset, int len) throws IOException {
		sb.append(buf, offset, len);
	}
}
