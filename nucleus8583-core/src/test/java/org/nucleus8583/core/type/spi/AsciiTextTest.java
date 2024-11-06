package org.nucleus8583.core.type.spi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nucleus8583.core.type.Alignment;
import org.nucleus8583.core.type.Type;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings({"unchecked", "raw"})
class AsciiTextTest {

	private Type alignL;

	private Type alignR;

	private Type alignN;

	@BeforeEach
	void beforeEach() {
        alignL = new AsciiText.Builder()
				.withAlignment(Alignment.TRIMMED_LEFT)
				.withLength(2)
				.build();

        alignR = new AsciiText.Builder()
				.withAlignment(Alignment.TRIMMED_RIGHT)
				.withPadWith(" ")
				.withLength(2)
				.build();

        alignN = new AsciiText.Builder()
				.withAlignment(Alignment.NONE)
				.withLength(2)
				.build();
	}

	@Test
	void packBinary() {
		assertThrows(ClassCastException.class, () -> {
			alignL.serializer().write(new ByteArrayOutputStream(), new byte[0]);
		});
	}

	@Test
	void packStringTooLong() throws Exception {
		String errorMsg = null;

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			try {
				alignL.serializer().write(out, "1124134=2343434");
			} catch (IllegalArgumentException ex) {
				errorMsg = ex.getMessage();
			}
		}

		assertThat(errorMsg, is("value too long, expected 2 but actual is 15"));
	}

	@Test
	void packStringNoPad() throws Exception {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			alignL.serializer().write(out, "20");

			assertThat(out.toString(StandardCharsets.UTF_8), is("20"));
		}

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			alignR.serializer().write(out, "20");

			assertThat(out.toString(StandardCharsets.UTF_8), is("20"));
		}

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			alignN.serializer().write(out, "20");

			assertThat(out.toString(StandardCharsets.UTF_8), is("20"));
		}
	}

	@Test
	void packEmptyString() throws Exception {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			alignL.serializer().write(out, "");

			assertThat(out.toString(StandardCharsets.UTF_8), is("  "));
		}

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			alignR.serializer().write(out, "");

			assertThat(out.toString(StandardCharsets.UTF_8), is("  "));
		}

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			alignN.serializer().write(out, "");

			assertThat(out.toString(StandardCharsets.UTF_8), is("  "));
		}
	}

	@Test
	void packStringWithPad() throws Exception {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			alignL.serializer().write(out, "j");
			out.flush();

			assertThat(out.toString(StandardCharsets.UTF_8), is("j "));
		}

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			alignR.serializer().write(out, "j");
			out.flush();

			assertThat(out.toString(StandardCharsets.UTF_8), is(" j"));
		}

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			alignN.serializer().write(out, "j");
			out.flush();

			assertThat(out.toString(StandardCharsets.UTF_8), is("j "));
		}
	}

	@Test
	void packStringOverflow() {
		assertThrows(IllegalArgumentException.class, () -> {
			alignL.serializer().write(new ByteArrayOutputStream(), "300");
		});
	}

	@Test
	void unpackStringNoUnpad() throws Exception {
		assertThat(alignL.serializer().read(new ByteArrayInputStream("20".getBytes())), is("20"));

		assertThat(alignR.serializer().read(new ByteArrayInputStream("20".getBytes())), is("20"));

		assertThat(alignN.serializer().read(new ByteArrayInputStream("20".getBytes())), is("20"));
	}

	@Test
	void unpackEmptyString() throws Exception {
		assertThat(alignL.serializer().read(new ByteArrayInputStream("  ".getBytes())), is(""));

		assertThat(alignR.serializer().read(new ByteArrayInputStream("  ".getBytes())), is(""));

		assertThat(alignN.serializer().read(new ByteArrayInputStream("  ".getBytes())), is("  "));
	}

	@Test
	void unpackStringUnpad() throws Exception {
		assertThat(alignL.serializer().read(new ByteArrayInputStream("j ".getBytes())), is("j"));

		assertThat(alignR.serializer().read(new ByteArrayInputStream(" j".getBytes())), is("j"));

		assertThat(alignN.serializer().read(new ByteArrayInputStream("j ".getBytes())), is("j "));
	}

	@Test
	void unpackStringUnpadOverflow() throws Exception {
		assertThat(alignL.serializer().read(new ByteArrayInputStream("j kl".getBytes())), is("j"));

		assertThat(alignR.serializer().read(new ByteArrayInputStream("j kl".getBytes())), is("j "));

		assertThat(alignN.serializer().read(new ByteArrayInputStream("j kl".getBytes())), is("j "));
	}
}
