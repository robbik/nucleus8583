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
		unpacked.mti("0200");
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
		byte[] raw1 = "0200423800080A010000000000000000000004312501041324311     1324310104C010000001762745214  0003701000abcdefghijkl                    ".getBytes();
		byte[] raw2 = "0200423800080A0100000000000000000000043125010413 000243111324310104C 010000001762745214  0003701000abcdefghijkl                    ".getBytes();

		Message msg1 = new Message();

		serializer.read(raw1, msg1);

		assertThat(msg1.mti(), is("0200"));
		assertThat(msg1.get(2), is(""));
		assertThat(msg1.get(7), is("0000000000"));
		assertThat(msg1.get(11), is("000004"));
		assertThat(msg1.get(12), is("312501"));
		assertThat(msg1.get(13), is("0413"));
		assertThat(msg1.get(29), is("24311    "));
		assertThat(msg1.get(37), is(" 1324310104C"));
		assertThat(msg1.get(39), is("01"));
		assertThat(msg1.get(48), is(""));

		MessageSerializer serializer2 = MessageSerializer.get("test1");

		assertThat(serializer2, notNullValue());

		Message msg2 = new Message();

		serializer2.read(raw2, msg2);

		assertThat(msg2.mti(), is("0200"));
		assertThat(msg2.get(2), is(""));
		assertThat(msg2.get(7), is("0000000000"));
		assertThat(msg2.get(11), is("4"));
		assertThat(msg2.get(12), is("312501"));
		assertThat(msg2.get(13), is("0413"));
		assertThat(msg2.get(29), is(" 24311"));
		assertThat(msg2.get(37), is("1324310104C"));
		assertThat(msg2.get(39), is("01"));
		assertThat(msg2.get(48), is(""));
	}
}
