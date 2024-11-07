package org.nucleus8583.core.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

	public int readUint(InputStream in) throws IOException {
		byte[] bbuf = new byte[nbytes];
		IOHelper.readFully(in, bbuf, nbytes);

		return readUint(bbuf, 0);
	}

	public void writeUint(OutputStream out, int value) throws IOException {
		byte[] buf = new byte[nbytes];

		writeUint(buf, 0, value);

		out.write(buf);
	}

	public int readUint(byte[] in, int start) throws IOException {
		if (in.length - start < nbytes) {
			throw new EOFException();
		}

		int value = (in[start] & 0xFF);
		int shlv = 8;

		for (int i = 1, j = start + 1; i < nbytes; ++i, ++j) {
			value |= (in[j] & 0xFF) << shlv;
			shlv <<= 1;
		}

		return value;
	}

	public void writeUint(byte[] out, int start, int value) throws IOException {
		if (out.length - start < nbytes) {
			throw new ArrayIndexOutOfBoundsException();
		}

		for (int i = 0, j = start; i < nbytes; ++i, ++j) {
			out[j] = (byte) (value & 0xFF);
			value >>= 8;
		}
	}
}

