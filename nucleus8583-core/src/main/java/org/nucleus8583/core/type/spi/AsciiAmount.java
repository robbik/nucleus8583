package org.nucleus8583.core.type.spi;

import org.nucleus8583.core.type.Alignment;
import org.nucleus8583.core.type.Type;
import org.nucleus8583.core.util.StringHelper;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class AsciiAmount extends AsciiText {

	protected AsciiAmount(Builder b) {
		super(b);
	}

	@Override
	public AsciiAmount serializer() {
		return this;
	}

	@Override
	public String read(InputStream in) throws IOException {
        int first = in.read();
        if (first < 0) {
            throw new EOFException();
        }
        
        if (length > 1) {
        	return ((char) first) + new String(padder.unpad(in, length - 1));
        } else {
        	return String.valueOf((char) first);
        }
	}

	@Override
	public void write(OutputStream out, String value) throws IOException {
		int vlen = value.length();
		if (vlen > length) {
			throw new IllegalArgumentException("value too long, expected " + length + " but actual is " + vlen);
		}

		if (vlen > 0) {
			out.write(value.charAt(0) & 0xFF);

        	padder.pad(out, value.substring(1), vlen - 1);
		} else {
			padder.pad(out, "", 0);
		}
	}

	public static class Builder extends AsciiText.Builder {

		public Builder() {
			super();

			alignment = Alignment.TRIMMED_RIGHT;
			padWith = '0';
			emptyValue = new char[] { '0' };
		}

		@Override
		public Class<? extends Type<String>> getObjectClass() {
			return AsciiAmount.class;
		}

		@Override
		public Builder withLength(int value) {
			if (value <= 0) {
				throw new IllegalArgumentException("length must greater than zero");
			}

			padderLength = value - 1;
			length = value;

			return this;
		}

		@Override
		public AsciiText.Builder withAlignment(Alignment value) {
			if (value == null) {
				value = Alignment.TRIMMED_RIGHT;
			}

			alignment = value;

			if (value == Alignment.NONE) {
				padWith = '0';
			}
			return this;
		}

		@Override
		public AsciiText.Builder withPadWith(String value) {
			if ((value == null) || value.isEmpty()) {
				padWith = '0';
			} else {
				padWith = StringHelper.escapeJava(value).charAt(0);
			}
			return this;
		}

		@Override
		public AsciiText.Builder withEmptyValue(String value) {
			if ((value == null) || value.isEmpty()) {
				emptyValue = new char[] { '0' };
			} else {
				emptyValue = StringHelper.escapeJava(value).toCharArray();
			}
			return this;
		}

		@Override
		public Builder with(Properties properties) {
			super.with(properties);

			return this;
		}

		@Override
		public AsciiAmount build() {
			super.validateAll();

			return new AsciiAmount(this);
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
