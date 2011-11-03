package org.nucleus8583.core;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.util.BitmapHelper;

public class MessageSerializer128Test {
	private MessageSerializer serializer;

	private String packed;

	private byte[] bpacked;

	private Message unpacked;

	@Before
	public void initialize() throws Exception {
		serializer = new MessageSerializer("file:src/test/resources/META-INF/codec8583.xml");

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
	public void testCreateNewInstanceIfNoF28Defined() throws Exception {
		String errorMsg = null;

		try {
			MessageSerializer.create("classpath:META-INF/codec8583-4.xml");
		} catch (IllegalArgumentException ex) {
			errorMsg = ex.getMessage();
		}

		assertEquals("field #28 is not defined", errorMsg);
	}

	@Test
	public void testReadFromBytes() throws Exception {
		Message unpacked = new Message();
		serializer.read(bpacked, unpacked);

		assertEquals(this.unpacked, unpacked);
	}

	@Test
	public void testReadFromString() throws Exception {
		Message unpacked = new Message();
		serializer.read(bpacked, unpacked);

		assertEquals(this.unpacked, unpacked);
	}

	@Test(expected = IOException.class)
	public void testReadFromEmptyString() throws Exception {
		serializer.read("".getBytes(), unpacked);
	}

	@Test
	public void testReadAfterSerialized() throws Exception {
		Message unpacked = new Message();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		new ObjectOutputStream(out).writeObject(unpacked);

		unpacked = (Message) new ObjectInputStream(new ByteArrayInputStream(out.toByteArray())).readObject();
		serializer.read(bpacked, unpacked);

		assertEquals(this.unpacked, unpacked);
	}

	@Test
	public void testWriteToOutputStream() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		serializer.write(unpacked, baos);
		assertEquals(packed, new String(baos.toByteArray()));
	}

	@Test
	public void packTestAfterSerialized() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		new ObjectOutputStream(out).writeObject(unpacked);

		unpacked = (Message) new ObjectInputStream(new ByteArrayInputStream(out.toByteArray())).readObject();

		out = new ByteArrayOutputStream();
		serializer.write(unpacked, out);
		assertEquals(packed, out.toString());
	}
}
