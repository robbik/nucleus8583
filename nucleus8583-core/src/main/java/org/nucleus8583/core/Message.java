package org.nucleus8583.core;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import org.nucleus8583.core.util.BitmapHelper;

import rk.commons.util.BinaryUtils;
import rk.commons.util.ObjectUtils;

/**
 * This class represents an ISO-8583 message. You can read, manipulate, and
 * write ISO-8583 message using this class.
 * 
 * @author Robbi Kurniawan
 * 
 */
public final class Message implements Serializable {
	private static final long serialVersionUID = -1503040549193848604L;

	final int count;

	final byte[] bits1To128;

	final byte[] bits129To192;

	final Object[] values;

	/**
	 * create a new instance of this class with 192 number of fields defined.
	 * 
	 * same as <code>Message(192)</code>.
	 */
	public Message() {
		count = 193;

		bits1To128 = BitmapHelper.create(128);
		bits129To192 = BitmapHelper.create(64);

		values = new Object[193];
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

		bits1To128 = BitmapHelper.create(128);
		bits129To192 = BitmapHelper.create(64);

		values = new Object[this.count];
	}

	/**
	 * set MTI field value, field number 0 in standard ISO-8583 message, must be
	 * 4 characters length
	 * 
	 * @param mti
	 *            new MTI field value
	 */
	public void setMti(Object mti) {
		if (mti == null) {
			values[0] = "";
		} else {
			values[0] = mti;
		}
	}

	/**
	 * retrieve MTI field value, field number 0 in standard ISO-8583 message
	 * 
	 * @return MTI field value
	 */
	@SuppressWarnings("unchecked")
	public <T> T getMti() {
		return (T) values[0];
	}

	/**
	 * set field value.
	 * 
	 * @param no
	 *            number of field to be set, should in range <code>2-192</code>,
	 *            exclude <code>65<code>.
	 * @param value
	 *            new field value
	 * @throws IllegalArgumentException
	 *             if <code>no</code> less than <code>2</code> or more than
	 *             <code>192</code> or equals to <code>65</code>. otherwise
	 *             <code>true</code>
	 */
	public void set(int no, Object value) {
		if (no == 0) {
			setMti(value);
			return;
		}

		if ((no <= 1) || (no > 192) || (no == 65) || (no >= count)) {
				throw new IllegalArgumentException("field no must be in range 0, 2-"
						+ (count - 1) + " and not equals to 65, actual is " + no);
		}

		if (value == null) {
			unsafeUnset(no);
		} else {
			values[no] = value;

			if (no > 128) {
				BitmapHelper.set(bits129To192, no - 129);
			} else {
				BitmapHelper.set(bits1To128, no - 1);
			}
		}
	}

