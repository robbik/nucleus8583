package org.nucleus8583.core.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.nucleus8583.core.xml.FieldAlignments;

public abstract class BcdUtils {

    private static final int[] iTENS;

    private static final long[] lTENS;

    static {
        iTENS = new int[10];

        iTENS[0] = 1;
        iTENS[1] = 10;

        for (int i = 2; i < iTENS.length; ++i) {
            iTENS[i] = iTENS[i - 1] * 10;
        }

        lTENS = new long[20];

        lTENS[0] = 1;
        lTENS[1] = 10;

        for (int i = 2; i < iTENS.length; ++i) {
            lTENS[i] = lTENS[i - 1] * 10;
        }
    }

    public static int hex2int(char ichar) {
        switch (ichar) {
        case '0':
            // 0000
            return 0;
        case '1':
            // 0001
            return 1;
        case '2':
            // 0010
            return 2;
        case '3':
            // 0011
            return 3;
        case '4':
            // 0100
            return 4;
        case '5':
            // 0101
            return 5;
        case '6':
            // 0110
            return 6;
        case '7':
            // 0111
            return 7;
        case '8':
            // 1000
            return 8;
        case '9':
            // 1001
            return 9;
        case 'A':
            // 1010
            return 10;
        case 'B':
            // 1011
            return 11;
        case 'C':
            // 1100
            return 12;
        case 'D':
            // 1101
            return 13;
        case 'E':
            // 1110
            return 14;
        case 'F':
            // 1111
            return 15;
        }

        throw new IllegalArgumentException("character " + ichar + " is not numeric");
    }

    /**
     * convert packed BCD to integer
     *
     * @param bcd
     * @return
     */
    public static int bcdToInt(byte[] bcd) {
        if (bcd.length > 4) {
            throw new IllegalArgumentException("bcd value is more than 99,999,999. use bcdToLong instead");
        }

        int result = 0;

        for (int i = bcd.length - 1, j = 0; i >= 0; --i, j += 2) {
            result += iTENS[j] * (bcd[i] & 0x0F);
            result += iTENS[j + 1] * ((bcd[i] >> 4) & 0x0F);
        }

        return result;
    }

    /**
     * convert packed BCD to long
     *
     * @param bcd
     * @return
     */
    public static long bcdToLong(byte[] bcd) {
        long result = 0;

        for (int i = bcd.length - 1, j = 0; i >= 0; --i, j += 2) {
            result += lTENS[j] * (bcd[i] & 0x0F);
            result += lTENS[j + 1] * ((bcd[i] >> 4) & 0x0F);
        }

        return result;
    }

    /**
     * read packed bcd from stream and return integer value
     *
     * @param in
     *            stream to be read
     * @param len
     *            packed bcd length in bytes
     * @return integer value
     * @throws IOException
     *             if an IO error occured
     */
    public static int readUint(InputStream in, int len) throws IOException {
        int result = 0;
        int bcd;

        for (int i = len - 1, j = (len << 1) - 1; i >= 0; --i, j -= 2) {
            bcd = in.read();

            if (bcd < 0) {
                throw new EOFException();
            }

            result += iTENS[j] * ((bcd >> 4) & 0x0F);
            result += iTENS[j - 1] * (bcd & 0x0F);
        }

        return result;
    }

    /**
     * write integer value to stream as packed bcd
     *
     * @param out
     *            stream to be read
     * @param value
     *            integer value
     * @param len
     *            number of packed bcd length in bytes
     * @throws IOException
     *             if an IO error occured
     */
    public static void writeUint(OutputStream out, int value, int len) throws IOException {
        byte[] bcd = new byte[len];
        int twoDigits;

        for (int i = len - 1; i >= 0; --i) {
            twoDigits = value % 100;
            value /= 100;

            bcd[i] = (byte) (((twoDigits / 10) << 4) | (twoDigits % 10));
        }

        out.write(bcd, 0, len);
    }

