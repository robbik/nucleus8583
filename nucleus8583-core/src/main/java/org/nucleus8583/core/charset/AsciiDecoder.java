package org.nucleus8583.core.charset;

import java.io.IOException;
import java.io.InputStream;

public class AsciiDecoder implements CharsetDecoder {

	public int read(InputStream in) throws IOException {
		int ubyte = in.read();
		if (ubyte < 0) {
			return -1;
		}

		return ubyte & 0x7F;
	}

	public int read(InputStream in, char[] cbuf, int off, int len) throws IOException {
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
