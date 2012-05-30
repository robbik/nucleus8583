package org.nucleus8583.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.nucleus8583.core.field.Alignment;

import rk.commons.util.IOHelper;

public class BcdPadder {

    private char padWith;

    private char align;

    private int roundupLength;

    private int valueLength;

    private int diffLength;

    private String padder;

    private char[] emptyValue;
    
    public BcdPadder() {
    	// do nothing
    }
    
    public BcdPadder(BcdPadder o) {
    	padWith = o.padWith;
    	align = o.align;
    	
    	roundupLength = o.roundupLength;
    	valueLength = o.valueLength;
    	diffLength = o.diffLength;
    	
    	padder = o.padder;
    	emptyValue = o.emptyValue;
    }

    public void setPadWith(char padWith) {
        if ((padWith < '0') || (padWith > '9')) {
            throw new IllegalArgumentException("padWith must be a number in range 0-9");
        }

        this.padWith = padWith;
    }

    public void setAlign(Alignment align) {
        this.align = align.symbolicValue();
    }

    public Alignment getAlign() {
        return Alignment.enumValueOf(align);
    }

    public void setLength(int length) {
        roundupLength = ((length + 1) >> 1) << 1;
        valueLength = length;

        diffLength = roundupLength - valueLength;
    }

    public void setEmptyValue(char[] emptyValue) {
        this.emptyValue = emptyValue;
    }

    public void initialize() {
        char[] padder = new char[roundupLength];
        Arrays.fill(padder, padWith);

        this.padder = new String(padder);
    }

    public void pad(OutputStream out, String value, int vlen) throws IOException {
        if (vlen == 0) {
            write(out, padder, roundupLength);
        } else if (vlen == roundupLength) {
            write(out, value, vlen);
        } else {
            switch (align) {
            case 'l':
            case 'L':
                write(out, value.concat(padder.substring(vlen)), roundupLength);
                break;
            case 'r':
            case 'R':
            	write(out, padder.substring(0, roundupLength - vlen).concat(value), roundupLength);
            	break;
            default: // NONE
            	if (diffLength == 0) {
            		write(out, padder.substring(0, roundupLength - vlen).concat(value), roundupLength);
            	} else {
            		write(out, padder.substring(0, valueLength - vlen).concat(value).concat(
            				padder.substring(0, diffLength)), roundupLength);
            	}
                break;
            }
        }
    }

    public char[] unpad(InputStream in) throws IOException {
        char[] value = new char[roundupLength];
        read(in, value, roundupLength);

        char[] result;
        int resultLength;

        switch (align) {
        case 'l':
            resultLength = 0;

            for (int i = roundupLength - 1; i >= 0; --i) {
                if (value[i] != padWith) {
                    resultLength = i + 1;
                    break;
                }
            }

            if (resultLength == 0) {
                result = emptyValue;
            } else if (resultLength == roundupLength) {
                result = value;
            } else {
                result = new char[resultLength];
                System.arraycopy(value, 0, result, 0, resultLength);
            }

            break;
        case 'r':
            int padLength = roundupLength;

            for (int i = 0; i < roundupLength; ++i) {
                if (value[i] != padWith) {
                    padLength = i;
                    break;
                }
            }

            if (padLength == 0) {
                result = value;
            } else if (padLength == roundupLength) {
                result = emptyValue;
            } else {
                resultLength = valueLength - padLength;

                result = new char[resultLength];
                System.arraycopy(value, padLength, result, 0, resultLength);
            }

            break;
        default: // NONE, UNTRIMMED_LEFT, UNTRIMMED_RIGHT
            result = value;
            break;
        }

        if (diffLength != 0) {
            value = result;

            result = new char[valueLength];
            System.arraycopy(value, 0, result, 0, valueLength);

            switch (align) {
            case 'r':
            case 'R':
                result = new char[valueLength];
                System.arraycopy(value, diffLength, result, 0, valueLength);
            	break;
        	default:
                result = new char[valueLength];
                System.arraycopy(value, 0, result, 0, valueLength);
                break;
            }
        }

        return result;
    }

    /**
     * read (N + 1) / 2 bytes from input stream and store it to <code>value</code>
     * starting from offset <code>off</code>.
     *
     * @param in
     * @param value
     * @param vlen
     * @throws IOException
     */
    public void read(InputStream in, char[] value, int vlen) throws IOException {
        byte[] bcd = new byte[(vlen + 1) >> 1];
        IOHelper.readFully(in, bcd, bcd.length);

        BcdUtils.bcdToStr(bcd, value, vlen);
    }

    /**
     * write N bytes of value to output stream. As the each byte of value will
     * be written in bcd form, so this method will write (N + 1) / 2 bytes in
     * the stream.
     *
     * @param out
     * @param value
     * @param vlen
     * @throws IOException
     */
    public void write(OutputStream out, String value, int vlen) throws IOException {
        byte[] bcd = new byte[(vlen + 1) >> 1];
        BcdUtils.strToBcd(value, vlen, bcd, padWith, align);

        out.write(bcd);
    }
}
