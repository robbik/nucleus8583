package org.nucleus8583.core.type.spi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nucleus8583.core.type.Type;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings({"unchecked", "raw"})
class AsciiPrefixedBase16BinaryTest {

	private Type binaryL;

    private Type binaryLL;

    private Type binaryLLL;

	@BeforeEach
	void before() {
		binaryL = new AsciiPrefixedBase16Binary.Builder()
                .withPrefixLength(1)
                .withMaxLength(9)
                .build();

		binaryLL = new AsciiPrefixedBase16Binary.Builder()
                .withPrefixLength(2)
                .withMaxLength(99)
                .build();

		binaryLLL = new AsciiPrefixedBase16Binary.Builder()
                .withPrefixLength(3)
                .withMaxLength(999)
                .build();
	}

    @Test
	void packBinary() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
		binaryL.serializer().write(out, new byte[] { 0x20 });

        assertThat(out.toString(StandardCharsets.US_ASCII), is("220"));

        out = new ByteArrayOutputStream();
        binaryLL.serializer().write(out, new byte[] { 0x20 });

        assertThat(out.toString(StandardCharsets.US_ASCII), is("0220"));

        out = new ByteArrayOutputStream();
        binaryLLL.serializer().write(out, new byte[] { 0x20 });

        assertThat(out.toString(StandardCharsets.US_ASCII), is("00220"));
	}

    @Test
	void packString1() {
        assertThrows(ClassCastException.class, () -> {
            binaryL.serializer().write(new ByteArrayOutputStream(), "");
        });
	}

    @Test
    void packString2() {
        assertThrows(ClassCastException.class, () -> {
            binaryLL.serializer().write(new ByteArrayOutputStream(), "");
        });
    }

    @Test
    void packString3() {
        assertThrows(ClassCastException.class, () -> {
            binaryLLL.serializer().write(new ByteArrayOutputStream(), "");
        });
    }

	@Test
	void packEmptyBinary() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		binaryL.serializer().write(out, new byte[0]);

		assertThat(out.toString(StandardCharsets.US_ASCII), is("0"));

        out = new ByteArrayOutputStream();
        binaryLL.serializer().write(out, new byte[0]);

        assertThat(out.toString(StandardCharsets.US_ASCII), is("00"));

        out = new ByteArrayOutputStream();
        binaryLLL.serializer().write(out, new byte[0]);

        assertThat(out.toString(StandardCharsets.US_ASCII), is("000"));
	}

	@Test
	void packBinaryOverflow() {
        assertThrows(IllegalArgumentException.class, () -> {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            binaryL.serializer().write(out, new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
                    0x07, 0x08, 0x09, 0x0A });
        });
	}

	@Test
	void unpackBinary() throws Exception {
	    byte[] val = (byte[]) binaryL.serializer().read(new ByteArrayInputStream("120".getBytes()));
	    assertThat(val.length, is(1));
		assertThat((int) val[0], is(0x20));

        val = (byte[]) binaryLL.serializer().read(new ByteArrayInputStream("0120".getBytes()));
        assertThat(val.length, is(1));
        assertThat((int) val[0], is(0x20));

        val = (byte[]) binaryLLL.serializer().read(new ByteArrayInputStream("00120".getBytes()));
        assertThat(val.length, is(1));
        assertThat((int) val[0], is(0x20));
	}

	@Test
	void unpackEmptyBinary() throws Exception {
        assertArrayEquals(new byte[0], (byte[]) binaryL.serializer().read(new ByteArrayInputStream("0".getBytes())));
        assertArrayEquals(new byte[0], (byte[]) binaryLL.serializer().read(new ByteArrayInputStream("00".getBytes())));
        assertArrayEquals(new byte[0], (byte[]) binaryLLL.serializer().read(new ByteArrayInputStream("000".getBytes())));
	}

	@Test
	void unpackBinaryOverflow() {
        assertThrows(EOFException.class, () -> {
            binaryL.serializer().read(new ByteArrayInputStream("5ab".getBytes()));
        });
	}
}
