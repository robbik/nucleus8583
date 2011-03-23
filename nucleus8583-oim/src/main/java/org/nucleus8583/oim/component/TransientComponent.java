package org.nucleus8583.oim.component;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Map;

import org.nucleus8583.oim.converter.BinaryConverter;
import org.nucleus8583.oim.converter.TypeConverter;
import org.nucleus8583.oim.util.ElExpression;

public final class TransientComponent extends BaseComponent {
	private final int no;

	private final String name;

	private final ElExpression generatedValue;

	private final TypeConverter converter;

	private final boolean binary;

	private final char align;

	private final char padWith;

	private final String padder;

	private final char[] cpadder;

	private final int length;

	public TransientComponent(int no, String name, TypeConverter converter,
			char align, char padWith, int length, ElExpression generatedValue) {
		this.no = no;
		this.name = name;

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

	private Object getValueFromSession(Object pojo, Map<String, Object> session) {
		Object value;

		if (generatedValue != null) {
			value = generatedValue.eval(pojo);
			if (value == null) {
				session.remove(name);
			} else {
				session.put(name, value);
			}
		} else {
			value = session.get(name);
		}

		return value;
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

		String value = converter.convertToIsoString(getValueFromSession(pojo,
				session));
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
		default:
			sb.append(value);
			break;
		}
	}

	@Override
	public String encodeToString(Object pojo, Map<String, Object> session) {
		if (binary) {
			throw new UnsupportedOperationException();
		}

		String value = converter.convertToIsoString(getValueFromSession(pojo,
				session));
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
			return value;
		}
	}

	@Override
	public BitSet encodeToBinary(Object pojo, Map<String, Object> session) {
		if (!binary) {
			return null;
		}

		return converter.convertToIsoBinary(getValueFromSession(pojo, session));
	}

	@Override
	public void decode(BitSet value, Object pojo, Map<String, Object> session) {
		if (binary) {
			throw new UnsupportedOperationException();
		}

		if (value == null) {
			session.remove(name);
		} else {
			session.put(name, value);
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

		Object ovalue = converter.convertToJavaObject(value);
		if (ovalue == null) {
			session.remove(name);
		} else {
			session.put(name, ovalue);
		}
	}

	@Override
	public Object decode(String value, Map<String, Object> session) {
		return converter.convertToJavaObject(value);
	}

	@Override
	public void decode(Reader reader, Object pojo, Map<String, Object> session)
			throws IOException {
		Object ovalue = decode(reader, session);
		if (ovalue == null) {
			session.remove(name);
		} else {
			session.put(name, ovalue);
		}
	}

	@Override
	public Object decode(Reader reader, Map<String, Object> session)
			throws IOException {
		if (binary) {
			throw new UnsupportedOperationException();
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

			if (endIndex == -1) {
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

			if (beginIndex == -1) {
				value = null;
			} else {
				value = converter.convertToJavaObject(cbuf, beginIndex, length
						- beginIndex);
			}

			break;
		default:
			value = converter.convertToJavaObject(cbuf, 0, cbuf.length);
			break;
		}

		return value;
	}
}
