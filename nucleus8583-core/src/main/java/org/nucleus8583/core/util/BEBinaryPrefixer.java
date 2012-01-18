package org.nucleus8583.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Binary prefixer (Big-Endian)
 */
public class BEBinaryPrefixer {
	private final int nbytes;

	public BEBinaryPrefixer(int prefixLength) {
        int nbytes = 1;

        while (prefixLength > 0xFF) {
            prefixlength >>= 8;
            ++nbytes;
        }

        this.nbytes = nbytes;
	}

	public void writeUint(OutputStream out, int value) throws IOException {
		byte[] buf = new byte[nbytes];

		for (int i = nbytes - 1; i >= 0; --i) {
			buf[i] = (byte) (value & 0xFF);
			value >>= 8;
		}

		out.write(buf);
	}

	public int readUint(InputStream in) throws IOException {
		byte[] bbuf = new byte[nbytes];
		IOUtils.readFully(in, bbuf, nbytes);

		int value = 0;

		for (int i = 0; i < nbytes; ++i) {
            value = (value << 8) | (bbuf[i] & 0xFF);
		}

		return value;
	}
}

