package org.nucleus8583.core.charset;

import java.io.IOException;
import java.io.InputStream;

public interface CharsetDecoder {

	int read(InputStream in) throws IOException;

	int read(InputStream in, char[] cbuf, int off, int len) throws IOException;

}
