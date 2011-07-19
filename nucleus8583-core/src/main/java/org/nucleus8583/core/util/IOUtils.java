package org.nucleus8583.core.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.nucleus8583.core.charset.CharsetDecoder;

public abstract class IOUtils {

	public static void readFully(InputStream in, byte[] buf, int length) throws IOException {
		int offset = 0;
		int remaining = length;

		int nbread;

		while (remaining > 0) {
			nbread = in.read(buf, offset, remaining);
			if (nbread == -1) {
				throw new EOFException();
			}

			remaining -= nbread;
			offset += nbread;
		}
	}

	public static void readFully(InputStream in, CharsetDecoder dec, char[] cbuf, int length) throws IOException {
		int offset = 0;
		int remaining = length;

		int nbread;

		while (remaining > 0) {
			nbread = dec.read(in, cbuf, offset, remaining);
			if (nbread == -1) {
				throw new EOFException();
			}

			remaining -= nbread;
			offset += nbread;
		}
	}
}
