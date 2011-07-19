package org.nucleus8583.core.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public abstract class ReaderUtils {

	public static void readFully(Reader reader, char[] cbuf, int length) throws IOException {
		int cbufWriteIndex = 0;
		int remaining = length;

		int nbread;

		while (remaining > 0) {
			nbread = reader.read(cbuf, cbufWriteIndex, remaining);
			if (nbread == -1) {
				throw new EOFException();
			}

			remaining -= nbread;
			cbufWriteIndex += nbread;
		}
	}
}
