package org.nucleus8583.core;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import org.nucleus8583.core.util.BinaryUtils;
import org.nucleus8583.core.util.BitmapHelper;

/**
 * This class represents an ISO-8583 message. You can read, manipulate, and
 * write ISO-8583 message using this class.
 *
 * @author Robbi Kurniawan
 *
 */
public final class Message implements Serializable {
	private static final long serialVersionUID = -1503040549193848604L;

	private final int count;

	private String mti;

	private final String[] stringValues;

	private final byte[][] binaryValues;

	private final byte[] bits1To128;

	private final byte[] bits129To192;

	/**
	 * create a new instance of this class with 192 number of fields defined.
	 *
	 * same as <code>Message(192)</code>.
	 */
	public Message() {
		this(192);
	}

	/**
	 * create a new instance of this class by specifying number of fields
	 * defined.
	 *
	 * @param count
	 *            the number of fields.
	 */
	public Message(int count) {
		if ((count < 64) || (count > 192)) {
			throw new IllegalArgumentException(
					"number of fields must in range 64-192");
		}

		this.count = count + 1;

		this.mti = "";

		this.stringValues = new String[this.count];
		this.binaryValues = new byte[this.count][];

		this.bits1To128 = BitmapHelper.create(128);
		this.bits129To192 = BitmapHelper.create(64);
	}

	/**
	 * set MTI field value, field number 0 in standard ISO-8583 message, must be 4 characters length
	 *
	 * @param mti
	 *            new MTI field value
	 */
	public void setMti(String mti) {
        if ((mti == null) || (mti.length() != 4)) {
            throw new IllegalArgumentException("mti is not valid, must be 4 characters length");
        }

		this.mti = mti;
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
	 *             <code>192</code> or equals to <code>65</code>
	 *             or the field is a string field. otherwise <code>true</code>
	 */
	public void set(int no, byte[] value) {
		if ((no <= 1) || (no > 192) || (no == 65) || (no >= count)) {
			throw new IllegalArgumentException("field no must be in range 2-" + (count - 1) + " and not equals to 65");
		}

		if (value == null) {
			unsafeUnset(no);
		} else {
			binaryValues[no] = value;
			stringValues[no] = null;

			if (no > 128) {
				BitmapHelper.set(bits129To192, no - 129);
			} else {
			    BitmapHelper.set(bits1To128, no - 1);
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
	 *             <code>192</code> or equals to <code>65</code>, otherwise <code>true</code>
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
			    BitmapHelper.set(bits129To192, no - 129);
			} else {
			    BitmapHelper.set(bits1To128, no - 1);
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
		    BitmapHelper.set(bits129To192, no - 129);
		} else {
		    BitmapHelper.set(bits1To128, no - 1);
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
	public void unsafeSet(int no, byte[] value) {
		binaryValues[no] = value;
		stringValues[no] = null;

		if (no > 128) {
		    BitmapHelper.set(bits129To192, no - 129);
		} else {
		    BitmapHelper.set(bits1To128, no - 1);
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
	 *             <code>192</code> or equals to <code>65</code>,
	 *             otherwise <code>true</code>
	 */
	public void unset(int no) {
		if ((no <= 1) || (no > 192) || (no == 65) || (no >= count)) {
			throw new IllegalArgumentException("field no must be in range 2-"
					+ (count - 1) + " and not equals to 65");
		}

		if (no > 128) {
		    BitmapHelper.set(bits129To192, no - 129);
		} else {
		    BitmapHelper.set(bits1To128, no - 1);
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
		    BitmapHelper.set(bits129To192, no - 129);
		} else {
		    BitmapHelper.set(bits1To128, no - 1);
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
	 *             <code>192</code> or equals to <code>65</code>,
	 *             otherwise the field value.
	 */
	public Object get(int no) {
		if (no == 0) {
			return getMti();
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
	 *             <code>192</code> or equals to <code>65</code>
	 *             or the field has binary (<code>b</code> data element) type.
	 *             otherwise the field value or the field is a binary field.
	 */
	public String getString(int no) {
		if (no == 0) {
			return getMti();
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
	 *         more than <code>192</code> or equals to <code>65</code> or
	 *         the field type is not binary (non <code>b</code> data element).
	 *         otherwise the field value.
	 */
	public byte[] getBinary(int no) {
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
	public byte[] unsafeGetBinary(int no) {
		return binaryValues[no];
	}

	/**
	 * clear all fields value
	 */
	public void clear() {
		mti = "";

		Arrays.fill(binaryValues, null);
		Arrays.fill(stringValues, null);

		BitmapHelper.clear(bits1To128);
		BitmapHelper.clear(bits129To192);
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
				if (BitmapHelper.get(bits1To128, iMin1)) {
					Object value = binaryValues[i];

					if (value == null) {
						map.put(Integer.valueOf(i), stringValues[i]);
					} else {
						map.put(Integer.valueOf(i), binaryValues[i]);
					}
				}
			} else {
				if (BitmapHelper.get(bits129To192, iMin129)) {
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

	/**
	 * set MTI to become a response one if and only-if the MTI is a request MTI
	 */
	public void setResponseMti() {
        char[] chars = getMti().toCharArray();

        int num = Character.getNumericValue(chars[2]);
        if ((num & 0x01) == 0x00) {
            chars[2] = (char) (num + '1');
            setMti(new String(chars));
        }
	}

	/**
	 * check whether the MTI is a request MTI.
	 *
	 * @return <code>true</code> if the MTI is a request MTI, otherwise <code>false</code>
	 */
	public boolean isRequest() {
        char[] chars = getMti().toCharArray();

        return (Character.getNumericValue(chars[2]) & 0x01) == 0x00;
	}

    /**
     * check whether the MTI is a response MTI.
     *
     * @return <code>true</code> if the MTI is a response MTI, otherwise <code>false</code>
     */
    public boolean isResponse() {
        return !isRequest();
    }

    private boolean equals(byte[] a, byte[] b) {
        if (a == b) {
            return true;
        }

        if ((a == null) || (b == null)) {
            return false;
        }

        return Arrays.equals(a, b);
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}

		if (!(object instanceof Message)) {
			return false;
		}

		Message another = (Message) object;
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
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
				if (BitmapHelper.get(bits1To128, iMin1)) {
					sbuf.append("    <iso-field id=\"");
					sbuf.append(i);
					sbuf.append("\" value=\"");

					Object val = binaryValues[i];

					if (val == null) {
						sbuf.append(stringValues[i]);
					} else {
						sbuf.append(BinaryUtils.toHex((byte[]) val));
					}

					sbuf.append("\" />\n");
				}
			} else {
				if (BitmapHelper.get(bits129To192, iMin129)) {
					sbuf.append("    <iso-field id=\"");
					sbuf.append(i);
					sbuf.append("\" value=\"");

					Object val = binaryValues[i];

					if (val == null) {
						sbuf.append(stringValues[i]);
					} else {
					    sbuf.append(BinaryUtils.toHex((byte[]) val));
					}

					sbuf.append("\" />\n");
				}
			}
		}

		sbuf.append("</iso-message>\n");

		return sbuf.toString();
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
	byte[] directBits1To128() {
		return bits1To128;
	}

	/**
	 * DO NOT use this method directly
	 */
	byte[] directBits129To192() {
		return bits129To192;
	}

	/**
	 * DO NOT use this method directly
	 */
	byte[][] directBinaryValues() {
		return binaryValues;
	}

	/**
	 * DO NOT use this method directly
	 */
	String[] directStringValues() {
		return stringValues;
	}
}
