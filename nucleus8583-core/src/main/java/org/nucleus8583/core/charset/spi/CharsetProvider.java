package org.nucleus8583.core.charset.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public interface CharsetProvider {
	Reader createDecoder(InputStream in) throws IOException;

	Writer createEncoder(OutputStream out) throws IOException;
}
