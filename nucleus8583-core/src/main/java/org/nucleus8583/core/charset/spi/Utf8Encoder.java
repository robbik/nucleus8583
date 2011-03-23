package org.nucleus8583.core.charset.spi;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class Utf8Encoder extends Writer {
    private final OutputStream out;

    public Utf8Encoder(OutputStream out) {
        this.out = out;
    }

    public void close() throws IOException {
        out.close();
    }

    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void write(int ichar) throws IOException {
        if (ichar < 0) {
            throw new IOException("data contains value " + ((long) ichar & 0xFFFFFFFFL)
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

    @Override
    public void write(char[] cbuf) throws IOException {
        int len = cbuf.length;
        for (int i = 0; i < len;) {
            write((int) cbuf[i++]);
        }
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        if (off == 0) {
            for (int i = 0; i < len;) {
                write((int) cbuf[i++]);
            }
        } else {
            for (int i = 0, j = off; i < len; ++i) {
                write((int) cbuf[j++]);
            }
        }
    }

    @Override
    public void write(String str) throws IOException {
        int len = str.length();

        for (int i = 0; i < len;) {
            write(str.charAt(i++) & 0x7F);
        }
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        if (off == 0) {
            for (int i = 0; i < len;) {
                write((int) str.charAt(i++));
            }
        } else {
            for (int i = 0, j = off; i < len; ++i) {
                write((int) str.charAt(j++));
            }
        }
    }
}
