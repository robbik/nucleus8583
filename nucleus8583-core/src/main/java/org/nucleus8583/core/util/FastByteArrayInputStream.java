package org.nucleus8583.core.util;

import java.io.IOException;
import java.io.InputStream;

public class FastByteArrayInputStream extends InputStream {

	protected byte buf[];

	protected int pos;

	protected int count;

	public FastByteArrayInputStream() {
		buf = null;
		count = 0;
		
		pos = 0;
	}

	@Override
	public int read() {
		return (pos < count) ? (buf[pos++] & 0xff) : -1;
	}

	@Override
	public int read(byte b[], int off, int len) {
		if (b == null) {
			throw new NullPointerException();
		} else if (off < 0 || len < 0 || len > b.length - off) {
			throw new IndexOutOfBoundsException();
		}

		if (pos >= count) {
			return -1;
		}

		int avail = count - pos;
		if (len > avail) {
			len = avail;
		}

		if (len <= 0) {
			return 0;
		}

		System.arraycopy(buf, pos, b, off, len);
		pos += len;

		return len;
	}

	@Override
	public long skip(long n) {
		long k = count - pos;
		if (n < k) {
			k = n < 0 ? 0 : n;
		}
		
		pos += k;
		return k;
	}

	@Override
	public int available() {
		return count - pos;
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public void reset() {
		pos = 0;
	}

	public void reset(byte[] buf) {
		this.buf = buf;
		this.count = buf.length;

		this.pos = 0;
	}

	@Override
	public void close() {
		// do nothing
	}
}
