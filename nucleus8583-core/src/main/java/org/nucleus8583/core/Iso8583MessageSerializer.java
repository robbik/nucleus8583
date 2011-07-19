package org.nucleus8583.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.JAXBContext;

import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.charset.CharsetProvider;
import org.nucleus8583.core.charset.Charsets;
import org.nucleus8583.core.field.type.FieldType;
import org.nucleus8583.core.field.type.FieldTypes;
import org.nucleus8583.core.util.ResourceUtils;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;
import org.nucleus8583.core.xml.Iso8583MessageDefinition;
import org.w3c.dom.Node;

/**
 * Serialize/deserialize {@link Iso8583Message} object. Creating this class
 * requires configuration.
 *
 * @author Robbi Kurniawan
 *
 */
public final class Iso8583MessageSerializer {

	private static final JAXBContext ctx;

	private static final Comparator<FieldType> sortByFieldId = new Comparator<FieldType>() {

		public int compare(FieldType a, FieldType b) {
			return a.getId() - b.getId();
		}
	};

	static {
		try {
			ctx = JAXBContext.newInstance(Iso8583MessageDefinition.class);
		} catch (Exception e) {
			StringWriter w = new StringWriter();
			PrintWriter pw = new PrintWriter(w);

			e.printStackTrace(pw);
			pw.flush();

			throw new InternalError(w.toString());
		}
	}

	/**
	 * same as <code>
	 *     return new Iso8583MessageFactory(location);
	 * </code>
	 *
	 * @param location
	 * @return a new instance of Iso8583MessageFactory.
	 */
	public static Iso8583MessageSerializer create(String location) {
		return new Iso8583MessageSerializer(location);
	}

	/**
	 * same as <code>
	 *     return new Iso8583MessageFactory(in);
	 * </code>
	 *
	 * @param in
	 * @return a new instance of Iso8583MessageFactory.
	 */
	public static Iso8583MessageSerializer create(InputStream in) {
		return new Iso8583MessageSerializer(in);
	}

	/**
	 * same as <code>
	 *     return new Iso8583MessageFactory(node);
	 * </code>
	 *
	 * @param node
	 * @return a new instance of Iso8583MessageFactory.
	 */
	public static Iso8583MessageSerializer create(Node node) {
		return new Iso8583MessageSerializer(node);
	}

	private FieldType[] fields;

	private boolean[] binaries;

	private int fieldsCount;

	private String encoding;

	private CharsetEncoder charsetEncoder;

	private CharsetDecoder charsetDecoder;

