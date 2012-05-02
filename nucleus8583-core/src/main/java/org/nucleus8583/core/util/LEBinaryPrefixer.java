package org.nucleus8583.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import rk.commons.util.IOUtils;

/**
 * Binary prefixer (Little-Endian)
 */
public class LEBinaryPrefixer {
	private int nbytes;

	public LEBinaryPrefixer() {
		// do nothing
	}
	
	public LEBinaryPrefixer(LEBinaryPrefixer o) {
		nbytes = o.nbytes;
	}
	
	public void setPrefixLength(int prefixLength) {
        int nbytes = 1;

        while (prefixLength > 0xFF) {
            prefixLength >>= 8;
            ++nbytes;
        }

        this.nbytes = nbytes;
	}

	public void writeUint(OutputStream out, int value) throws IOException {
		byte[] buf = new byte[nbytes];

		for (int i = 0; i < nbytes; ++i) {
			buf[i] = (byte) (value & 0xFF);
			value >>= 8;
		}

		out.write(buf);
	}

	public int readUint(InputStream in) throws IOException {
		byte[] bbuf = new byte[nbytes];
		IOUtils.readFully(in, bbuf, nbytes);

		int value = (bbuf[0] & 0xFF);
        int shlv = 8;

		for (int i = 1; i < nbytes; ++i) {
            value |= (bbuf[i] & 0xFF) << shlv;
            shlv <<= 1;
		}

		return value;
	}
}

