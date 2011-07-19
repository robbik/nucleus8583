package org.nucleus8583.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Map;

/**
 * This class represents an ISO-8583 message. You can read, manipulate, and
 * write ISO-8583 message using this class.
 *
 * To read ISO-8583 message, you can simply use: <code>
 * InputStream config = ...
 *
 * Iso8583MessageFactory factory = Iso8583MessageFactory.create(config);
 * Iso8583Message msg = factory.createMessage();
 *
 * InputStream raw = ...
 * msg.unpack(raw);
 * </code>
 *
 * To write ISO-8583 message, you can simply use: <code>
 * Iso8583Message msg = ...
 *
 * OutputStream raw = ...
 * msg.pack(raw);
 * </code>
 *
 * @author Robbi Kurniawan
 *
 */
public final class Iso8583Message implements Serializable {
	private static final long serialVersionUID = -1503040549193848604L;

	private final int count;

	private String mti;

	private final String[] stringValues;

	private final BitSet[] binaryValues;

	private final BitSet bits1To128;

	private final BitSet bits129To192;

	private transient Iso8583MessageSerializer serializer;

	/**
	 * create a new instance of this class with 192 number of fields defined.
	 *
	 * same as <code>Iso8583Message(192)</code>.
	 */
	public Iso8583Message() {
		this(192);
	}

	/**
	 * create a new instance of this class by specifying number of fields
	 * defined.
	 *
	 * @param count
	 *            the number of fields.
	 */
	public Iso8583Message(int count) {
		if ((count < 64) || (count > 192)) {
			throw new IllegalArgumentException(
					"number of fields must in range 64-192");
		}

		this.count = count + 1;

		this.mti = "";
		this.stringValues = new String[this.count];
		this.binaryValues = new BitSet[this.count];

		this.bits1To128 = new BitSet(128);
		this.bits129To192 = new BitSet(64);
	}

	/**
	 * set MTI field value, field number 0 in standard ISO-8583 message
	 *
	 * @param mti
	 *            new MTI field value
	 */
	public void setMti(String mti) {
		if (mti == null) {
			mti = "";
		}

		this.mti = mti;
	}

	/**
	 * clear MTI field value, field number 0 in standard ISO-8583 message
	 */
	public void unsetMti() {
		mti = "";
	}

	/**
	 * retrieve MTI field value, field number 0 in standard ISO-8583 message
	 *
	 * @return MTI field value
	 */
	public String getMti() {
		return mti;
	}

	/**
	 * set binary (<code>b</code> data element) field value.
	 *
	 * @param no
	 *            number of field to be set, should in range <code>2-192</code>,
	 *            exclude <code>65<code>.
	 * @param value
	 *            new binary value
	 * @throws IllegalArgumentException
	 *             if <code>no</code> less than <code>2</code> or more than
	 *             <code>192</code> or equals to <code>65</code> or more than
	 *             number of fields defined in configuration used by
	 *             {@link Iso8583MessageFactory} that instantiates this object
	 *             or the field is a string field. otherwise <code>true</code>
	 */
	public void set(int no, BitSet value) {
		if ((no <= 1) || (no > 192) || (no == 65) || (no >= count)) {
			throw new IllegalArgumentException("field no must be in range 2-"
					+ (count - 1) + " and not equals to 65");
		}

		if (value == null) {
			unsafeUnset(no);
		} else {
			binaryValues[no] = value;
			stringValues[no] = null;

			if (no > 128) {
				bits129To192.set(no - 129);
			} else {
				bits1To128.set(no - 1);
			}
		}
	}

	/**
	 * set string-based (non <code>b</code> data element) field value.
	 *
	 * @param no
	 *            number of field to be set, should in range <code>2-192</code>,
	 *            exclude <code>65<code>.
	 * @param value
	 *            new value
	 * @throws IllegalArgumentException
	 *             if <code>no</code> less than <code>2</code> or more than
	 *             <code>192</code> or equals to <code>65</code> or more than
	 *             number of fields defined in configuration used by
	 *             {@link Iso8583MessageFactory} that instantiates this object
	 *             or the field is a binary field. otherwise <code>true</code>
	 */
	public void set(int no, String value) {
		if (no == 0) {
			setMti(value);
			return;
		}

		if ((no <= 1) || (no > 192) || (no == 65) || (no >= count)) {
			throw new IllegalArgumentException("field no must be in range 2-"
					+ (count - 1) + " and not equals to 65");
		}

		if (value == null) {
			unsafeUnset(no);
		} else {
			binaryValues[no] = null;
			stringValues[no] = value;

			if (no > 128) {
				bits129To192.set(no - 129);
			} else {
				bits1To128.set(no - 1);
			}
		}
	}

