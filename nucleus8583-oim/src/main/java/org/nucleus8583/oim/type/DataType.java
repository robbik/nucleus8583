package org.nucleus8583.oim.type;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

public interface DataType {
	
	Object read(ByteArrayInputStream in) throws Exception;

	Object read(StringReader reader) throws Exception;

	void write(ByteArrayOutputStream out, Object data) throws Exception;

	void write(StringWriter writer, Object data) throws Exception;
}
