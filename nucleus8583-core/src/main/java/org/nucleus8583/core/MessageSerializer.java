package org.nucleus8583.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.charset.CharsetProvider;
import org.nucleus8583.core.charset.Charsets;
import org.nucleus8583.core.field.type.FieldType;
import org.nucleus8583.core.field.type.FieldTypes;
import org.nucleus8583.core.util.BitmapHelper;
import org.nucleus8583.core.util.ResourceUtils;
import org.nucleus8583.core.xml.FieldDefinition;
import org.nucleus8583.core.xml.MessageDefinition;
import org.w3c.dom.Node;

/**
 * Serialize/deserialize {@link Message} object. Creating this class
 * requires configuration.
 *
 * @author Robbi Kurniawan
 *
 */
public final class MessageSerializer {

	private static final JAXBContext ctx;

	private static final Schema xsd;

	private static final Comparator<FieldType> sortByFieldId = new Comparator<FieldType>() {

		public int compare(FieldType a, FieldType b) {
			return a.getId() - b.getId();
		}
	};

	static {
		try {
			ctx = JAXBContext.newInstance(MessageDefinition.class);

			xsd = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(
			        ResourceUtils.getURL("classpath:META-INF/nucleus8583/schema/iso-message.xsd"));
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
	 *     return new MessageSerializer(location);
	 * </code>
	 *
	 * @param location
	 * @return a new instance of MessageSerializer.
	 */
	public static MessageSerializer create(String location) {
		return new MessageSerializer(location);
	}

	/**
	 * same as <code>
	 *     return new MessageSerializer(in);
	 * </code>
	 *
	 * @param in
	 * @return a new instance of MessageSerializer.
	 */
	public static MessageSerializer create(InputStream in) {
		return new MessageSerializer(in);
	}

	/**
	 * same as <code>
	 *     return new MessageSerializer(node);
	 * </code>
	 *
	 * @param node
	 * @return a new instance of MessageSerializer.
	 */
	public static MessageSerializer create(Node node) {
		return new MessageSerializer(node);
	}

	private boolean hasMti;

	private FieldType[] fields;

	private boolean[] binaries;

	private int fieldsCount;

	private String encoding;

	private CharsetEncoder charsetEncoder;

	private CharsetDecoder charsetDecoder;

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
		URL found = ResourceUtils.getURL(location);
		if (found == null) {
			throw new RuntimeException("unable to find " + location);
		}

		MessageDefinition definition;

		try {
            Unmarshaller unmarshaller = MessageSerializer.ctx.createUnmarshaller();
            unmarshaller.setSchema(MessageSerializer.xsd);

			definition = (MessageDefinition) unmarshaller.unmarshal(found);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		init(definition);
	}

	/**
	 * create a new instance of {@link MessageSerializer} using given
	 * configuration
	 *
	 * @param in
	 *            input stream
	 */
	public MessageSerializer(InputStream in) {
		MessageDefinition definition;

		try {
            Unmarshaller unmarshaller = MessageSerializer.ctx.createUnmarshaller();
            unmarshaller.setSchema(MessageSerializer.xsd);

			definition = (MessageDefinition) unmarshaller.unmarshal(in);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		init(definition);
	}

	/**
	 * create a new instance of {@link MessageSerializer} using given
	 * configuration
	 *
	 * @param node
	 *            an DOM node where the entire XML start from
	 */
	public MessageSerializer(Node node) {
		MessageDefinition definition;

		try {
		    Unmarshaller unmarshaller = MessageSerializer.ctx.createUnmarshaller();
		    unmarshaller.setSchema(MessageSerializer.xsd);

			definition = (MessageDefinition) unmarshaller.unmarshal(node);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		init(definition);
	}

    private boolean replace(int id, FieldDefinition newdef, List<FieldDefinition> fields, int count) {
        for (int i = 0; i < count; ++i) {
            FieldDefinition def = fields.get(i);

            if (def.getId() == id) {
                fields.set(i, newdef);
                return true;
            }
        }

        return false;
    }

	private void init(MessageDefinition definition) {
		List<FieldDefinition> fields = definition.getFields();

		int count = fields.size();

		// automatic override field no 0 (if any)
        hasMti = replace(0, FieldDefinition.FIELD_0, fields, count);
        if (!hasMti) {
		    fields.add(FieldDefinition.FIELD_0);
		}

        // automatic set field no 1
		if (!replace(1, FieldDefinition.FIELD_1, fields, count)) {
		    fields.add(FieldDefinition.FIELD_1);
		}

        // automatic set field no 65
        if (!replace(65, FieldDefinition.FIELD_65, fields, count)) {
            fields.add(FieldDefinition.FIELD_65);
        }

        this.fieldsCount = fields.size();
        this.fields = new FieldType[this.fieldsCount];

        for (int i = 0; i < fieldsCount; ++i) {
            this.fields[i] = FieldTypes.getType(fields.get(i));
        }

		// sort fields by it's id
		Arrays.sort(this.fields, MessageSerializer.sortByFieldId);

		// check for skipped fields
		for (int i = 1; i < fieldsCount; ++i) {
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
		read(new ByteArrayInputStream(buf), out);
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
		byte[] bits1To128 = out.directBits1To128();
		byte[] bits129To192 = out.directBits129To192();

		int count = out.size();
		if (count > fieldsCount) {
			count = fieldsCount;
		}

		if (hasMti) {
    		// read bit-0
    		out.setMti(fields[0].readString(in, charsetDecoder));
		}

		// read bit-1
		fields[1].read(in, charsetDecoder, bits1To128, 0, 8);

		if (BitmapHelper.get(bits1To128, 0)) {
		    fields[1].read(in, charsetDecoder, bits1To128, 8, 8);
		}

		// read bit-i
		for (int i = 2, iMin1 = 1, iMin129 = -127; i < count; ++i, ++iMin1, ++iMin129) {
			if (i == 65) {
				if (BitmapHelper.get(bits1To128, 64)) {
					fields[i].read(in, charsetDecoder, bits129To192, 0, 8);
				}
			} else if (i < 129) {
				if (BitmapHelper.get(bits1To128, iMin1)) {
					if (binaries[i]) {
						out.unsafeSet(i, fields[i].readBinary(in, charsetDecoder));
					} else {
						out.unsafeSet(i, fields[i].readString(in, charsetDecoder));
					}
				}
			} else {
				if (BitmapHelper.get(bits129To192, iMin129)) {
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
     * serialize {@link Message} object into internal byte buffer and return the buffer
     *
     * @param msg
     *            The {@link Message} object
     */
    public byte[] write(Message msg) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

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
	    byte[] bits1To128 = msg.directBits1To128();
	    byte[] bits129To192 = msg.directBits129To192();

		String mti = msg.getMti();

		byte[][] binaryValues = msg.directBinaryValues();
		String[] stringValues = msg.directStringValues();

        // is bit 1 on?
		boolean bit1IsOn = false;

		if (BitmapHelper.realBytesInUse(bits1To128) > 8) {
		    BitmapHelper.set(bits1To128, 0);
		    bit1IsOn = true;
		} else {
		    BitmapHelper.clear(bits1To128, 0);
		}

		// is bit 65 on?
		if (BitmapHelper.isEmpty(bits129To192)) {
		    BitmapHelper.clear(bits1To128, 64);

			binaryValues[65] = null;
			stringValues[65] = null;
		} else {
		    if (!bit1IsOn) {
		        BitmapHelper.set(bits1To128, 0); // bit 1 must be on
		        bit1IsOn = true;
		    }
		    BitmapHelper.set(bits1To128, 64);

			binaryValues[65] = bits129To192;
			stringValues[65] = null;
		}

		int count = msg.size();
		if (count > fieldsCount) {
			count = fieldsCount;
		}

		// pack!
		if (hasMti) {
		    fields[0].write(out, charsetEncoder, mti);
		}

		fields[1].write(out, charsetEncoder, bits1To128, 0, bit1IsOn ? 16 : 8);

		for (int i = 2, j = 1; (i < count) && (i < 129); ++i, ++j) {
			if (BitmapHelper.get(bits1To128, j)) {
			    if (i == 65) {
			        fields[i].write(out, charsetEncoder, binaryValues[i], 0, 8);
			    } else {
    				if (binaries[i]) {
    					fields[i].write(out, charsetEncoder, binaryValues[i]);
    				} else {
    					fields[i].write(out, charsetEncoder, stringValues[i]);
    				}
			    }
			}
		}

		for (int i = 129, j = 0; i < count; ++i, ++j) {
			if (BitmapHelper.get(bits129To192, j)) {
				if (binaries[i]) {
					fields[i].write(out, charsetEncoder, binaryValues[i]);
				} else {
					fields[i].write(out, charsetEncoder, stringValues[i]);
				}
			}
		}
	}
}
