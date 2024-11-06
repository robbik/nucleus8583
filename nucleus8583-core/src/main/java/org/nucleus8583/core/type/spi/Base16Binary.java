package org.nucleus8583.core.type.spi;

import org.nucleus8583.core.type.Alignment;
import org.nucleus8583.core.type.Serializer;
import org.nucleus8583.core.type.Type;
import org.nucleus8583.core.type.TypeBuilder;
import org.nucleus8583.core.util.Base16Padder;
import org.nucleus8583.core.util.ObjectHelper;
import org.nucleus8583.core.util.StringHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Base16Binary implements Type<byte[]>, Serializer<byte[]> {

    protected final Base16Padder padder;

    protected final int length;

	protected Base16Binary(Builder b) {
		padder = new Base16Padder();

		padder.setAlign(b.alignment);
		padder.setPadWith(b.padWith);
		padder.setEmptyValue(b.emptyValue);
		padder.setLength(b.padderLength);

		padder.initialize();
    	
    	length = b.length;
    }

	@Override
	public Base16Binary serializer() {
		return this;
	}

	@Override
	public byte[] read(InputStream in) throws IOException {
		return padder.unpad(in);
	}

	@Override
	public void write(OutputStream out, byte[] value) throws IOException {
		int vlen = value.length;
        if (vlen != length) {
            throw new IllegalArgumentException("value length is not equals to " + length
                    + ", actual is " + vlen);
        }

        padder.pad(out, value, 0, vlen);
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

		protected int length;

		protected int padderLength;

		protected Alignment alignment;

		protected byte padWith;

		protected byte[] emptyValue;

		public Builder() {
			length = 0;

			alignment = Alignment.NONE;
			padWith = (byte) 0;

			emptyValue = new byte[0];
		}

		@Override
		public Class<? extends Type<byte[]>> getObjectClass() {
			return Base16Binary.class;
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
				padWith = (byte) 0;
			}

			return this;
		}

		public Builder withPadWith(String value) {
			if ((value == null) || value.isEmpty()) {
				padWith = (byte) 0;
			} else {
				padWith = StringHelper.escapeJava(value).getBytes()[0];
			}

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
		public Base16Binary build() {
			validateAll();

			return new Base16Binary(this);
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