	/**
	 * set binary (<code>b</code> data element) field value.
	 *
	 * this is unsafe method since no range checking performed so <b>PLEASE USE
	 * THIS METHOD WITH CARE</b>.
	 *
	 * @param no
	 *            number of field to be set, should in range <code>2-192</code>,
	 *            exclude <code>65<code>.
	 * @param value
	 *            new binary value
	 */
	public void unsafeSet(int no, String value) {
		binaryValues[no] = null;
		stringValues[no] = value;

		if (no > 128) {
			bits129To192.set(no - 129);
		} else {
			bits1To128.set(no - 1);
		}
	}

	/**
	 * set string-based (non <code>b</code> data element) field value.
	 *
	 * this is unsafe method since no range checking performed so <b>PLEASE USE
	 * THIS METHOD WITH CARE</b>.
	 *
	 * @param no
	 *            number of field to be set, should in range <code>2-192</code>,
	 *            exclude <code>65<code>.
	 * @param value
	 *            new value
	 */
	public void unsafeSet(int no, BitSet value) {
		binaryValues[no] = value;
		stringValues[no] = null;

		if (no > 128) {
			bits129To192.set(no - 129);
		} else {
			bits1To128.set(no - 1);
		}
	}

	/**
	 * clear field value
	 *
	 * @param no
	 *            number of field to be cleared, should in range
	 *            <code>2-192</code>, exclude <code>65<code>.
	 * @throws IllegalArgumentException
	 *             if <code>no</code> less than <code>2</code> or more than
	 *             <code>192</code> or equals to <code>65</code> or more than
	 *             number of fields defined in configuration used by
	 *             {@link Iso8583MessageFactory} that instantiates this object.
	 *             otherwise <code>true</code>
	 */
	public void unset(int no) {
		if ((no <= 1) || (no > 192) || (no == 65) || (no >= count)) {
			throw new IllegalArgumentException("field no must be in range 2-"
					+ (count - 1) + " and not equals to 65");
		}

		if (no > 128) {
			bits129To192.clear(no - 129);
		} else {
			bits1To128.clear(no - 1);
		}

		// let gc do it's work
		binaryValues[no] = null;
		stringValues[no] = null;
	}

	/**
	 * clear field value.
	 *
	 * this is unsafe method since no range checking performed so <b>PLEASE USE
	 * THIS METHOD WITH CARE</b>.
	 *
	 * @param no
	 *            number of field to be cleared, should in range
	 *            <code>2-192</code>, exclude <code>65<code>.
	 */
	public void unsafeUnset(int no) {
		if (no > 128) {
			bits129To192.clear(no - 129);
		} else {
			bits1To128.clear(no - 1);
		}

		// let gc do it's work
		binaryValues[no] = null;
		stringValues[no] = null;
	}

	/**
	 * retrieve field value
	 *
	 * @param no
	 *            number of field to be get, should in range <code>2-192</code>,
	 *            exclude <code>65<code>.
	 * @return field value or null if the bit has not been set yet.
	 * @throws IllegalArgumentException
	 *             if <code>no</code> less than <code>2</code> or more than
	 *             <code>192</code> or equals to <code>65</code> or more than
	 *             number of fields defined in configuration used by
	 *             {@link Iso8583MessageFactory} that instantiates this object.
	 *             otherwise the field value.
	 */
	public Object get(int no) {
		if (no == 0) {
			return mti;
		}

		if ((no <= 1) || (no > 192) || (no == 65) || (no >= count)) {
			throw new IllegalArgumentException("field no must be in range 2-"
					+ (count - 1) + " and not equals to 65");
		}

		Object value = binaryValues[no];
		if (value != null) {
			return value;
		}

		return stringValues[no];
	}

	/**
	 * retrieve non <code>b</code> data element field value.
	 *
	 * @param no
	 *            number of field to be get, should in range <code>2-192</code>,
	 *            exclude <code>65<code>.
	 * @return field value or null if the bit has not been set yet.
	 * @throws IllegalArgumentException
	 *             if <code>no</code> less than <code>2</code> or more than
	 *             <code>192</code> or equals to <code>65</code> or more than
	 *             number of fields defined in configuration used by
	 *             {@link Iso8583MessageFactory} that instantiates this object
	 *             or the field has binary (<code>b</code> data element) type.
	 *             otherwise the field value or the field is a binary field.
	 */
	public String getString(int no) {
		if (no == 0) {
			return mti;
		}

		if ((no <= 1) || (no > 192) || (no == 65) || (no >= count)) {
			throw new IllegalArgumentException("field no must be in range 2-"
					+ (count - 1) + " and not equals to 65");
		}

		return stringValues[no];
	}

