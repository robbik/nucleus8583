package org.nucleus8583.core.type.spi;

import org.nucleus8583.core.type.Serializer;
import org.nucleus8583.core.type.Type;
import org.nucleus8583.core.type.TypeBuilder;
import org.nucleus8583.core.util.AsciiPrefixer;
import org.nucleus8583.core.util.Base16Padder;
import org.nucleus8583.core.util.ObjectHelper;
import org.nucleus8583.core.util.StringHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class AsciiPrefixedBase16Binary implements Type<byte[]>, Serializer<byte[]> {

	protected final AsciiPrefixer prefixer;

	protected final Base16Padder padder;

	protected final int maxLength;

	protected final byte[] emptyValue;

	protected AsciiPrefixedBase16Binary(Builder b) {
		prefixer = new AsciiPrefixer();
		prefixer.setPrefixLength(b.prefixLength);

		padder = new Base16Padder();
		
		maxLength = b.maxLength;
		emptyValue = b.emptyValue;
	}

	@Override
	public AsciiPrefixedBase16Binary serializer() {
		return this;
	}

	@Override
	public byte[] read(InputStream in) throws IOException {
		// read body length
		int vlen = (prefixer.readUint(in) + 1) >> 1;
		if (vlen == 0) {
			return emptyValue;
		}

		// read body
		byte[] buf = new byte[vlen];
		padder.read(in, buf, 0, vlen);

		return buf;
	}

	@Override
	public void write(OutputStream out, byte[] value) throws IOException {
		int vlen = value.length;
		if (vlen > maxLength) {
			throw new IllegalArgumentException("value too long, expected 0-" + maxLength + " but actual is " + vlen);
		}

		// write body length
		prefixer.writeUint(out, vlen << 1);
		
		// write body
		padder.write(out, value, 0, vlen);
	}

	@Override
	public void readBitmap(InputStream in, byte[] bitmap, int off, int len) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeBitmap(OutputStream out, byte[] bitmap, int off, int len) throws IOException {
		throw new UnsupportedOperationException();
	}

	public static class Builder implements TypeBuilder<byte[]> {

		protected int prefixLength;

		protected int maxLength;

		protected byte[] emptyValue;

		public Builder() {
			prefixLength = 0;
			maxLength = 0;
			emptyValue = new byte[0];
		}

		@Override
		public Class<? extends Type<byte[]>> getObjectClass() {
			return AsciiPrefixedBase16Binary.class;
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

			maxLength = value >> 1;
			return this;
		}

		public Builder withEmptyValue(String value) {
			if ((value == null) || value.isEmpty()) {
				emptyValue = new byte[0];
			} else {
				emptyValue = StringHelper.escapeJava(value).getBytes();
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
		public AsciiPrefixedBase16Binary build() {
			validateAll();

			return new AsciiPrefixedBase16Binary(this);
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
