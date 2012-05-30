package org.nucleus8583.core.field.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.field.Alignment;
import org.nucleus8583.core.field.Type;
import org.nucleus8583.core.util.BcdPadder;
import org.nucleus8583.core.util.EbcdicPrefixer;

import rk.commons.util.StringHelper;

public class EbcdicPrefixedBcdNumeric implements Type<String> {

    private static final long serialVersionUID = -5615324004502124085L;

    protected final EbcdicPrefixer prefixer;

    protected final BcdPadder padder;

    protected int maxLength;

    protected String emptyValue;

    public EbcdicPrefixedBcdNumeric() {
        prefixer = new EbcdicPrefixer();
        padder = new BcdPadder();
        
        emptyValue = "";
    }
    
    public EbcdicPrefixedBcdNumeric(EbcdicPrefixedBcdNumeric o) {
    	prefixer = new EbcdicPrefixer(o.prefixer);
    	padder = new BcdPadder(o.padder);
    	
    	maxLength = o.maxLength;
    	emptyValue = o.emptyValue;
    }
	
	public void setPrefixLength(int prefixLength) {
		prefixer.setPrefixLength(prefixLength);
	}
	
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public void setAlignment(Alignment align) {
		if (align == null) {
			throw new IllegalArgumentException("alignment required");
		}

		padder.setAlign(align);

		if (align == Alignment.NONE) {
			padder.setPadWith('0');
		}
	}

	public void setPadWith(String padWith) {
		if (!StringHelper.hasText(padWith, false)) {
			throw new IllegalArgumentException("pad-with required");
		}

		padder.setPadWith(StringHelper.escapeJava(padWith).charAt(0));
	}
	
	public void setEmptyValue(String emptyValue) {
		if (emptyValue == null) {
			this.emptyValue = "";
		} else {
			this.emptyValue = StringHelper.escapeJava(emptyValue);
		}
	}

    public String read(InputStream in) throws IOException {
    	// read body length
        int vlen = prefixer.readUint(in);
        if (vlen == 0) {
            return emptyValue;
        }

        // read body
        char[] cbuf = new char[vlen];
        padder.read(in, cbuf, vlen);

        return new String(cbuf);
    }

    public void write(OutputStream out, String value) throws IOException {
        int vlen = value.length();
        if (vlen > maxLength) {
            throw new IllegalArgumentException("value too long, expected 0-" + maxLength + " but actual is " + vlen);
        }

        // write body length
        prefixer.writeUint(out, vlen);
        
        // write body
        padder.write(out, value, vlen);
    }

	public void readBitmap(InputStream in, byte[] bitmap, int off, int len)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	public void writeBitmap(OutputStream out, byte[] bitmap, int off, int len)
			throws IOException {
		throw new UnsupportedOperationException();
	}
	
	public Type<String> clone() {
		return new EbcdicPrefixedBcdNumeric(this);
	}
}
