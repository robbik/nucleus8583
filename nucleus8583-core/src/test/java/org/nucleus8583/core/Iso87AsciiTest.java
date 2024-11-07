package org.nucleus8583.core;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class Iso87AsciiTest {

	@Test
	void case1() throws Exception {
		Message msg = new Message();
		msg.mti("0330");
		msg.set(2, "suka2 ");
		msg.set(4, "89");

		MessageSerializer serializer = MessageSerializer.get("iso87ascii");
        assertThat(serializer, notNullValue());

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		serializer.write(msg, out);

		assertThat(out.toString(StandardCharsets.UTF_8), is("0330500000000000000006suka2 000000000089"));
	}
}
