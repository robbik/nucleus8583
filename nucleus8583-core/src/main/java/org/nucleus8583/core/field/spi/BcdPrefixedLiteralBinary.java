package org.nucleus8583.core.field.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.field.Type;
import org.nucleus8583.core.util.BcdPrefixer;
import org.nucleus8583.core.util.LiteralBinaryPadder;

import rk.commons.util.StringHelper;

public class BcdPrefixedLiteralBinary implements Type<byte[]> {

    private static final long serialVersionUID = -5615324004502124085L;

    protected final BcdPrefixer prefixer;

    protected final LiteralBinaryPadder padder;

    protected int maxLength;

	protected byte[] emptyValue;

	public BcdPrefixedLiteralBinary() {
        prefixer = new BcdPrefixer();
        padder = new LiteralBinaryPadder();
        
        emptyValue = new byte[0];
	}
	
	public BcdPrefixedLiteralBinary(BcdPrefixedLiteralBinary o) {
		prefixer = new BcdPrefixer(o.prefixer);
		padder = new LiteralBinaryPadder(o.padder);
		
		maxLength = o.maxLength;
		emptyValue = o.emptyValue;
	}
	
	public void setPrefixLength(int prefixLength) {
		prefixer.setPrefixLength(prefixLength);
	}
	
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public void setEmptyValue(String emptyValue) {
		if (emptyValue == null) {
			this.emptyValue = new byte[0];
		} else {
			this.emptyValue = StringHelper.escapeJava(emptyValue).getBytes();
		}
	}

    public byte[] read(InputStream in) throws IOException {
    	// read body length
        int vlen = prefixer.readUint(in);
        if (vlen == 0) {
            return padder.getEmptyValue();
        }

        // read body
        byte[] bbuf = new byte[vlen];
        padder.read(in, bbuf, 0, vlen);

        return bbuf;
    }

    public void write(OutputStream out, byte[] value) throws IOException {
        int vlen = value.length;
        if (vlen > maxLength) {
            throw new IllegalArgumentException("value too long, expected " + maxLength + " but actual is " + vlen);
        }

    	// write body length
        prefixer.writeUint(out, vlen);
        
        // write body
        padder.write(out, value, 0, vlen);
    }

	public void readBitmap(InputStream in, byte[] bitmap, int off, int len)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	public void writeBitmap(OutputStream out, byte[] bitmap, int off, int len)
			throws IOException {
		throw new UnsupportedOperationException();
	}
	
	public Type<byte[]> clone() {
		return new BcdPrefixedLiteralBinary(this);
	}
}