	/**
	 * set field value.
	 * 
	 * this is unsafe method since no range checking performed so <b>PLEASE USE
	 * THIS METHOD WITH CARE</b>.
	 * 
	 * @param no
	 *            number of field to be set, should in range <code>2-192</code>,
	 *            exclude <code>65<code>.
	 * @param value
	 *            new field value
	 */
	public void unsafeSet(int no, Object value) {
		values[no] = value;

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
	 *             <code>192</code> or equals to <code>65</code>, otherwise
	 *             <code>true</code>
	 */
	public void unset(int no) {
		if ((no <= 1) || (no > 192) || (no == 65) || (no >= count)) {
			throw new IllegalArgumentException("field no must be in range 2-"
					+ (count - 1) + " and not equals to 65");
		}

		if (no > 128) {
			BitmapHelper.clear(bits129To192, no - 129);
		} else {
			BitmapHelper.clear(bits1To128, no - 1);
		}

		values[no] = null;
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
			BitmapHelper.clear(bits129To192, no - 129);
		} else {
			BitmapHelper.clear(bits1To128, no - 1);
		}

		values[no] = null;
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
	 *             <code>192</code> or equals to <code>65</code>, otherwise the
	 *             field value.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(int no) {
		if (no == 0) {
			return getMti();
		}

		if ((no <= 1) || (no > 192) || (no == 65) || (no >= count)) {
			throw new IllegalArgumentException("field no must be in range 2-"
					+ (count - 1) + " and not equals to 65");
		}

		return (T) values[no];
	}

	/**
	 * retrieve data element field value.
	 * 
	 * this is unsafe method since no range checking performed so <b>PLEASE USE
	 * THIS METHOD WITH CARE</b>.
	 * 
	 * @param no
	 *            number of field to be get, should in range <code>2-192</code>,
	 *            exclude <code>65<code>.
	 * @return <code>null<code> if the field has not been set. otherwise the
	 *         field value.
	 */
	@SuppressWarnings("unchecked")
	public <T> T unsafeGet(int no) {
		return (T) values[no];
	}

	/**
	 * clear all fields value
	 */
	public void clear() {
		Arrays.fill(values, null);

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
		Object mti = values[0];

		if (mti != null) {
			map.put(Integer.valueOf(0), mti);
		}

		for (int i = 2, iMin1 = 1, iMin129 = -127; i < count; ++i, ++iMin1, ++iMin129) {
			if (i == 65) {
				// do nothing
			} else if (i < 129) {
				if (BitmapHelper.get(bits1To128, iMin1)) {
					map.put(Integer.valueOf(i), values[i]);
				}
			} else {
				if (BitmapHelper.get(bits129To192, iMin129)) {
					map.put(Integer.valueOf(i), values[i]);
				}
			}
		}
	}

	/**
	 * set MTI to become a response one if and only-if the MTI is a request MTI
	 */
	public void setResponseMti() {
		char[] chars = ((String) getMti()).toCharArray();

		int num = Character.getNumericValue(chars[2]);
		if ((num & 0x01) == 0x00) {
			chars[2] = (char) (num + '1');
			setMti(new String(chars));
		}
	}

	/**
	 * check whether the MTI is a request MTI.
	 * 
	 * @return <code>true</code> if the MTI is a request MTI, otherwise
	 *         <code>false</code>
	 */
	public boolean isRequest() {
		char[] chars = ((String) getMti()).toCharArray();

		return (Character.getNumericValue(chars[2]) & 0x01) == 0x00;
	}

	/**
	 * check whether the MTI is a response MTI.
	 * 
	 * @return <code>true</code> if the MTI is a response MTI, otherwise
	 *         <code>false</code>
	 */
	public boolean isResponse() {
		return !isRequest();
	}

	/*
	 * (non-Javadoc)
	 * 
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
		if (!ObjectUtils.equals(values[0], another.values[0])) {
			return false;
		}

		if (count != another.count) {
			return false;
		}

		for (int i = count - 1; i >= 2; --i) {
			if (i != 65) {
				Object myValue = values[i];
				Object anotherValue = another.values[i];

				if ((myValue instanceof byte[])
						&& (anotherValue instanceof byte[])) {
					return ObjectUtils.equals((byte[]) myValue,
							(byte[]) anotherValue);
				}

				if (!ObjectUtils.equals(values[i], another.values[i])) {
					return false;
				}
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
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

		if (values[0] != null) {
			sbuf.append("    <iso-field id=\"0\" value=\"");
			sbuf.append(values[0]);
			sbuf.append("\" />\n");
		}

		for (int i = 2, iMin1 = 1, iMin129 = -127; i < count; ++i, ++iMin1, ++iMin129) {
			if (i == 65) {
				// do nothing
			} else if (i < 129) {
				if (BitmapHelper.get(bits1To128, iMin1)) {
					sbuf.append("    <iso-field id=\"");
					sbuf.append(i);
					sbuf.append("\" value=\"");

					Object val = values[i];

					if (val instanceof byte[]) {
						sbuf.append(BinaryUtils.toHex((byte[]) val));
					} else {
						sbuf.append(values[i]);
					}

					sbuf.append("\" />\n");
				}
			} else {
				if (BitmapHelper.get(bits129To192, iMin129)) {
					sbuf.append("    <iso-field id=\"");
					sbuf.append(i);
					sbuf.append("\" value=\"");

					Object val = values[i];

					if (val instanceof byte[]) {
						sbuf.append(BinaryUtils.toHex((byte[]) val));
					} else {
						sbuf.append(values[i]);
					}

					sbuf.append("\" />\n");
				}
			}
		}

		sbuf.append("</iso-message>\n");

		return sbuf.toString();
	}
}
