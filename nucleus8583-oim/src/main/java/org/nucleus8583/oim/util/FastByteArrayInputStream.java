package org.nucleus8583.oim.util;

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

	public int read() {
		return (pos < count) ? (buf[pos++] & 0xff) : -1;
	}

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
	
	public long skip(long n) {
		long k = count - pos;
		if (n < k) {
			k = n < 0 ? 0 : n;
		}
		
		pos += k;
		return k;
	}
	
	public int available() {
		return count - pos;
	}
	
	public boolean markSupported() {
		return false;
	}
	
	public void reset() {
		pos = 0;
	}

	public void reset(byte[] buf) {
		this.buf = buf;
		this.count = buf.length;

		this.pos = 0;
	}
	
	public void close() throws IOException {
		// do nothing
	}
}
