package org.nucleus8583.oim.processor;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Map;

import org.nucleus8583.oim.converter.BinaryConverter;
import org.nucleus8583.oim.converter.TypeConverter;
import org.nucleus8583.oim.util.ElExpression;

public final class BasicComponent extends BaseComponent {
	private final int no;

	private final String name;

	private final Field field;

	private final ElExpression generatedValue;

	private final TypeConverter converter;

	private final boolean binary;

	private final char align;

	private final char padWith;

	private final String padder;

	private final char[] cpadder;

	private final int length;

	public BasicComponent(HasFieldsComponent parent, int no, String name,
			TypeConverter converter, char align, char padWith, int length,
			ElExpression generatedValue) {
		this.no = no;
		this.name = name;

		this.field = name == null ? null : parent.getField(name);

		this.converter = converter;
		this.binary = BinaryConverter.class.isInstance(converter);

		this.align = align;
		this.padWith = padWith;
		this.length = length;

		this.generatedValue = generatedValue;

		this.cpadder = new char[length];
		Arrays.fill(this.cpadder, padWith);

		this.padder = new String(this.cpadder);
	}

	private void read(Reader reader, char[] cbuf, int length)
			throws IOException {
		int cidx = 0;
		int crem = length;

		int nbread;

		while (crem > 0) {
			nbread = reader.read(cbuf, cidx, crem);
			if (nbread == -1) {
				throw new EOFException(
						"EOF detected while reading from stream, read so far = "
								+ cidx + ", remaining = " + crem + " [name="
								+ name + "]");
			}

			crem -= nbread;
			cidx += nbread;
		}
	}

	private void skip(Reader reader, int length) throws IOException {
		int crem = length;

		while (crem > 0) {
			crem -= (int) reader.skip(crem);
		}
	}

	private Object getValueFromPojo(Object pojo) {
		Object value;

		if (field == null) {
			if (generatedValue != null) {
				value = generatedValue.eval(pojo);
			} else {
				value = null;
			}
		} else {
			try {
				value = field.get(pojo);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		return value;
	}

	private void setValueToPojo(Object pojo, Object value) {
		if (field != null) {
			try {
				field.set(pojo, value);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public int getNo() {
		return no;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean isBinary() {
		return binary;
	}

	@Override
	public void encode(StringBuilder sb, Object pojo,
			Map<String, Object> session) {
		if (binary) {
			throw new UnsupportedOperationException();
		}

		String value = converter.convertToIsoString(getValueFromPojo(pojo));
		if (value == null) {
			return;
		}

		int vlen = value.length();
		if (vlen > length) {
			throw new IllegalArgumentException("value too long, expected "
					+ length + " but actual is " + vlen);
		}

		if (vlen == 0) {
			sb.append(cpadder);
			return;
		}

		if (vlen == length) {
			sb.append(value);
			return;
		}

		switch (align) {
		case 'l':
			sb.append(value);
			sb.append(cpadder, 0, length - vlen);
			break;
		case 'r':
			sb.append(cpadder, 0, length - vlen);
			sb.append(value);
			break;
		default: // 'n'
			sb.append(value);
			sb.append(cpadder, 0, length - vlen);
			break;
		}
	}

	@Override
	public String encodeToString(Object pojo, Map<String, Object> session) {
		if (binary) {
			throw new UnsupportedOperationException();
		}

		String value = converter.convertToIsoString(getValueFromPojo(pojo));
		if (value == null) {
			return null;
		}

		int vlen = value.length();
		if (vlen > length) {
			throw new IllegalArgumentException("value too long, expected "
					+ length + " but actual is " + vlen);
		}

		if (vlen == 0) {
			return padder;
		}

		if (vlen == length) {
			return value;
		}

		switch (align) {
		case 'l':
			return value + padder.substring(0, length - vlen).intern();
		case 'r':
			return padder.substring(0, length - vlen).intern() + value;
		default:
			return value + padder.substring(0, length - vlen).intern();
		}
	}

	@Override
	public BitSet encodeToBinary(Object pojo, Map<String, Object> session) {
		if (!binary) {
			return null;
		}

		return converter.convertToIsoBinary(getValueFromPojo(pojo));
	}

	@Override
	public void decode(BitSet value, Object pojo, Map<String, Object> session) {
		if (binary) {
			throw new UnsupportedOperationException();
		}

		if (field != null) {
			setValueToPojo(pojo, value);
		}
	}

	@Override
	public BitSet decode(BitSet value, Map<String, Object> session) {
		return value;
	}

	@Override
	public void decode(String value, Object pojo, Map<String, Object> session) {
		if (binary) {
			throw new UnsupportedOperationException();
		}

		if (field != null) {
			setValueToPojo(pojo, converter.convertToJavaObject(value));
		}
	}

	@Override
	public Object decode(String value, Map<String, Object> session) {
		return converter.convertToJavaObject(value);
	}

	@Override
	public void decode(Reader reader, Object pojo, Map<String, Object> session)
			throws IOException {
		setValueToPojo(pojo, decode(reader, session));
	}

	@Override
	public Object decode(Reader reader, Map<String, Object> session)
			throws IOException {
		if (binary) {
			throw new UnsupportedOperationException();
		}

		if (field == null) {
			skip(reader, length);
			return null;
		}

		char[] cbuf = new char[length];
		read(reader, cbuf, length);

		Object value;

		switch (align) {
		case 'l':
			int endIndex = -1;

			for (int i = length - 1; i >= 0; --i) {
				if (cbuf[i] != padWith) {
					endIndex = i + 1;
					break;
				}
			}

			if (endIndex < 0) {
				value = null;
			} else {
				value = converter.convertToJavaObject(cbuf, 0, endIndex);
			}

			break;
		case 'r':
			int beginIndex = -1;

			for (int i = 0; i < length; ++i) {
				if (cbuf[i] != padWith) {
					beginIndex = i;
					break;
				}
			}

			if (beginIndex < 0) {
				value = null;
			} else {
				value = converter.convertToJavaObject(cbuf, beginIndex, length
						- beginIndex);
			}

			break;
		default: // 'n'
			value = converter.convertToJavaObject(cbuf, 0, cbuf.length);
			break;
		}

		return value;
	}
}
