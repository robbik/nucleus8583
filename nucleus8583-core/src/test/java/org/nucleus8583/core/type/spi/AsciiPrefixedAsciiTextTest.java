package org.nucleus8583.core.type.spi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nucleus8583.core.type.Type;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings({"unchecked", "raw"})
class AsciiPrefixedAsciiTextTest {

	private Type stringField;

	@BeforeEach
	void beforeEach() {
		stringField = new AsciiPrefixedAsciiText.Builder()
				.withPrefixLength(1)
				.withMaxLength(9)
				.build();
	}

	@Test
	public void packBitmap() {
		assertThrows(UnsupportedOperationException.class, () -> {
			stringField.serializer().writeBitmap(new ByteArrayOutputStream(), new byte[0], 0, 0);
		});
	}

	@Test
	public void unpackBitmap() throws Exception {
		assertThrows(UnsupportedOperationException.class, () -> {
			stringField.serializer().readBitmap(new ByteArrayInputStream("a".getBytes()), new byte[0], 0, 0);
		});
	}

	@Test
	void packString() throws Exception {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			stringField.serializer().write(out, "20");

			assertThat(out.toString(StandardCharsets.UTF_8), is("220"));
		}
	}

	@Test
	void packEmptyString() throws Exception {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			stringField.serializer().write(out, "");

			assertThat(out.toString(StandardCharsets.UTF_8), is("0"));
		}
	}

	@Test
	public void packStringOverflow() {
		assertThrows(IllegalArgumentException.class, () -> {
			try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
				stringField.serializer().write(out, "abcdefghji");
			}
		});
	}

	@Test
	void unpackString() throws Exception {
		assertThat(stringField.serializer().read(new ByteArrayInputStream("220".getBytes())), is("20"));
	}

	@Test
	void unpackEmptyString() throws Exception {
		assertThat(stringField.serializer().read(new ByteArrayInputStream("0".getBytes())), is(""));
	}

	@Test
	void unpackStringUnpadOverflow() {
		assertThrows(EOFException.class, () -> {
			stringField.serializer().read(new ByteArrayInputStream("5ab".getBytes()));
		});
	}
}
