package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.nucleus8583.core.xml.FieldAlignments;
import org.nucleus8583.core.xml.FieldDefinition;

public abstract class FieldType implements Serializable {
	private static final long serialVersionUID = -1162912563090715434L;

	protected final int id;

	public FieldType(FieldDefinition def, FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue) {
		this.id = def.getId();
	}

	public int getId() {
		return id;
	}

	public boolean isBinary() {
		return false;
	}

	/**
	 * read N bytes of value from input stream and store it to
	 * <code>value</code> starting from offset <code>off</code>
	 * 
	 * @param in
	 *            input stream
	 * @param value
	 *            where read value to be stored
	 * @param off
	 *            starting offset
	 * @param len
	 *            N bytes
	 * @throws IOException
	 *             if an IO error occured
	 */
	public void read(InputStream in, byte[] value, int off, int len)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	public byte[] readBinary(InputStream in) throws IOException {
		throw new UnsupportedOperationException(" field #" + id + " is not binary field");
	}

	public String readString(InputStream in) throws IOException {
		throw new UnsupportedOperationException(" field #" + id + " is not string field");
	}

	public void write(OutputStream out, String value) throws IOException {
		throw new UnsupportedOperationException(" field #" + id + " is not string field");
	}

	/**
	 * same as <br/>
	 * <code>write(out, value, 0, value.length)</code>
	 * 
	 * @param out
	 * @param value
	 *            value to be written
	 * @throws IOException
	 *             if an IO error occured
	 */
	public void write(OutputStream out, byte[] value) throws IOException {
		throw new UnsupportedOperationException(" field #" + id + " is not binary field");
	}

	/**
	 * write N bytes of <code>value</code> to output stream starting from offset
	 * <code>off</code>
	 * 
	 * @param out
	 *            the output stream
	 * @param value
	 *            value to be written
	 * @param off
	 * @param len
	 * @throws IOException
	 *             if an IO error occured
	 */
	public void write(OutputStream out, byte[] value, int off, int len)
			throws IOException {
		throw new UnsupportedOperationException(" field #" + id + " is not binary field");
	}
}
