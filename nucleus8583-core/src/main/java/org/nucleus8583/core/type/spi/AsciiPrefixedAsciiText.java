package org.nucleus8583.core.type.spi;

import org.nucleus8583.core.type.Serializer;
import org.nucleus8583.core.type.Type;
import org.nucleus8583.core.type.TypeBuilder;
import org.nucleus8583.core.util.AsciiPadder;
import org.nucleus8583.core.util.AsciiPrefixer;
import org.nucleus8583.core.util.ObjectHelper;
import org.nucleus8583.core.util.StringHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class AsciiPrefixedAsciiText implements Type<String>, Serializer<String> {

	protected final AsciiPrefixer prefixer;

	protected final AsciiPadder padder;

	protected int maxLength;

	protected String emptyValue;

	protected AsciiPrefixedAsciiText(Builder b) {
		prefixer = new AsciiPrefixer();
		prefixer.setPrefixLength(b.prefixLength);

		padder = new AsciiPadder();

		maxLength = b.maxLength;
		emptyValue = b.emptyValue;
	}

	@Override
	public AsciiPrefixedAsciiText serializer() {
		return this;
	}

	@Override
	public String read(InputStream in) throws IOException {
		// read body length
		int vlen = prefixer.readUint(in);
		if (vlen == 0) {
			return emptyValue;
		}

		// read body
		char[] cbuf = new char[vlen];
		padder.read(in, cbuf, 0, vlen);

		return new String(cbuf);
	}

	@Override
	public void write(OutputStream out, String value) throws IOException {
		int vlen = value.length();
		if (vlen > maxLength) {
			throw new IllegalArgumentException("value too long, expected 0-" + maxLength + " but actual is " + vlen);
		}

		// write body length
		prefixer.writeUint(out, vlen);

		// write body
		padder.write(out, value, 0, vlen);
	}

	@Override
	public void readBitmap(InputStream in, byte[] bitmap, int off, int len)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeBitmap(OutputStream out, byte[] bitmap, int off, int len)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	public static class Builder implements TypeBuilder<String> {

		protected int prefixLength;

		protected int maxLength;

		protected String emptyValue;

		public Builder() {
			prefixLength = 0;
			maxLength = 0;

			emptyValue = "";
		}

		@Override
		public Class<? extends Type<String>> getObjectClass() {
			return AsciiPrefixedAsciiText.class;
		}

		public Builder withPrefixLength(int value) {
			if (value <= 0) {
				throw new IllegalArgumentException("prefix length must greater than 0");
			}

			prefixLength = value;
			return this;
		}

		public Builder withMaxLength(int value) {
			if (value <= 0) {
				throw new IllegalArgumentException("max length must greater than 0");
			}

			maxLength = value;
			return this;
		}

		public Builder withEmptyValue(String value) {
			if ((value == null) || value.isEmpty()) {
				emptyValue = "";
			} else {
				emptyValue = StringHelper.escapeJava(value);
			}
			return this;
		}

		@Override
		public Builder with(Properties properties) {
			StringHelper.ifInt(properties.getProperty("prefix-length"), this::withPrefixLength);
			StringHelper.ifInt(properties.getProperty("max-length"), this::withMaxLength);
			ObjectHelper.ifNotNull(properties.getProperty("empty-value"), this::withEmptyValue);

			return this;
		}

		protected void validateAll() {
			if (prefixLength <= 0) {
				throw new IllegalArgumentException("prefix length must greater than 0");
			}
			if (maxLength <= 0) {
				throw new IllegalArgumentException("max length must greater than 0");
			}
		}

		@Override
		public AsciiPrefixedAsciiText build() {
			validateAll();

			return new AsciiPrefixedAsciiText(this);
		}

		@Override
		public Builder copy() {
			Builder c = new Builder();

			c.prefixLength = prefixLength;
			c.maxLength = maxLength;
			c.emptyValue = emptyValue;

			return c;
		}
	}
}
