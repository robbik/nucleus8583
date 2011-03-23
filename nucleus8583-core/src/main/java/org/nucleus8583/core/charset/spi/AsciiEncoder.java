package org.nucleus8583.core.charset.spi;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class AsciiEncoder extends Writer {
	private final OutputStream out;

	public AsciiEncoder(OutputStream out) {
		this.out = out;
	}

	public void close() throws IOException {
		out.close();
	}

	public void flush() throws IOException {
		out.flush();
	}

	@Override
	public void write(String str) throws IOException {
		int len = str.length();
		for (int i = 0; i < len; ++i) {
			out.write(str.charAt(i) & 0x7F);
		}
	}

	@Override
	public void write(String str, int off, int len) throws IOException {
		if (off == 0) {
			for (int i = 0; i < len; ++i) {
				out.write(str.charAt(i) & 0x7F);
			}
		} else {
			for (int i = 0, j = off; i < len; ++i) {
				out.write(str.charAt(j++) & 0x7F);
			}
		}
	}

	@Override
	public void write(int ichar) throws IOException {
		out.write(ichar & 0x7F);
	}

	@Override
	public void write(char[] cbuf) throws IOException {
		int len = cbuf.length;
		for (int i = 0; i < len; ++i) {
			out.write(cbuf[i] & 0x7F);
		}
	}

	public void write(char[] cbuf, int off, int len) throws IOException {
		if (off == 0) {
			for (int i = 0; i < len; ++i) {
				out.write(cbuf[i] & 0x7F);
			}
		} else {
			for (int i = 0, j = off; i < len; ++i, ++j) {
				out.write(cbuf[j] & 0x7F);
			}
		}
	}
}
