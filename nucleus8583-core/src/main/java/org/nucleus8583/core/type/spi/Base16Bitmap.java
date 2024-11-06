package org.nucleus8583.core.type.spi;

import org.nucleus8583.core.type.Serializer;
import org.nucleus8583.core.type.Type;
import org.nucleus8583.core.type.TypeBuilder;
import org.nucleus8583.core.util.Base16Padder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Base16Bitmap implements Type<Void>, Serializer<Void> {

	protected final Base16Padder padder;

	protected Base16Bitmap(Builder b) {
		padder = new Base16Padder();
	}

	@Override
	public Base16Bitmap serializer() {
		return this;
	}

	@Override
	public Void read(InputStream in) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void write(OutputStream out, Void value) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void readBitmap(InputStream in, byte[] bitmap, int off, int len) throws IOException {
		padder.read(in, bitmap, off, len);
	}

	@Override
	public void writeBitmap(OutputStream out, byte[] bitmap, int off, int len) throws IOException {
		padder.write(out, bitmap, off, len);
	}

	public static class Builder implements TypeBuilder<Void> {

		public Builder() {
			// do nothing
		}

		@Override
		public Class<? extends Type<Void>> getObjectClass() {
			return Base16Bitmap.class;
		}

		@Override
		public Builder with(Properties properties) {
			return this;
		}

		@Override
		public Base16Bitmap build() {
			return new Base16Bitmap(this);
		}

		@Override
		public Builder copy() {
			return new Builder();
		}
	}
}
