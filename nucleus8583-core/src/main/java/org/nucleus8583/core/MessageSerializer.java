package org.nucleus8583.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.nucleus8583.core.field.DefaultFields;
import org.nucleus8583.core.field.Field;
import org.nucleus8583.core.field.Type;
import org.nucleus8583.core.util.BitmapHelper;

import rk.commons.util.FastByteArrayInputStream;
import rk.commons.util.FastByteArrayOutputStream;

/**
 * Serialize/deserialize {@link Message} object. This class can be instatiated
 * using {@link XmlContext} object or directly using constructor provided.
 * 
 * @author Robbi Kurniawan
 * 
 */
public final class MessageSerializer {

	private boolean hasMti;

	@SuppressWarnings("rawtypes")
	private Type[] types;

	private int count;

	/**
	 * create a new instance of {@link MessageSerializer} using given
	 * configuration.
	 * 
	 * For example, if you want to load "nucleus8583.xml" from "META-INF"
	 * located in classpath, the location should be
	 * <code>classpath:META-INF/nucleus8583.xml</code>.
	 * 
	 * If you want to load "nucleus8583.xml" from "conf" directory, the location
	 * should be <code>file:conf/nucleus8583.xml</code> or just
	 * <code>conf/nucleus8583.xml</code>.
	 * 
	 * @param name
	 *            of message if there are multiple messages defined in
	 *            configuration file
	 * @param location
	 *            configuration location (in URI)
	 */
	public MessageSerializer(String name, String... locations) {
		this(new XmlContext(locations).getMessageSerializer(name));
	}

	/**
	 * create a new instance of {@link MessageSerializer} using given
	 * configuration.
	 * 
	 * For example, if you want to load "nucleus8583.xml" from "META-INF"
	 * located in classpath, the location should be
	 * <code>classpath:META-INF/nucleus8583.xml</code>.
	 * 
	 * If you want to load "nucleus8583.xml" from "conf" directory, the location
	 * should be <code>file:conf/nucleus8583.xml</code> or just
	 * <code>conf/nucleus8583.xml</code>.
	 * 
	 * @param location
	 *            configuration location (in URI)
	 */
	public MessageSerializer(String location) {
		this(new XmlContext(location).getMessageSerializer());
	}

	@SuppressWarnings("unused")
	private MessageSerializer() {
		// do nothing
	}

	private MessageSerializer(MessageSerializer c) {
		hasMti = c.hasMti;

		types = c.types;

		count = c.count;
	}

	private boolean setIfAbsent(int no, Field value, List<Field> fields,
			int count) {
		for (int i = 0; i < count; ++i) {
			Field def = fields.get(i);

			if (def.getNo() == no) {
				return false;
			}
		}

		fields.add(value);
		return true;
	}

	private void checkDuplicateNo(List<Field> list, int count) {
		Set<Integer> set = new HashSet<Integer>();

		for (int i = 0; i < count; ++i) {
			Integer id = Integer.valueOf(list.get(i).getNo());

			if (set.contains(id)) {
				throw new IllegalArgumentException("duplicate no " + id
						+ " found");
			}

			set.add(id);
		}
	}

	public void setFields(List<Field> fields) {
		count = fields.size();

		// has field #0?
		hasMti = !setIfAbsent(0, DefaultFields.FIELD_0, fields, count);

		// check duplicate numbers
		checkDuplicateNo(fields, count);

		// re-assign new count
		count = fields.size();

		// sort fields by it's id
		Collections.sort(fields, Field.COMPARATOR_ASC);

		// check for skipped fields
		for (int i = hasMti ? 0 : 1; i < count; ++i) {
			if (fields.get(i).getNo() != i) {
				throw new IllegalArgumentException("field #" + i
						+ " is not defined");
			}
		}

		// gather types
		types = new Type[count];

		for (int i = hasMti ? 0 : 1; i < count; ++i) {
			types[i] = fields.get(i).getType();
		}
	}

	/**
	 * read serialized data from buffer and set it's values to given
	 * {@link Message} object
	 * 
	 * @param buf
	 *            The buffer
	 * @param out
	 *            The {@link Message} object
	 * @throws IOException
	 *             thrown if the buffer length is shorter than expected.
	 */
	public void read(byte[] buf, Message out) throws IOException {
		FastByteArrayInputStream in = new FastByteArrayInputStream();
		in.reset(buf);
		
		read(in, out);
	}

