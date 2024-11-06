package org.nucleus8583.core.type.spi;

import org.nucleus8583.core.type.Alignment;
import org.nucleus8583.core.type.Serializer;
import org.nucleus8583.core.type.Type;
import org.nucleus8583.core.type.TypeBuilder;
import org.nucleus8583.core.util.AsciiPadder;
import org.nucleus8583.core.util.ObjectHelper;
import org.nucleus8583.core.util.StringHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class AsciiText implements Type<String>, Serializer<String> {

	protected final AsciiPadder padder;

	protected final int length;

	protected AsciiText(Builder b) {
		padder = new AsciiPadder();

		padder.setLength(b.padderLength);
		padder.setAlign(b.alignment);
		padder.setPadWith(b.padWith);
		padder.setEmptyValue(b.emptyValue);

		padder.initialize();

		length = b.length;
	}

	@Override
	public AsciiText serializer() {
		return this;
	}

	@Override
	public String read(InputStream in) throws IOException {
		return new String(padder.unpad(in, length));
	}

	@Override
	public void write(OutputStream out, String value) throws IOException {
		int vlen = value.length();
		if (vlen > length) {
			throw new IllegalArgumentException("value too long, expected " + length + " but actual is " + vlen);
		}

		padder.pad(out, value, vlen);
	}

	@Override
	public void readBitmap(InputStream in, byte[] bitmap, int off, int len) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeBitmap(OutputStream out, byte[] bitmap, int off, int len) throws IOException {
		throw new UnsupportedOperationException();
	}

	public static class Builder implements TypeBuilder<String> {

		protected int length;

		protected int padderLength;

		protected Alignment alignment;

		protected char padWith;

		protected char[] emptyValue;

		public Builder() {
			length = 0;
			padderLength = 0;

			alignment = Alignment.NONE;
			padWith = ' ';
			emptyValue = new char[0];
		}

		@Override
		public Class<? extends Type<String>> getObjectClass() {
			return AsciiText.class;
		}

		public Builder withLength(int value) {
			if (value <= 0) {
				throw new IllegalArgumentException("length must greater than zero");
			}

			length = value;
			padderLength = value;

			return this;
		}

		public Builder withAlignment(Alignment value) {
			if (value == null) {
				value = Alignment.NONE;
			}

			alignment = value;

			if (value == Alignment.NONE) {
				padWith = ' ';
			}
			return this;
		}

		public Builder withPadWith(String value) {
			if ((value == null) || value.isEmpty()) {
				padWith = ' ';
			} else {
				padWith = StringHelper.escapeJava(value).charAt(0);
			}
			return this;
		}

		public Builder withEmptyValue(String value) {
			if ((value == null) || value.isEmpty()) {
				emptyValue = new char[0];
			} else {
				emptyValue = StringHelper.escapeJava(value).toCharArray();
			}
			return this;
		}

		@Override
		public Builder with(Properties properties) {
			StringHelper.ifInt(properties.getProperty("length"), this::withLength);
			ObjectHelper.ifNotNull(Alignment.enumValueOf(properties.getProperty("alignment")), this::withAlignment);
			StringHelper.ifHasText(properties.getProperty("pad-with"), this::withPadWith);
			StringHelper.ifHasText(properties.getProperty("empty-value"), this::withEmptyValue);

			return this;
		}

		protected void validateAll() {
			if (length <= 0) {
				throw new IllegalArgumentException("length must greater than zero");
			}
		}

		@Override
		public AsciiText build() {
			validateAll();

			return new AsciiText(this);
		}

		@Override
		public Builder copy() {
			Builder c = new Builder();

			c.length = length;
			c.padderLength = padderLength;
			c.alignment = alignment;
			c.padWith = padWith;
			c.emptyValue = emptyValue;

			return c;
		}
	}
}
