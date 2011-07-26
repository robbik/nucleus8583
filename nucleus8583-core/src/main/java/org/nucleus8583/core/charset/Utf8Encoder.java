package org.nucleus8583.core.charset;

import java.io.IOException;
import java.io.OutputStream;

public class Utf8Encoder implements CharsetEncoder {

    public void write(OutputStream out, int ichar) throws IOException {
        if (ichar < 0) {
            throw new IOException("data contains value " + (ichar & 0xFFFFFFFFL)
                    + " which is not compatible with UTF-8 encoding");
        }

        if (ichar <= 0x7F) {
            out.write(ichar); // 0xxxxxxx
        } else if (ichar <= 0x7FF) {
            out.write(0xC0 | (ichar >> 6)); // 110xxxxx
            out.write(0x80 | (ichar & 0x3F)); // 10xxxxxx
        } else if (ichar <= 0xFFFF) {
            out.write(0xE0 | (ichar >> 12)); // 1110xxxx
            out.write(0x80 | ((ichar >> 6) & 0x3F)); // 10xxxxxx
            out.write(0x80 | (ichar & 0x3F)); // 10xxxxxx
        } else if (ichar <= 0x1FFFFF) {
            out.write(0xF0 | (ichar >> 18)); // 11110xxx
            out.write(0x80 | ((ichar >> 12) & 0x3F)); // 10xxxxxx
            out.write(0x80 | ((ichar >> 6) & 0x3F)); // 10xxxxxx
            out.write(0x80 | (ichar & 0x3F)); // 10xxxxxx
        } else if (ichar <= 0x3FFFFFF) {
            out.write(0xF8 | (ichar >> 24)); // 111110xx
            out.write(0x80 | ((ichar >> 18) & 0x3F)); // 10xxxxxx
            out.write(0x80 | ((ichar >> 12) & 0x3F)); // 10xxxxxx
            out.write(0x80 | ((ichar >> 6) & 0x3F)); // 10xxxxxx
            out.write(0x80 | (ichar & 0x3F)); // 10xxxxxx
        } else {
            out.write(0xFC | (ichar >> 30)); // 1111110x
            out.write(0x80 | ((ichar >> 24) & 0x3F)); // 10xxxxxx
            out.write(0x80 | ((ichar >> 18) & 0x3F)); // 10xxxxxx
            out.write(0x80 | ((ichar >> 12) & 0x3F)); // 10xxxxxx
            out.write(0x80 | ((ichar >> 6) & 0x3F)); // 10xxxxxx
            out.write(0x80 | (ichar & 0x3F)); // 10xxxxxx
        }
    }

    public void write(OutputStream out, char[] cbuf) throws IOException {
        int len = cbuf.length;
        for (int i = 0; i < len;) {
            write(out, cbuf[i++]);
        }
    }

    public void write(OutputStream out, char[] cbuf, int off, int len) throws IOException {
        if (off == 0) {
            for (int i = 0; i < len;) {
                write(out, cbuf[i++]);
            }
        } else {
            for (int i = 0, j = off; i < len; ++i) {
                write(out, cbuf[j++]);
            }
        }
    }

    public void write(OutputStream out, String str) throws IOException {
        int len = str.length();

        for (int i = 0; i < len;) {
            write(out, str.charAt(i++));
        }
    }

    public void write(OutputStream out, String str, int off, int len) throws IOException {
        if (off == 0) {
            for (int i = 0; i < len;) {
                write(out, str.charAt(i++));
            }
        } else {
            for (int i = 0, j = off; i < len; ++i) {
                write(out, str.charAt(j++));
            }
        }
    }
}
