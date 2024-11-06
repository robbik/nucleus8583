package org.nucleus8583.core.type.spi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nucleus8583.core.type.Alignment;
import org.nucleus8583.core.type.Type;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SuppressWarnings({"unchecked", "raw"})
class AsciiAmountTest {

	private Type type;
	
	@BeforeEach
	void beforeEach() {
		type = new AsciiAmount.Builder()
				.withLength(9)
				.withAlignment(Alignment.UNTRIMMED_RIGHT)
				.build();
	}
	
	@Test
	void testWrite() throws Exception {
		try (ByteArrayOutputStream mock = new ByteArrayOutputStream()) {
			type.serializer().write(mock, "775525");

			assertThat(mock.toString(StandardCharsets.UTF_8), is("700075525"));
		}
	}
	
	@Test
	void testWrite2() throws Exception {
		try (ByteArrayOutputStream mock = new ByteArrayOutputStream()) {
			type.serializer().write(mock, "1");

			assertThat(mock.toString(StandardCharsets.UTF_8), is("100000000"));
		}
	}
}
