package org.nucleus8583.core.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Serializer<T> {

	T read(InputStream in) throws IOException;
	
	void write(OutputStream out, T value) throws IOException;
	
	void readBitmap(InputStream in, byte[] bitmap, int off, int len) throws IOException;
	
	void writeBitmap(OutputStream out, byte[] bitmap, int off, int len) throws IOException;

}
