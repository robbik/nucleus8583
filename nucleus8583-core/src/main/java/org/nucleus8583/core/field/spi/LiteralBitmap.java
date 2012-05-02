package org.nucleus8583.core.field.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.field.Type;
import org.nucleus8583.core.util.LiteralBinaryPadder;

public class LiteralBitmap implements Type<Void> {

	private static final long serialVersionUID = -5615324004502124085L;

	protected final LiteralBinaryPadder padder;

	public LiteralBitmap() {
		padder = new LiteralBinaryPadder();
	}
	
	public LiteralBitmap(LiteralBitmap o) {
		padder = new LiteralBinaryPadder(o.padder);
	}

	public Void read(InputStream in) throws IOException {
		throw new UnsupportedOperationException();
	}

	public void write(OutputStream out, Void value) throws IOException {
		throw new UnsupportedOperationException();
	}

	public void readBitmap(InputStream in, byte[] bitmap, int off, int len)
			throws IOException {
		padder.read(in, bitmap, off, len);
	}

	public void writeBitmap(OutputStream out, byte[] bitmap, int off, int len) throws IOException {
		padder.write(out, bitmap, off, len);
	}
	
	public Type<Void> clone() {
		return new LiteralBitmap(this);
	}
}
