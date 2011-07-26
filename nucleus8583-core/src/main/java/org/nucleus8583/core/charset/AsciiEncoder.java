package org.nucleus8583.core.charset;

import java.io.IOException;
import java.io.OutputStream;

public class AsciiEncoder implements CharsetEncoder {

	public void write(OutputStream out, String str) throws IOException {
		int len = str.length();
		for (int i = 0; i < len; ++i) {
			out.write(str.charAt(i) & 0x7F);
		}
	}

	public void write(OutputStream out, String str, int off, int len) throws IOException {
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

	public void write(OutputStream out, int ichar) throws IOException {
		out.write(ichar & 0x7F);
	}

	public void write(OutputStream out, char[] cbuf) throws IOException {
		int len = cbuf.length;
		for (int i = 0; i < len; ++i) {
			out.write(cbuf[i] & 0x7F);
		}
	}

	public void write(OutputStream out, char[] cbuf, int off, int len) throws IOException {
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
