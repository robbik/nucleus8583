package org.nucleus8583.core.util;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class BcdUtilsTest {

    private static final byte[][] PACKED_BCD_1 = { { 0 }, { 1 }, { 2 }, { 3 }, { 4 }, { 5 }, { 6 }, { 7 }, { 8 }, { 9 } };

    private static final byte[][] PACKED_BCD_2;

    private static final byte[][] PACKED_BCD_3;

    private static final byte[][] PACKED_BCD_4;

    static {
        PACKED_BCD_2 = new byte[100][];
        PACKED_BCD_3 = new byte[1000][];
        PACKED_BCD_4 = new byte[10000][];

        for (int i = 10; i < 100; ++i) {
            PACKED_BCD_2[i] = new byte[] { (byte) (((i / 10) << 4) | (i % 10)) };
        }

        for (int i = 100; i < 1000; ++i) {
            int hi = i / 100;
            int lo = i % 100;

            PACKED_BCD_3[i] = new byte[] { (byte) (hi % 10), (byte) (((lo / 10) << 4) | (lo % 10)) };
        }

        for (int i = 1000; i < 10000; ++i) {
            int hi = i / 100;
            int lo = i % 100;

            PACKED_BCD_4[i] = new byte[] { (byte) (((hi / 10) << 4) | (hi % 10)), (byte) (((lo / 10) << 4) | (lo % 10)) };
        }
    }

    @Test
    void testBcdToIntIfLenIs1() {
        for (int i = 0; i < PACKED_BCD_1.length; ++i) {
            assertThat(BcdUtils.bcdToInt(PACKED_BCD_1[i]), is(i));
        }
    }

    @Test
    void testReadUintIfLenIs1() throws IOException {
        for (int i = 0; i < PACKED_BCD_1.length; ++i) {
            assertThat(BcdUtils.readUint(new ByteArrayInputStream(PACKED_BCD_1[i]), PACKED_BCD_1[i].length), is(i));
        }
    }

    @Test
    void testBcdToStrIfLenIs1() {
        char[] chars = new char[2];

        for (int i = 0; i < PACKED_BCD_1.length; ++i) {
            BcdUtils.bcdToStr(PACKED_BCD_1[i], chars, 2);

            assertThat(new String(chars), is("0" + i));
        }
    }

    @Test
    void testIntToBcdIfLenIs1() {
        byte[] bcd = new byte[1];

        for (int i = 0; i < 10; ++i) {
            BcdUtils.intToBcd(i, bcd);

            assertArrayEquals(PACKED_BCD_1[i], bcd);
        }
    }

    @Test
    void testWriteUintIfLenIs1() throws IOException {
        for (int i = 0; i < 10; ++i) {
            ByteArrayOutputStream out = new ByteArrayOutputStream(1);
            BcdUtils.writeUint(out, i, 1);

            assertArrayEquals(PACKED_BCD_1[i], out.toByteArray());
        }
    }

    @Test
    void testStrToBcdIfLenIs1() {
        byte[] bcd = new byte[1];

        for (int i = 0; i < 10; ++i) {
            BcdUtils.strToBcd(("" + i).toCharArray(), 1, bcd);
            assertArrayEquals(PACKED_BCD_1[i], bcd);
        }
    }

    @Test
    void testBcdToIntIfLenIs2() {
        for (int i = 10; i < PACKED_BCD_2.length; ++i) {
            assertThat(BcdUtils.bcdToInt(PACKED_BCD_2[i]), is(i));
        }
    }

    @Test
    void testReadUintIfLenIs2() throws IOException {
        for (int i = 10; i < PACKED_BCD_2.length; ++i) {
            assertThat(BcdUtils.readUint(new ByteArrayInputStream(PACKED_BCD_2[i]), PACKED_BCD_2[i].length), is(i));
        }
    }

    @Test
    void testBcdToStrIfLenIs2() {
        char[] chars = new char[2];

        for (int i = 10; i < PACKED_BCD_2.length; ++i) {
            BcdUtils.bcdToStr(PACKED_BCD_2[i], chars, 2);

            assertThat(new String(chars), is(Integer.toString(i)));
        }
    }

    @Test
    void testIntToBcdIfLenIs2() {
        byte[] bcd = new byte[1];

        for (int i = 10; i < 100; ++i) {
            BcdUtils.intToBcd(i, bcd);
            assertArrayEquals(PACKED_BCD_2[i], bcd);
        }
    }

    @Test
    void testWriteUintIfLenIs2() throws IOException {
        for (int i = 10; i < 100; ++i) {
            ByteArrayOutputStream out = new ByteArrayOutputStream(1);
            BcdUtils.writeUint(out, i, 1);

            assertArrayEquals(PACKED_BCD_2[i], out.toByteArray());
        }
    }

    @Test
    void testStrToBcdIfLenIs2() {
        byte[] bcd = new byte[1];

        for (int i = 10; i < 100; ++i) {
            BcdUtils.strToBcd(("" + i).toCharArray(), 2, bcd);
            assertArrayEquals(PACKED_BCD_2[i], bcd);
        }
    }

    @Test
    void testBcdToIntIfLenIs3() {
        for (int i = 100; i < PACKED_BCD_3.length; ++i) {
            assertThat(BcdUtils.bcdToInt(PACKED_BCD_3[i]), is(i));
        }
    }

    @Test
    void testReadUintIfLenIs3() throws IOException {
        for (int i = 100; i < PACKED_BCD_3.length; ++i) {
            assertThat(BcdUtils.readUint(new ByteArrayInputStream(PACKED_BCD_3[i]), PACKED_BCD_3[i].length), is(i));
        }
    }

    @Test
    void testBcdToStrIfLenIs3() {
        char[] chars = new char[4];

        for (int i = 100; i < PACKED_BCD_3.length; ++i) {
            BcdUtils.bcdToStr(PACKED_BCD_3[i], chars, 4);

            assertThat(new String(chars), is("0" + i));
        }
    }

    @Test
    void testIntToBcdIfLenIs3() {
        byte[] bcd = new byte[2];

        for (int i = 100; i < 1000; ++i) {
            BcdUtils.intToBcd(i, bcd);
            assertArrayEquals(PACKED_BCD_3[i], bcd);
        }
    }

    @Test
    void testWriteUintIfLenIs3() throws IOException {
        for (int i = 100; i < 1000; ++i) {
            ByteArrayOutputStream out = new ByteArrayOutputStream(2);
            BcdUtils.writeUint(out, i, 2);

            assertArrayEquals(PACKED_BCD_3[i], out.toByteArray());
        }
    }

    @Test
    void testStrToBcdIfLenIs3() {
        byte[] bcd = new byte[2];

        for (int i = 100; i < 1000; ++i) {
            BcdUtils.strToBcd(("" + i).toCharArray(), 3, bcd);
            assertArrayEquals(PACKED_BCD_3[i], bcd);
        }
    }

    @Test
    void testBcdToIntIfLenIs4() {
        for (int i = 1000; i < PACKED_BCD_4.length; ++i) {
            assertThat(BcdUtils.bcdToInt(PACKED_BCD_4[i]), is(i));
        }
    }

    @Test
    void testReadUintIfLenIs4() throws IOException {
        for (int i = 1000; i < PACKED_BCD_4.length; ++i) {
            assertThat(BcdUtils.readUint(new ByteArrayInputStream(PACKED_BCD_4[i]), PACKED_BCD_4[i].length), is(i));
        }
    }

    @Test
    void testBcdToStrIfLenIs4() {
        char[] chars = new char[4];

        for (int i = 1000; i < PACKED_BCD_4.length; ++i) {
            BcdUtils.bcdToStr(PACKED_BCD_4[i], chars, 4);

            assertThat(new String(chars), is(Integer.toString(i)));
        }
    }

    @Test
    void testIntToBcdIfLenIs4() {
        byte[] bcd = new byte[2];

        for (int i = 1000; i < 10000; ++i) {
            BcdUtils.intToBcd(i, bcd);
            assertArrayEquals(PACKED_BCD_4[i], bcd);
        }
    }

    @Test
    void testWriteUintIfLenIs4() throws IOException {
        for (int i = 1000; i < 10000; ++i) {
            ByteArrayOutputStream out = new ByteArrayOutputStream(2);
            BcdUtils.writeUint(out, i, 2);

            assertArrayEquals(PACKED_BCD_4[i], out.toByteArray());
        }
    }

    @Test
    void testStrToBcdIfLenIs4() {
        byte[] bcd = new byte[2];

        for (int i = 1000; i < 10000; ++i) {
            BcdUtils.strToBcd(("" + i).toCharArray(), 4, bcd);
            assertArrayEquals(PACKED_BCD_4[i], bcd);
        }
    }
}