	/**
	 * retrieve <code>b</code> data element field value.
	 *
	 * @param no
	 *            number of field to be get, should in range <code>2-192</code>,
	 *            exclude <code>65<code>.
	 * @return <code>null<code> if <code>no</code> less than <code>2</code> or
	 *         more than <code>192</code> or equals to <code>65</code> or more
	 *         than number of fields defined in configuration used by
	 *         {@link Iso8583MessageFactory} that instantiates this object or
	 *         the field type is not binary (non <code>b</code> data element).
	 *         otherwise the field value.
	 */
	public BitSet getBinary(int no) {
		if ((no <= 1) || (no > 192) || (no == 65) || (no >= count)) {
			throw new IllegalArgumentException("field no must be in range 2-"
					+ (count - 1) + " and not equals to 65");
		}

		return binaryValues[no];
	}

	/**
	 * retrieve non <code>b</code> data element field value.
	 *
	 * this is unsafe method since no range checking performed so <b>PLEASE USE
	 * THIS METHOD WITH CARE</b>.
	 *
	 * @param no
	 *            number of field to be get, should in range <code>2-192</code>,
	 *            exclude <code>65<code>.
	 * @return <code>null<code> if the field has binary (<code>b</code> data
	 *         element) type. otherwise the field value.
	 */
	public String unsafeGetString(int no) {
		return stringValues[no];
	}

	/**
	 * retrieve <code>b</code> data element field value.
	 *
	 * this is unsafe method since no range checking performed so <b>PLEASE USE
	 * THIS METHOD WITH CARE</b>.
	 *
	 * @param no
	 *            number of field to be get, should in range <code>2-192</code>,
	 *            exclude <code>65<code>.
	 * @return <code>null<code> if the field type is not binary (non <code>b
	 *         </code> data element). otherwise the field value.
	 */
	public BitSet unsafeGetBinary(int no) {
		return binaryValues[no];
	}

	/**
	 * clear all fields value
	 */
	public void clear() {
		mti = "";

		Arrays.fill(binaryValues, null);
		Arrays.fill(stringValues, null);

		bits1To128.clear();
		bits129To192.clear();
	}

