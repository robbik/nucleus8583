package org.nucleus8583.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Binary prefixer (Big-Endian)
 */
public class BEBinaryPrefixer {
	
	private int nbytes;

	public BEBinaryPrefixer() {
		// do nothing
	}
	
	public BEBinaryPrefixer(BEBinaryPrefixer o) {
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

	public int readUint(InputStream in) throws IOException {
		byte[] bbuf = new byte[nbytes];
		IOHelper.readFully(in, bbuf, nbytes);

		int value = 0;

		for (int i = 0; i < nbytes; ++i) {
            value = (value << 8) | (bbuf[i] & 0xFF);
		}

		return value;
	}

	public void writeUint(OutputStream out, int value) throws IOException {
		byte[] buf = new byte[nbytes];

		writeUint(buf, 0, value);

		out.write(buf);
	}

	public int readUint(byte[] in, int start) throws IOException {
		int value = 0;

		for (int i = 0, j = start; i < nbytes; ++i, ++j) {
			value = (value << 8) | (in[j] & 0xFF);
		}

		return value;
	}

	public void writeUint(byte[] out, int start, int value) throws IOException {
		if (out.length - start < nbytes) {
			throw new ArrayIndexOutOfBoundsException();
		}

		for (int i = start + nbytes - 1; i >= start; --i) {
			out[i] = (byte) (value & 0xFF);
			value >>= 8;
		}
	}
}