	/**
	 * read serialized data from stream and set it's values to given
	 * {@link Message} object
	 * 
	 * @param in
	 *            The stream
	 * @param out
	 *            The {@link Message} object
	 * @throws IOException
	 *             thrown if an IO error occurred while serializing.
	 */
	public void read(InputStream in, Message out) throws IOException {
		byte[] bits1To128 = out.bits1To128;
		byte[] bits129To192 = out.bits129To192;

		int count = out.count;

		if (count > this.count) {
			count = this.count;
		}

		int i = 0;

		try {
			if (hasMti) {
				// read bit-0
				out.setMti(types[0].read(in));
			}

			// read bit-1
			i = 1;
			types[1].readBitmap(in, bits1To128, 0, 8);

			if (BitmapHelper.get(bits1To128, 0)) {
				types[1].readBitmap(in, bits1To128, 8, 8);
			}

			// read bit-i
			i = 2;
			for (int iMin1 = 1, iMin129 = -127; i < count; ++i, ++iMin1, ++iMin129) {
				if (i == 65) {
					if (BitmapHelper.get(bits1To128, 64)) {
						types[i].readBitmap(in, bits129To192, 0, 8);
					}
				} else if (i < 129) {
					if (BitmapHelper.get(bits1To128, iMin1)) {
						out.unsafeSet(i, types[i].read(in));
					}
				} else {
					if (BitmapHelper.get(bits129To192, iMin129)) {
						out.unsafeSet(i, types[i].read(in));
					}
				}
			}
		} catch (IOException ex) {
			throw (IOException) new IOException("unable to read field #" + i)
					.initCause(ex);
		} catch (RuntimeException ex) {
			throw new RuntimeException("unable to read field #" + i, ex);
		}
	}

	/**
	 * serialize {@link Message} object into internal byte buffer and return the
	 * buffer
	 * 
	 * @param msg
	 *            The {@link Message} object
	 */
	public byte[] write(Message msg) {
		FastByteArrayOutputStream out = new FastByteArrayOutputStream();

		try {
			write(msg, out);
		} catch (IOException e) {
			// never been here
		}

		return out.toByteArray();
	}

	/**
	 * serialize {@link Message} object into given stream
	 * 
	 * @param msg
	 *            The {@link Message} object
	 * @param out
	 *            The stream
	 * @throws IOException
	 *             thrown if an IO error occurred while serializing.
	 */
	@SuppressWarnings("unchecked")
	public void write(Message msg, OutputStream out) throws IOException {
		byte[] bits1To128 = msg.bits1To128;
		byte[] bits129To192 = msg.bits129To192;

		Object[] values = msg.values;

		// is bit 1 (secondary bitmap) on?
		boolean bit1IsOn = false;

		if (BitmapHelper.realBytesInUse(bits1To128) > 8) {
			BitmapHelper.set(bits1To128, 0);

			bit1IsOn = true;
		} else {
			BitmapHelper.clear(bits1To128, 0);
		}

		// is bit 65 (tertiary bitmap) on?
		if (BitmapHelper.isEmpty(bits129To192)) {
			BitmapHelper.clear(bits1To128, 64);

			values[65] = null;
		} else {
			if (!bit1IsOn) {
				BitmapHelper.set(bits1To128, 0); // bit 1 must set on

				bit1IsOn = true;
			}

			BitmapHelper.set(bits1To128, 64);

			values[65] = bits129To192;
		}

		int count = msg.count;
		if (count > this.count) {
			count = this.count;
		}

		int i = 0;

		try {
			// write bit 0
			if (hasMti) {
				types[0].write(out, msg.getMti());
			}

			// write bit 1 (primary + secondary bitmap)
			i = 1;
			types[1].writeBitmap(out, bits1To128, 0, bit1IsOn ? 16 : 8);

			// write bit i
			i = 2;
			for (int j = 1; (i < count) && (i < 129); ++i, ++j) {
				if (BitmapHelper.get(bits1To128, j)) {
					if (i == 65) {
						// tertiary bitmap
						types[i].writeBitmap(out, (byte[]) values[i], 0, 8);
					} else {
						types[i].write(out, values[i]);
					}
				}
			}

			i = 129;
			for (int j = 0; i < count; ++i, ++j) {
				if (BitmapHelper.get(bits129To192, j)) {
					types[i].write(out, values[i]);
				}
			}
		} catch (Throwable t) {
			throw new RuntimeException("unable to write field #" + i, t);
		}
	}
}
