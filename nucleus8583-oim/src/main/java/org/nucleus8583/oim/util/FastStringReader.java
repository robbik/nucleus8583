package org.nucleus8583.oim.util;

import java.io.IOException;
import java.io.Reader;

public class FastStringReader extends Reader {
	private final String value;

	private final int vlen;

	private int readIndex;

	private int remaining;

	public FastStringReader(String value) {
		this.value = value;
		this.vlen = StringUtil.nullOrEmpty(value, false) ? 0 : value.length();

		this.readIndex = 0;
		this.remaining = this.vlen;
	}

	public int read(char[] cbuf, int off, int len) throws IOException {
		if (remaining == 0) {
			return -1;
		}

		int actualLen = Math.min(len, remaining);
		if (actualLen > 0) {
			value.getChars(readIndex, readIndex + actualLen, cbuf, off);

			readIndex += actualLen;
			remaining -= actualLen;
		}

		return actualLen;
	}

	@Override
	public int read() throws IOException {
		if (remaining <= 0) {
			return -1;
		}

		char cc = value.charAt(readIndex);

		++readIndex;
		--remaining;

		return cc;
	}

	@Override
	public int read(char[] cbuf) throws IOException {
		return read(cbuf, 0, cbuf.length);
	}

	@Override
	public long skip(long n) throws IOException {
		int actualN = Math.min(remaining, (int) n);

		readIndex += actualN;
		remaining -= actualN;

		return actualN;
	}

	@Override
	public boolean ready() throws IOException {
		return remaining > 0;
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public void mark(int readAheadLimit) throws IOException {
		throw new IOException();
	}

	@Override
	public void reset() throws IOException {
		this.readIndex = 0;
		this.remaining = this.vlen;
	}

	public void close() throws IOException {
		this.readIndex = 0;
		this.remaining = this.vlen;
	}
}
