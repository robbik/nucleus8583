package org.nucleus8583.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.nucleus8583.core.xml.FieldAlignments;

public class BcdPadder {

    private char padWith;

    private FieldAlignments align;

    private int length;

    private String padder;

    private char[] emptyValue;

    public void setPadWith(String padWith) {
        int len = padWith.length();
        if (len == 0) {
            padWith = "0";
        }

        setPadWith(padWith.charAt(0));
    }

    public void setPadWith(char padWith) {
        if ((padWith < '0') || (padWith > '9')) {
            throw new IllegalArgumentException("padWith must be a number in range 0-9");
        }

        this.padWith = padWith;
    }

    public void setAlign(FieldAlignments align) {
        this.align = align;
    }

    public FieldAlignments getAlign() {
        return align;
    }

    public void setLength(int length) {
        this.length = length << 1;
    }

    public void setEmptyValue(char[] emptyValue) {
        this.emptyValue = emptyValue;
    }

    public void initialize() {
        char[] padder = new char[length];
        Arrays.fill(padder, padWith);

        this.padder = new String(padder);
    }

    public void pad(OutputStream out, String value, int vlen) throws IOException {
        if (vlen == 0) {
            write(out, padder, length);
        } else if (vlen == length) {
            write(out, value, vlen);
        } else {
            switch (align) {
            case TRIMMED_LEFT:
            case UNTRIMMED_LEFT:
                write(out, value + padder.substring(vlen), length);
                break;
            case TRIMMED_RIGHT:
            case UNTRIMMED_RIGHT:
                write(out, padder.substring(0, length - vlen) + value, length);
                break;
            default: // NONE
                write(out, padder.substring(0, length - vlen) + value, length);
                break;
            }
        }
    }

    public char[] unpad(InputStream in) throws IOException {
        char[] value = new char[length];
        read(in, value, length);

        char[] result;
        int resultLength;

        switch (align) {
        case TRIMMED_LEFT:
            resultLength = 0;

            for (int i = length - 1; i >= 0; --i) {
                if (value[i] != padWith) {
                    resultLength = i + 1;
                    break;
                }
            }

            if (resultLength == 0) {
                result = emptyValue;
            } else if (resultLength == length) {
                result = value;
            } else {
                result = new char[resultLength];
                System.arraycopy(value, 0, result, 0, resultLength);
            }

            break;
        case TRIMMED_RIGHT:
            int padLength = length;

            for (int i = 0; i < length; ++i) {
                if (value[i] != padWith) {
                    padLength = i;
                    break;
                }
            }

            if (padLength == 0) {
                result = value;
            } else if (padLength == length) {
                result = emptyValue;
            } else {
                resultLength = length - padLength;

                result = new char[resultLength];
                System.arraycopy(value, padLength, result, 0, resultLength);
            }

            break;
        default: // NONE, UNTRIMMED_LEFT, UNTRIMMED_RIGHT
            result = value;
            break;
        }

        return result;
    }

    /**
     * read (N + 1) / 2 bytes from input stream and store it to <code>value</code>
     * starting from offset <code>off</code>.
     *
     * @param in
     * @param value
     * @param off
     * @param vlen
     * @throws IOException
     */
    public void read(InputStream in, char[] value, int vlen) throws IOException {
        byte[] bcd = new byte[(vlen + 1) >> 1];
        IOUtils.readFully(in, bcd, bcd.length);

        BcdUtils.bcdToStr(bcd, value, vlen);
    }

    /**
     * write N bytes of value to output stream. As the each byte of value will
     * be written in bcd form, so this method will write (N + 1) / 2 bytes in
     * the stream.
     *
     * @param out
     * @param value
     * @param off
     * @param vlen
     * @throws IOException
     */
    public void write(OutputStream out, String value, int vlen) throws IOException {
        byte[] bcd = new byte[(vlen + 1) >> 1];
        BcdUtils.strToBcd(value, vlen, bcd, padWith, align);

        out.write(bcd);
    }
}
