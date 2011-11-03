package org.nucleus8583.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BcdPrefixer {

	private int prefixLength;

	public BcdPrefixer(int prefixLength) {
		this.prefixLength = (prefixLength + 1) >> 1;
	}

	public void writeUint(OutputStream out, int value) throws IOException {
	    BcdUtils.writeUint(out, value, prefixLength);
	}

	public int readUint(InputStream in) throws IOException {
	    return BcdUtils.readUint(in, prefixLength);
	}
}
