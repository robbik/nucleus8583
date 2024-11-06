package org.nucleus8583.core.type.spi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nucleus8583.core.type.Type;
import org.nucleus8583.core.util.BitmapHelper;
import org.nucleus8583.core.util.NullOutputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings({"unchecked", "raw"})
class Base16BinaryTest {

	private Type binaryField;

	@BeforeEach
	void before() {
		binaryField = new Base16Binary.Builder().withLength(1).build();
	}

	@Test
	void packString() {
        assertThrows(ClassCastException.class, () -> {
            binaryField.serializer().write(new NullOutputStream(), "a");
        });
	}

	@Test
	void packEmptyBinary() {
        assertThrows(IllegalArgumentException.class, () -> {
            binaryField.serializer().write(new NullOutputStream(), new byte[0]);
        });
    }

	@Test
	void packBinary() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

//		binaryField.write(out, new byte[0]);
//		assertEquals("00", out.toString());

		byte[] bs = BitmapHelper.create(8);
		BitmapHelper.set(bs, 0);

		binaryField.serializer().write(out, bs);
		assertThat(out.toString(StandardCharsets.US_ASCII), is("80"));

		BitmapHelper.clear(bs);
		BitmapHelper.set(bs, 1);

		binaryField.serializer().write(out, bs);
        assertThat(out.toString(StandardCharsets.US_ASCII), is("8040"));

		BitmapHelper.clear(bs);
		BitmapHelper.set(bs, 2);

		binaryField.serializer().write(out, bs);
        assertThat(out.toString(StandardCharsets.US_ASCII), is("804020"));

		BitmapHelper.clear(bs);
		BitmapHelper.set(bs, 3);

		binaryField.serializer().write(out, bs);
        assertThat(out.toString(StandardCharsets.US_ASCII), is("80402010"));

		BitmapHelper.clear(bs);
		BitmapHelper.set(bs, 0);
        BitmapHelper.set(bs, 1);
        BitmapHelper.set(bs, 2);
        BitmapHelper.set(bs, 3);

		binaryField.serializer().write(out, bs);
        assertThat(out.toString(StandardCharsets.US_ASCII), is("80402010F0"));
	}

	@Test
	void unpackEmptyBinary1() {
        assertThrows(EOFException.class, () -> {
            binaryField.serializer().read(new ByteArrayInputStream("".getBytes()));
        });
	}
}
