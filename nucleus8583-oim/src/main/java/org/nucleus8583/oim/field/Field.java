package org.nucleus8583.oim.field;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.nucleus8583.oim.util.BeanAccessor;

public interface Field {

	int getNo();
	
	boolean supportWriter();
	
	boolean supportOutputStream();

	/**
	 * read from input source and set corresponding value of root.
	 * 
	 * @param in
	 *            input source. can be {@link InputStream} or {@link Reader}
	 * @param ba
	 * @param tmp temporary variables
	 * @throws Exception
	 */
	void read(InputStream in, BeanAccessor ba, Map<String, Object> tmp) throws Exception;
	
	void read(Reader in, BeanAccessor ba, Map<String, Object> tmp) throws Exception;

	/**
	 * get corresponding value from root and write it to output.
	 * 
	 * @param out
	 *            output. can be {@link OutputStream} or {@link Writer}
	 * @param ba
	 * @param tmp temporary variables
	 * @throws Exception
	 */
	void write(OutputStream out, BeanAccessor ba, Map<String, Object> tmp) throws Exception;
	
	void write(Writer out, BeanAccessor ba, Map<String, Object> tmp) throws Exception;
}
