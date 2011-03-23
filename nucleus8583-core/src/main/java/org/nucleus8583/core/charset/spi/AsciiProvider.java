package org.nucleus8583.core.charset.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;


public class AsciiProvider implements CharsetProvider {

	public Writer createEncoder(OutputStream out) throws IOException {
		return new AsciiEncoder(out);
	}

	public Reader createDecoder(InputStream in) throws IOException {
		return new AsciiDecoder(in);
	}
}
