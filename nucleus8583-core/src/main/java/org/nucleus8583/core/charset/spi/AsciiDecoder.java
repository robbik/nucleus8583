package org.nucleus8583.core.charset.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class AsciiDecoder extends Reader {
	private final InputStream in;

	public AsciiDecoder(InputStream in) {
		this.in = in;
	}

	public void close() throws IOException {
		in.close();
	}

	@Override
	public int read() throws IOException {
		int ubyte = in.read();
		if (ubyte < 0) {
			return -1;
		}

		return ubyte & 0x7F;
	}

	public int read(char[] cbuf, int off, int len) throws IOException {
		int ubyte;
		int total = 0;

		for (int i = 0, j = off; i < len; ++i, ++j) {
			ubyte = in.read();
			if (ubyte < 0) {
				if (total == 0) {
					total = -1;
				}

				break;
			}

			cbuf[j] = (char) (ubyte & 0x7F);
			++total;
		}

		return total;
	}
}
