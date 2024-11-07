package org.nucleus8583.core;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nucleus8583.core.util.BitmapHelper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class MessageSerializer192Test {
	private MessageSerializer serializer;

	private String packed;

	private Message unpacked;

	@BeforeEach
	void beforeEach() {
		serializer = MessageSerializer.get("codec8583L");
        assertThat(serializer, notNullValue());

		packed = "0200C000000000010000800000000000000006030000004999800000000100000043301002000000000000000000000000000000000000000000000";

		unpacked = new Message();
		unpacked.mti("0200");
		unpacked.set(2, "030000");
		unpacked.set(48, "9998");
		unpacked.set(164, "301");

		byte[] bs = BitmapHelper.create(192);
		BitmapHelper.set(bs, 10);

		unpacked.set(190, bs);
	}

	@Test
	void testRead() throws Exception {
		Message unpacked = new Message();

		serializer.read(packed.getBytes(), unpacked);
		assertThat(unpacked, equalTo(this.unpacked));
	}

	@Test
	void testWrite() throws Exception {
		ByteArrayOutputStream sw = new ByteArrayOutputStream();

		serializer.write(unpacked, sw);
		assertThat(sw.toString(StandardCharsets.UTF_8), is(packed));
	}
}
