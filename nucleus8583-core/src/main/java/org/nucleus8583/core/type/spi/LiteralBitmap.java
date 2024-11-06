package org.nucleus8583.core.type.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.nucleus8583.core.type.Serializer;
import org.nucleus8583.core.type.Type;
import org.nucleus8583.core.type.TypeBuilder;
import org.nucleus8583.core.util.LiteralBinaryPadder;

public class LiteralBitmap implements Type<Void>, Serializer<Void> {

	protected final LiteralBinaryPadder padder;

	protected LiteralBitmap(Builder b) {
		padder = new LiteralBinaryPadder();
	}

	@Override
	public LiteralBitmap serializer() {
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

		@Override
		public Class<? extends Type<Void>> getObjectClass() {
			return LiteralBitmap.class;
		}

		@Override
		public Builder with(Properties properties) {
			return this;
		}

		@Override
		public LiteralBitmap build() {
			return new LiteralBitmap(this);
		}

		@Override
		public Builder copy() {
			return new Builder();
		}
	}
}
