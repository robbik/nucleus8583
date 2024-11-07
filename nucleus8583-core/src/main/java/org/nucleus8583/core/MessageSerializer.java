package org.nucleus8583.core;

import org.nucleus8583.core.config.Configuration;
import org.nucleus8583.core.config.DefaultFields;
import org.nucleus8583.core.config.Field;
import org.nucleus8583.core.type.Serializer;
import org.nucleus8583.core.type.Type;
import org.nucleus8583.core.type.TypeBuilder;
import org.nucleus8583.core.util.BitmapHelper;
import org.nucleus8583.core.util.FastByteArrayInputStream;
import org.nucleus8583.core.util.FastByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

@SuppressWarnings({"unchecked", "raw", "rawtypes"})
public class MessageSerializer {

	protected final Configuration configuration;

	protected final boolean hasMti;

	protected final Field[] fields;

	protected final Serializer[] serializers;

	protected final int count;

	protected MessageSerializer(Builder b) {
		configuration = b.configuration;

		final List<Field> fields = new ArrayList<>(b.fields.values());

		count = fields.size();

		// has field #0?
		hasMti = !addIfAbsent(0, DefaultFields.FIELD_0, fields, count);

		fields.sort(Field.COMPARATOR_ASC);

		// check for skipped fields
		for (int i = hasMti ? 0 : 1; i < count; ++i) {
			if (fields.get(i).no() != i) {
				throw new IllegalArgumentException("field #" + i + " is not defined");
			}
		}

		// gather types
		this.fields = new Field[count];
		this.serializers = new Serializer[count];

		for (int i = hasMti ? 0 : 1; i < count; ++i) {
			this.fields[i] = fields.get(i);
			this.serializers[i] = this.fields[i].type().serializer();
		}
	}

	public static Builder custom() {
		return new Builder();
	}

	public static Builder custom(Configuration configuration) {
		return new Builder(configuration);
	}

	public static MessageSerializer get(String name) {
		return get(Configuration.DEFAULT, name);
	}

	public static MessageSerializer get(Configuration configuration, String name) {
		return configuration.getSerializer(name);
	}

	public Builder copy() {
		return new Builder(this);
	}

	private static boolean addIfAbsent(int no, Field value, List<Field> fields, int count) {
		for (int i = 0; i < count; ++i) {
			final Field field = fields.get(i);

			if (field.no() == no) {
				return false;
			}
		}

		fields.add(value);
		return true;
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
				out.mti(serializers[0].read(in));
			}

			// read bit-1
			i = 1;
			serializers[1].readBitmap(in, bits1To128, 0, 8);

			if (BitmapHelper.get(bits1To128, 0)) {
				serializers[1].readBitmap(in, bits1To128, 8, 8);
			}

			// read bit-i
			i = 2;
			for (int iMin1 = 1, iMin129 = -127; i < count; ++i, ++iMin1, ++iMin129) {
				if (i == 65) {
					if (BitmapHelper.get(bits1To128, 64)) {
						serializers[i].readBitmap(in, bits129To192, 0, 8);
					}
				} else if (i < 129) {
					if (BitmapHelper.get(bits1To128, iMin1)) {
						out.unsafeSet(i, serializers[i].read(in));
					}
				} else {
					if (BitmapHelper.get(bits129To192, iMin129)) {
						out.unsafeSet(i, serializers[i].read(in));
					}
				}
			}
		} catch (IOException ex) {
			throw new IOException("unable to read field #" + i, ex);
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
				serializers[0].write(out, msg.mti());
			}

			// write bit 1 (primary + secondary bitmap)
			i = 1;
			serializers[1].writeBitmap(out, bits1To128, 0, bit1IsOn ? 16 : 8);

			// write bit i
			i = 2;
			for (int j = 1; (i < count) && (i < 129); ++i, ++j) {
				if (BitmapHelper.get(bits1To128, j)) {
					if (i == 65) {
						// tertiary bitmap
						serializers[i].writeBitmap(out, (byte[]) values[i], 0, 8);
					} else {
						serializers[i].write(out, values[i]);
					}
				}
			}

			i = 129;
			for (int j = 0; i < count; ++i, ++j) {
				if (BitmapHelper.get(bits129To192, j)) {
					serializers[i].write(out, values[i]);
				}
			}
		} catch (Throwable t) {
			throw new RuntimeException("unable to write field #" + i, t);
		}
	}

	public static class Builder {

		protected final Configuration configuration;

		protected final Map<Integer, Field> fields;

		public Builder() {
			this(Configuration.DEFAULT);
		}

		public Builder(Configuration configuration) {
			this.configuration = configuration;

			this.fields = new TreeMap<>();
		}

		public Builder(MessageSerializer o) {
			configuration = o.configuration;

			fields = new TreeMap<>();
			for (Field field : o.fields) {
				fields.put(field.no(), field);
			}
		}

		public Builder defineField(int no, String typeName) {
			return defineField(no, typeName, (Map<String, String>) null);
		}

		public Builder defineField(int no, String typeName, Map<String, String> properties) {
			Properties p = new Properties();

			if ((properties != null) && !properties.isEmpty()) {
                p.putAll(properties);
			}

			return defineField(no, typeName, p);
		}

		public Builder defineField(int no, String typeName, Properties properties) {
			final TypeBuilder<?> tb = configuration.getType(typeName);
			if (tb == null) {
				throw new IllegalArgumentException("unknown type " + typeName);
			}

			final Type<?> type = tb.copy().with(properties).build();

			fields.put(no, new Field(no, type));

			return this;
		}

		public MessageSerializer build() {
			return new MessageSerializer(this);
		}
	}
}