	/**
	 * convert this object into standard ISO-8583 message and return a byte
	 * array containing the message. This method is slower than
	 * <code>void pack(OutputStream)</code> method.
	 *
	 * @return the byte array containing the ISO-8583 message
	 * @deprecated use {@link Iso8583MessageSerializer} object instead
	 */
	@Deprecated
	public byte[] pack() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			serializer.write(this, baos);
		} catch (IOException e) {
			// should not be here
		}

		return baos.toByteArray();
	}

	/**
	 * convert this object into ISO-8583 message and write to given
	 * {@link OutputStream}. This method is slower than
	 * <code>void pack(Writer)</code> method.
	 *
	 * @param out
	 *            the {@link OutputStream}
	 * @throws IOException
	 *             thrown if an IO error occurred while converting.
	 * @deprecated use {@link Iso8583MessageSerializer} object instead
	 */
	@Deprecated
	public void pack(OutputStream out) throws IOException {
		serializer.write(this, out);
	}

	/**
	 * convert this object into ISO-8583 message and write to given
	 * {@link Writer}.
	 *
	 * @param writer
	 *            the {@link Writer}
	 * @throws IOException
	 *             thrown if an IO error occured while converting.
	 * @deprecated use {@link Iso8583MessageSerializer} object instead
	 */
	@Deprecated
	public void pack(Writer writer) throws IOException {
		serializer.write(this, writer);
	}

	/**
	 * read ISO-8583 message contained in given byte array and assign the fields
	 * value to this object. This method is slower than
	 * <code>void unpack(InputStream)</code> method.
	 *
	 * @param in
	 *            the byte array
	 * @deprecated use {@link Iso8583MessageSerializer} object instead
	 */
	@Deprecated
	public void unpack(byte[] in) {
		try {
			serializer.read(in, this);
		} catch (IOException ex) {
			// should not be here
		}
	}

	/**
	 * read ISO-8583 message contained in given string and assign the fields
	 * value to this object. This method is slower than
	 * <code>void unpack(Reader)</code> method but faster then
	 * <code>void unpack(InputStream)</code> method.
	 *
	 * @param str
	 *            the string
	 * @deprecated use {@link Iso8583MessageSerializer} object instead
	 */
	@Deprecated
	public void unpack(String str) {
		try {
			serializer.read(str, this);
		} catch (IOException ex) {
			// should not be here
		}
	}

	/**
	 * read ISO-8583 message from given {@link InputStream} and assign the
	 * fields value to this object. This method is slower than
	 * <code>void unpack(Reader)</code> method.
	 *
	 * @param in
	 *            the {@link InputStream}
	 * @throws IOException
	 *             thrown if an IO error occurred while converting.
	 * @deprecated use {@link Iso8583MessageSerializer} object instead
	 */
	@Deprecated
	public void unpack(InputStream in) throws IOException {
		serializer.read(in, this);
	}

	/**
	 * read ISO-8583 message from given {@link Reader} and assign the fields
	 * value to this object.
	 *
	 * @param in
	 *            the {@link Reader}
	 * @throws IOException
	 *             thrown if an IO error occurred while converting.
	 * @deprecated use {@link Iso8583MessageSerializer} object instead
	 */
	@Deprecated
	public void unpack(Reader in) throws IOException {
		serializer.read(in, this);
	}

	/**
	 * dump active fields value to a map. The map key is the field number and
	 * the map value is the field value. This method <b>WILL NOT</b> clear the
	 * map first.
	 *
	 * @param map
	 *            the map
	 */
	public void dump(Map<Integer, Object> map) {
		map.put(Integer.valueOf(0), mti);

		for (int i = 2, iMin1 = 1, iMin129 = -127; i < count; ++i, ++iMin1, ++iMin129) {
			if (i == 65) {
				// do nothing
			} else if (i < 129) {
				if (bits1To128.get(iMin1)) {
					Object value = binaryValues[i];

					if (value == null) {
						map.put(Integer.valueOf(i), stringValues[i]);
					} else {
						map.put(Integer.valueOf(i), binaryValues[i]);
					}
				}
			} else {
				if (bits129To192.get(iMin129)) {
					Object value = binaryValues[i];

					if (value == null) {
						map.put(Integer.valueOf(i), stringValues[i]);
					} else {
						map.put(Integer.valueOf(i), binaryValues[i]);
					}
				}
			}
		}
	}

	private boolean equals(Object a, Object b) {
		if (a == b) {
			return true;
		}

		if ((a == null) || (b == null)) {
			return false;
		}

		return a.equals(b);
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}

		if (!(object instanceof Iso8583Message)) {
			return false;
		}

		Iso8583Message another = (Iso8583Message) object;
		if (!equals(this.mti, another.mti)) {
			return false;
		}

		if (this.count != another.count) {
			return false;
		}

		for (int i = this.count - 1; i >= 2; --i) {
			if (i != 65) {
				if (!equals(this.binaryValues[i], another.binaryValues[i])) {
					return false;
				}

				if (!equals(this.stringValues[i], another.stringValues[i])) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sbuf = new StringBuffer();

		sbuf.append("<iso-message>\n");
		sbuf.append("    <iso-field id=\"0\" value=\"");
		sbuf.append(mti);
		sbuf.append("\" />\n");

		for (int i = 2, iMin1 = 1, iMin129 = -127; i < count; ++i, ++iMin1, ++iMin129) {
			if (i == 65) {
				// do nothing
			} else if (i < 129) {
				if (bits1To128.get(iMin1)) {
					sbuf.append("    <iso-field id=\"");
					sbuf.append(i);
					sbuf.append("\" value=\"");

					Object val = binaryValues[i];

					if (val == null) {
						sbuf.append(stringValues[i]);
					} else {
						sbuf.append(val);
					}

					sbuf.append("\" />\n");
				}
			} else {
				if (bits129To192.get(iMin129)) {
					sbuf.append("    <iso-field id=\"");
					sbuf.append(i);
					sbuf.append("\" value=\"");

					Object val = binaryValues[i];

					if (val == null) {
						sbuf.append(stringValues[i]);
					} else {
						sbuf.append(val);
					}

					sbuf.append("\" />\n");
				}
			}
		}

		sbuf.append("</iso-message>\n");

		return sbuf.toString();
	}

	/**
	 * attach this message to a message factory
	 *
	 * @param factory
	 *            the message factory
	 * @deprecated use {@link Iso8583MessageSerializer} object instead
	 */
	@Deprecated
	public void attach(Iso8583MessageFactory factory) {
		serializer = factory.getWrappedObject();
	}

	/**
	 * detach any message factory from this message
	 * @deprecated use {@link Iso8583MessageSerializer} object instead
	 */
	@Deprecated
	public void detach() {
		serializer = null;
	}

	/**
	 * DO NOT use this method directly
	 */
	int size() {
		return count;
	}

	/**
	 * DO NOT use this method directly
	 */
	BitSet directBits1To128() {
		return bits1To128;
	}

	/**
	 * DO NOT use this method directly
	 */
	BitSet directBits129To192() {
		return bits129To192;
	}

	/**
	 * DO NOT use this method directly
	 */
	BitSet[] directBinaryValues() {
		return binaryValues;
	}

	/**
	 * DO NOT use this method directly
	 */
	String[] directStringValues() {
		return stringValues;
	}

	/**
	 * DO NOT use this method directly
	 */
	String directMti() {
		return mti;
	}
}