	/**
	 * create a new instance of {@link Iso8583MessageSerializer} using given
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
	public Iso8583MessageSerializer(String location) {
		URL found = ResourceUtils.getURL(location);
		if (found == null) {
			throw new RuntimeException("unable to find " + location);
		}

		Iso8583MessageDefinition definition;

		try {
			definition = (Iso8583MessageDefinition) ctx.createUnmarshaller()
					.unmarshal(found);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		init(definition);
	}

	/**
	 * create a new instance of {@link Iso8583MessageSerializer} using given
	 * configuration
	 *
	 * @param in
	 *            input stream
	 */
	public Iso8583MessageSerializer(InputStream in) {
		Iso8583MessageDefinition definition;

		try {
			definition = (Iso8583MessageDefinition) ctx.createUnmarshaller()
					.unmarshal(in);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		init(definition);
	}

	/**
	 * create a new instance of {@link Iso8583MessageSerializer} using given
	 * configuration
	 *
	 * @param node
	 *            an DOM node where the entire XML start from
	 */
	public Iso8583MessageSerializer(Node node) {
		Iso8583MessageDefinition definition;

		try {
			definition = (Iso8583MessageDefinition) ctx.createUnmarshaller()
					.unmarshal(node);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		init(definition);
	}

	private void init(Iso8583MessageDefinition definition) {
		List<Iso8583FieldDefinition> fields = definition.getFields();

		this.fieldsCount = fields.size();

		this.fields = new FieldType[this.fieldsCount];
		for (int i = 0; i < fieldsCount; ++i) {
			this.fields[i] = FieldTypes.getType(fields.get(i));
		}

		// sort fields by it's id
		Arrays.sort(this.fields, sortByFieldId);

		// check for skipped fields
		for (int i = 0; i < fieldsCount; ++i) {
			if (this.fields[i].getId() != i) {
				throw new IllegalArgumentException("field #" + i + " is not defined");
			}
		}

		this.binaries = new boolean[fieldsCount];
		for (int i = fieldsCount - 1; i >= 0; --i) {
			this.binaries[i] = this.fields[i].isBinary();
		}

		this.encoding = definition.getEncoding();

		CharsetProvider charsetProvider = Charsets.getProvider(this.encoding);
		if (charsetProvider == null) {
			throw new RuntimeException(new UnsupportedEncodingException(this.encoding));
		}

		charsetEncoder = charsetProvider.getEncoder();
		charsetDecoder = charsetProvider.getDecoder();
	}

	/**
	 * retrieve encoding used by this instance
	 *
	 * @return used encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * read serialized data from buffer and set it's values to given
	 * {@link Iso8583Message} object
	 *
	 * @param buf
	 *            The buffer
	 * @param out
	 *            The {@link Iso8583Message} object
	 * @throws IOException
	 *             thrown if the buffer length is shorter than expected.
	 */
	public void read(byte[] buf, Iso8583Message out) throws IOException {
		read(new ByteArrayInputStream(buf), out);
	}

	/**
	 * read serialized data from stream and set it's values to given
	 * {@link Iso8583Message} object
	 *
	 * @param in
	 *            The stream
	 * @param out
	 *            The {@link Iso8583Message} object
	 * @throws IOException
	 *             thrown if an IO error occurred while serializing.
	 */
	public void read(InputStream in, Iso8583Message out) throws IOException {
		BitSet bits1To128 = out.directBits1To128();
		BitSet bits129To192 = out.directBits129To192();

		int count = out.size();
		if (count > fieldsCount) {
			count = fieldsCount;
		}

		// read bit-0
		out.setMti(fields[0].readString(in, charsetDecoder));

		// read bit-1
		fields[1].read(in, charsetDecoder, bits1To128);

		// read bit-i
		for (int i = 2, iMin1 = 1, iMin129 = -127; i < count; ++i, ++iMin1, ++iMin129) {
			if (i == 65) {
				if (bits1To128.get(64)) {
					fields[i].read(in, charsetDecoder, bits129To192);
				}
			} else if (i < 129) {
				if (bits1To128.get(iMin1)) {
					if (binaries[i]) {
						out.unsafeSet(i, fields[i].readBinary(in, charsetDecoder));
					} else {
						out.unsafeSet(i, fields[i].readString(in, charsetDecoder));
					}
				}
			} else {
				if (bits129To192.get(iMin129)) {
					if (binaries[i]) {
						out.unsafeSet(i, fields[i].readBinary(in, charsetDecoder));
					} else {
						out.unsafeSet(i, fields[i].readString(in, charsetDecoder));
					}
				}
			}
		}
	}

	/**
	 * serialize {@link Iso8583Message} object into given stream
	 *
	 * @param msg
	 *            The {@link Iso8583Message} object
	 * @param out
	 *            The stream
	 * @throws IOException
	 *             thrown if an IO error occurred while serializing.
	 */
	public void write(Iso8583Message msg, OutputStream out) throws IOException {
		BitSet bits1To128 = msg.directBits1To128();
		BitSet bits129To192 = msg.directBits129To192();

		String mti = msg.directMti();

		BitSet[] binaryValues = msg.directBinaryValues();
		String[] stringValues = msg.directStringValues();

		// is bit 65 on?
		if (bits129To192.isEmpty()) {
			bits1To128.clear(64);

			binaryValues[65] = null;
			stringValues[65] = null;
		} else {
			bits1To128.set(64);

			binaryValues[65] = bits129To192;
			stringValues[65] = null;
		}

		// bit 1 is always on
		bits1To128.set(0);

		int count = msg.size();
		if (count > fieldsCount) {
			count = fieldsCount;
		}

		// pack!
		fields[0].write(out, charsetEncoder, mti);
		fields[1].write(out, charsetEncoder, bits1To128);

		for (int i = 2, j = 1; (i < count) && (i < 129); ++i, ++j) {
			if (bits1To128.get(j)) {
				if (binaries[i]) {
					fields[i].write(out, charsetEncoder, binaryValues[i]);
				} else {
					fields[i].write(out, charsetEncoder, stringValues[i]);
				}
			}
		}

		for (int i = 129, j = 0; i < count; ++i, ++j) {
			if (bits129To192.get(j)) {
				if (binaries[i]) {
					fields[i].write(out, charsetEncoder, binaryValues[i]);
				} else {
					fields[i].write(out, charsetEncoder, stringValues[i]);
				}
			}
		}
	}
}
