package org.nucleus8583.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import rk.commons.util.IOUtils;

public class EbcdicPrefixer {
    private static final int[][] digitsToInt;

    private static final byte[] intToDigits;

    private static final int MAX_DIGIT = 10;

    static {
        digitsToInt = new int[10][MAX_DIGIT];

        for (int i = 0, len = digitsToInt.length; i < len; ++i) {
            int tens = 1;

            for (int j = 0; j < MAX_DIGIT; ++j) {
                digitsToInt[i][j] = i * tens;
                tens *= 10;
            }
        }

        intToDigits = new byte[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

        for (int i = 0, len = intToDigits.length; i < len; ++i) {
            intToDigits[i] = (byte) EbcdicAsciiTable.ASCII_TO_EBCDIC[intToDigits[i] & 0xFF];
        }
    }

    private int prefixLength;
    
    public EbcdicPrefixer() {
    	// do nothing
    }
    
    public EbcdicPrefixer(EbcdicPrefixer o) {
    	prefixLength = o.prefixLength;
    }

	public void setPrefixLength(int prefixLength) {
        this.prefixLength = prefixLength;
	}

    public void writeUint(OutputStream out, int value) throws IOException {
        int rem = value;

        byte[] buf = new byte[prefixLength];

        for (int i = prefixLength - 1; i >= 0; --i) {
            buf[i] = intToDigits[rem % 10];
            rem = rem / 10;
        }

        out.write(buf);
    }

    public int readUint(InputStream in) throws IOException {
        int value = 0;

        byte[] bbuf = new byte[prefixLength];
        IOUtils.readFully(in, bbuf, prefixLength);

        for (int i = prefixLength - 1, j = 0; i >= 0; --i, ++j) {
            int digitInt = bbuf[j];
            if ((digitInt < 0xF0) || (digitInt > 0xF9)) {
                throw new NumberFormatException((char) EbcdicAsciiTable.EBCDIC_TO_ASCII[bbuf[i]] + " is not a number.");
            }

            value += digitsToInt[digitInt - 0xF0][i];
        }

        return value;
    }
}