    /**
     * convert integer to packed BCD
     *
     * @param value
     * @param bcd
     */
    public static void intToBcd(int value, byte[] bcd) {
        Arrays.fill(bcd, (byte) 0);

        int i = bcd.length - 1;
        int digit10;

        while (value > 0) {
            digit10 = value % 10;
            value = value / 10;

            bcd[i] = (byte) digit10;

            if (value > 0) {
                digit10 = value % 10;
                value = value / 10;

                bcd[i] |= (byte) (digit10 << 4);
            }

            --i;
        }
    }

    /**
     * convert string to packed bcd
     *
     * @param chars
     * @param length
     * @param bcd
     */
    public static void strToBcd(char[] chars, int length, byte[] bcd) {
        Arrays.fill(bcd, (byte) 0);

        int i = bcd.length - 1;

        boolean even = false;

        for (int j = length - 1; j >= 0; --j) {
            if (even) {
                bcd[i] |= (byte) (((chars[j] - '0') & 0x0F) << 4);
                --i;
            } else {
                bcd[i] |= (byte) ((chars[j] - '0') & 0x0F);
            }

            even = !even;
        }
    }

    /**
     * convert string to packed bcd
     *
     * @param str
     * @return
     */
    public static byte[] strToBcd(String str) {
        int length = str.length();
        byte[] bcd = new byte[length + 1 >> 1];

        Arrays.fill(bcd, (byte) 0);

        int i = bcd.length - 1;

        for (int j = length - 1, k = 0; j >= 0; --j, ++k) {
            if ((k & 0x01) == 0) {
                bcd[i] |= (byte) ((str.charAt(j) - '0') & 0x0F);
            } else {
                bcd[i] |= (byte) (((str.charAt(j) - '0') & 0x0F) << 4);
                --i;
            }
        }

        return bcd;
    }

    /**
     * convert string to packed bcd
     *
     * @param str
     * @param length
     * @param bcd
     */
    public static void strToBcd(String str, int length, byte[] bcd, char padWith, FieldAlignments align) {
        int i = bcd.length - 1;

        if ((length & 1) != 0) {
            switch (align) {
            case TRIMMED_LEFT:
            case UNTRIMMED_LEFT:
                str = str + padWith;
                break;
            case TRIMMED_RIGHT:
            case UNTRIMMED_RIGHT:
                str = padWith + str;
                break;
            default: // NONE
                str = padWith + str;
                break;
            }

            ++length;
        }

        for (int j = length - 1, k = 0; j >= 0; --j, ++k) {
            if ((k & 1) == 0) {
                bcd[i] |= (byte) (hex2int(str.charAt(j)) & 0x0F);
            } else {
                bcd[i] |= (byte) ((hex2int(str.charAt(j)) & 0x0F) << 4);
                --i;
            }
        }
    }

    /**
     * convert packed bcd to string
     *
     * @param bcd
     * @return
     */
    public static String bcdToStr(byte[] bcd) {
        int length = bcd.length << 1;
        char[] chars = new char[length];

        bcdToStr(bcd, chars, length);
        return new String(chars);
    }

    /**
     * convert packed bcd to string
     *
     * @param bcd
     * @param length
     * @return
     */
    public static String bcdToStr(byte[] bcd, int length) {
        char[] chars = new char[length];

        bcdToStr(bcd, chars, length);
        return new String(chars);
    }

    /**
     * convert packed bcd to string
     *
     * @param bcd
     * @param chars
     * @param length
     */
    public static void bcdToStr(byte[] bcd, char[] chars, int length) {
        int bcdLength = bcd.length;

        Arrays.fill(chars, '0');

        for (int i = bcdLength - 1, j = length - 1; (i >= 0) && (j >= 0); --i, j -= 2) {
            chars[j] = (char) ('0' + (bcd[i] & 0x0F));

            if (j > 0) {
                chars[j - 1] = (char) ('0' + ((bcd[i] >> 4) & 0x0F));
            }
        }
    }
}
