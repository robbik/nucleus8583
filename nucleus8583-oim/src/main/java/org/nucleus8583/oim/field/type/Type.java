package org.nucleus8583.oim.field.type;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public interface Type extends Cloneable {
	
	boolean supportWriter();
	
	boolean supportOutputStream();

	/**
	 * read from input source and return the converted value.
	 * 
	 * @param in
	 *            input source. can be {@link InputStream} or {@link Reader}
	 * @return converted read value
	 * @throws Exception
	 */
	Object read(InputStream in) throws Exception;

	Object read(Reader in) throws Exception;

	/**
	 * convert value and write it to output.
	 * 
	 * @param out
	 *            output. can be {@link OutputStream} or {@link Writer}
	 * @param value
	 *            convert value
	 * @throws Exception
	 */
	void write(OutputStream out, Object value) throws Exception;

	void write(Writer out, Object value) throws Exception;

	Type clone() throws CloneNotSupportedException;
}
