package org.nucleus8583.core.field.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.field.Type;
import org.nucleus8583.core.util.Base16Padder;
import org.nucleus8583.core.util.BcdPrefixer;

import rk.commons.util.StringHelper;

public class BcdPrefixedBase16Binary implements Type<byte[]> {

    private static final long serialVersionUID = -5615324004502124085L;

    protected final BcdPrefixer prefixer;

    protected final Base16Padder padder;

    protected int maxLength;

    protected byte[] emptyValue;

    public BcdPrefixedBase16Binary() {
        prefixer = new BcdPrefixer();
        padder = new Base16Padder();
        
        emptyValue = new byte[0];
    }
    
    public BcdPrefixedBase16Binary(BcdPrefixedBase16Binary o) {
    	prefixer = new BcdPrefixer(o.prefixer);
    	padder = new Base16Padder(o.padder);
    	
    	maxLength = o.maxLength;
    	emptyValue = o.emptyValue;
    }
	
	public void setPrefixLength(int prefixLength) {
		prefixer.setPrefixLength(prefixLength);
	}
	
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength >> 1;
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
        int vlen = (prefixer.readUint(in) + 1) >> 1;
        if (vlen == 0) {
            return emptyValue;
        }

        // read body
        byte[] buf = new byte[vlen];
        padder.read(in, buf, 0, vlen);

        return buf;
    }

    public void write(OutputStream out, byte[] value) throws IOException {
        int vlen = value.length;
        if (vlen > maxLength) {
            throw new IllegalArgumentException("value too long, expected 0-" + maxLength
                    + " but actual is " + vlen);
        }

        // write body length
        prefixer.writeUint(out, vlen << 1);
        
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
		return new BcdPrefixedBase16Binary(this);
	}
}
