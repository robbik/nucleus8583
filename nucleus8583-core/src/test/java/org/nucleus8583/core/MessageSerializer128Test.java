package org.nucleus8583.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nucleus8583.core.util.BitmapHelper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageSerializer128Test {

	private MessageSerializer serializer;

	private String packed;

	private byte[] bpacked;

	private Message unpacked;

	@BeforeEach
	void beforeEach() {
		serializer = MessageSerializer.get("codec8583");
        assertThat(serializer, notNullValue());

		packed = "0200C000000000010001040000000000000006030000004999800000000000000000000000000000000301";
		bpacked = packed.getBytes();

		unpacked = new Message();
		unpacked.setMti("0200");
		unpacked.set(2, "030000");
		unpacked.set(48, "9998");
		unpacked.set(64, BitmapHelper.create(128));
		unpacked.set(70, "301");
	}

	@Test
	void testReadFromBytes() throws Exception {
		Message unpacked = new Message();
		serializer.read(bpacked, unpacked);

		assertThat(unpacked, equalTo(this.unpacked));
	}

	@Test
	void testReadFromString() throws Exception {
		Message unpacked = new Message();
		serializer.read(bpacked, unpacked);

		assertThat(unpacked, equalTo(this.unpacked));
	}

	@Test
	void testReadFromEmptyString() {
        assertThrows(IOException.class, () -> {
            serializer.read("".getBytes(), unpacked);
        });
	}

	@Test
	void testReadAfterSerialized() throws Exception {
		Message unpacked = new Message();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		new ObjectOutputStream(out).writeObject(unpacked);

		unpacked = (Message) new ObjectInputStream(new ByteArrayInputStream(out.toByteArray())).readObject();
		serializer.read(bpacked, unpacked);

		assertThat(unpacked, equalTo(this.unpacked));
	}

	@Test
	void testWriteToOutputStream() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		serializer.write(unpacked, baos);
		assertThat(baos.toString(StandardCharsets.UTF_8), is(packed));
	}

	@Test
	void packTestAfterSerialized() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		new ObjectOutputStream(out).writeObject(unpacked);

		unpacked = (Message) new ObjectInputStream(new ByteArrayInputStream(out.toByteArray())).readObject();

		out = new ByteArrayOutputStream();
		serializer.write(unpacked, out);
		assertThat(out.toString(StandardCharsets.UTF_8), is(packed));
	}

	@Test
	void testReadFromTC1() throws Exception {
		Message message = new Message();

		serializer.read("0200423800080A010000000000000000000004312501041324311     1324310104C010000001762745214  0003701000abcdefghijkl                    ".getBytes(), message);

		System.out.println(message);
	}
}
