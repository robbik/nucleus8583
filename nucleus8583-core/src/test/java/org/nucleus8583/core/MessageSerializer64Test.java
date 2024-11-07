package org.nucleus8583.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nucleus8583.core.config.Codec8583No28SerializerConfig;
import org.nucleus8583.core.config.Configuration;
import org.nucleus8583.core.util.BitmapHelper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageSerializer64Test {

	private MessageSerializer serializer;

    private MessageSerializer serializer2;

	private String packed;

    private String packed2;

	private byte[] bpacked;

    private byte[] bpacked2;

	private Message unpacked;

    private Message unpacked2;

	@BeforeEach
	void beforeEach() {
		serializer = MessageSerializer.get("codec8583");
        assertThat(serializer, notNullValue());

		serializer2 = MessageSerializer.get("codec8583-noMTI");
        assertThat(serializer2, notNullValue());

		packed = "0200400000000001000106030000004999800000000000000000000000000000000";
        packed2 = "400000000001000106030000004999800000000000000000000000000000000";

		bpacked = packed.getBytes();
        bpacked2 = packed2.getBytes();

		unpacked = new Message();
		unpacked.mti("0200");
		unpacked.set(2, "030000");
		unpacked.set(48, "9998");
		unpacked.set(64, BitmapHelper.create(128));

        unpacked2 = new Message();
        unpacked2.set(2, "030000");
        unpacked2.set(48, "9998");
        unpacked2.set(64, BitmapHelper.create(128));
	}

	@Test
	void testCreateNewInstanceIfNoF28Defined() {
		String errorMsg = null;

		try {
			new Codec8583No28SerializerConfig().configure(Configuration.DEFAULT);
		} catch (IllegalArgumentException ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));

			errorMsg = sw.toString();
		}

		if ((errorMsg == null) || !errorMsg.contains("field #28 is not defined")) {
			throw new AssertionError(errorMsg);
		}
	}

	@Test
	void testReadFromBytes() throws Exception {
		Message unpacked = new Message();
		serializer.read(bpacked, unpacked);

		assertThat(unpacked, equalTo(this.unpacked));
	}

    @Test
    void testReadFromBytes2() throws Exception {
        Message unpacked = new Message();
        serializer2.read(bpacked2, unpacked);

        assertThat(unpacked, equalTo(unpacked2));
    }

	@Test
	void testReadFromString() throws Exception {
		Message unpacked = new Message();
		serializer.read(bpacked, unpacked);

		assertThat(unpacked, equalTo(this.unpacked));
	}

    @Test
    void testReadFromString2() throws Exception {
        Message unpacked = new Message();
        serializer2.read(bpacked2, unpacked);

        assertThat(unpacked, equalTo(unpacked2));
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
    void testWriteToOutputStream2() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        serializer2.write(unpacked2, baos);
        assertThat(baos.toString(StandardCharsets.UTF_8), is(packed2));
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
}
