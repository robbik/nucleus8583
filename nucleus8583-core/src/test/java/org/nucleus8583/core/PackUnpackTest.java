package org.nucleus8583.core;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

public class PackUnpackTest {
	private Iso8583MessageFactory messageFactory;

	private String packed;

	private Iso8583Message unpacked;

	@Before
	public void initialize() throws Exception {
		messageFactory = new Iso8583MessageFactory(
				"classpath:META-INF/codec8583.xml");

		packed = "02004000000000010001040000000000000006030000004999800000000301";

		unpacked = messageFactory.createMessage();
		unpacked.setMti("0200");
		unpacked.set(2, "030000");
		unpacked.set(48, "9998");
		unpacked.set(64, new BitSet());
		unpacked.set(70, "301");
	}

	@Test
	public void unpackFromBytesTest() throws Exception {
		Iso8583Message unpacked = messageFactory.createMessage();
		unpacked.unpack(packed.getBytes());

		assertEquals(this.unpacked, unpacked);
	}

	@Test
	public void unpackFromStringTest() throws Exception {
		Iso8583Message unpacked = messageFactory.createMessage();
		unpacked.unpack(packed);

		assertEquals(this.unpacked, unpacked);
	}

	@Test
	public void unpackFromEmptyStringTest() throws Exception {
		Iso8583Message unpacked = messageFactory.createMessage();
		unpacked.unpack("");

		assertEquals(messageFactory.createMessage(), unpacked);
	}

	@Test
	public void unpackTestAfterSerialized() throws Exception {
		Iso8583Message unpacked = messageFactory.createMessage();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream serializer = new ObjectOutputStream(out);

		serializer.writeObject(unpacked);

		unpacked = null;

		ObjectInputStream deserializer = new ObjectInputStream(
				new ByteArrayInputStream(out.toByteArray()));
		unpacked = (Iso8583Message) deserializer.readObject();

		unpacked.reattach(messageFactory);
		unpacked.unpack(packed.getBytes());

		assertEquals(this.unpacked, unpacked);
	}

	@Test
	public void packTest() throws Exception {
		byte[] packed = unpacked.pack();
		assertEquals(this.packed, new String(packed));
	}

	@Test
	public void packTestAfterSerialized() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream serializer = new ObjectOutputStream(out);

		serializer.writeObject(unpacked);

		unpacked = null;

		ObjectInputStream deserializer = new ObjectInputStream(
				new ByteArrayInputStream(out.toByteArray()));
		unpacked = (Iso8583Message) deserializer.readObject();

		unpacked.reattach(messageFactory);

		byte[] packed = unpacked.pack();
		assertEquals(this.packed, new String(packed));
	}
}
