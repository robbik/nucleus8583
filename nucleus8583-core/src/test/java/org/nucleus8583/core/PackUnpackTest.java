package org.nucleus8583.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

	private Iso8583Message draftUnpacked;

	@Before
	public void initialize() throws Exception {
		messageFactory = new Iso8583MessageFactory(
				"classpath:META-INF/codec8583.xml");

		packed = "0200C00000000001000104000000000000000603000000499980000000000000000301";

		unpacked = messageFactory.createMessage();
		unpacked.setMti("0200");
		unpacked.set(2, "030000");
		unpacked.set(48, "9998");
		unpacked.set(64, new BitSet());
		unpacked.set(70, "301");

		draftUnpacked = new Iso8583Message();
		draftUnpacked.setMti("0200");
		draftUnpacked.set(2, "030000");
		draftUnpacked.set(48, "9998");
		draftUnpacked.set(64, new BitSet());
		draftUnpacked.set(70, "301");
	}

	@Test
	public void publicTest1() throws Exception {
		Iso8583Message msg = new Iso8583MessageFactory("classpath:META-INF/codec8583-qs.xml").createMessage();
		msg.setMti("0800");
		msg.set(7, "0627180510");
		msg.set(11, "123456");
		msg.set(70, "301");
		
		byte[] packed = msg.pack();
		assertEquals("0800822000000000000004000000000000000627180510123456301", new String(packed));
	}

	@Test
	public void publicTest2() throws Exception {
		Iso8583MessageFactory factory = new Iso8583MessageFactory("classpath:META-INF/codec8583-qs.xml");
		
		Iso8583Message msg = factory.createMessage();
		msg.setMti("0800");
		msg.set(7, "0627180510");
		msg.set(11, "123456");
		
		byte[] packed = msg.pack();
		assertEquals("080002200000000000000627180510123456", new String(packed));
		
		Iso8583Message msg2 = factory.createMessage();
		msg2.unpack("080002200000000000000627180510123456");
		
		assertEquals(msg, msg2);
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

		unpacked = new Iso8583Message();

		boolean error = false;
		try {
			unpacked.unpack("");
		} catch (Throwable t) {
			error = true;
		}
		assertTrue(error);

		unpacked.attach(messageFactory);
		unpacked.unpack(packed);
		assertEquals(this.unpacked, unpacked);
	}

	@Test
	public void unpackFromEmptyStringTest() throws Exception {
		Iso8583Message unpacked = messageFactory.createMessage();
		unpacked.unpack("");
		assertEquals(messageFactory.createMessage(), unpacked);

		unpacked.detach();

		boolean error = false;
		try {
			unpacked.unpack("");
		} catch (Throwable t) {
			error = true;
		}
		assertTrue(error);

		unpacked = new Iso8583Message();

		error = false;
		try {
			unpacked.unpack("");
		} catch (Throwable t) {
			error = true;
		}
		assertTrue(error);

		unpacked.attach(messageFactory);
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

		unpacked.attach(messageFactory);
		unpacked.unpack(packed.getBytes());

		assertEquals(this.unpacked, unpacked);
	}

	@Test
	public void pack2Test() throws Exception {
		String errorMsg = null;

		try {
			new Iso8583MessageFactory("classpath:META-INF/codec8583-4.xml")
					.createMessage();
		} catch (IllegalArgumentException ex) {
			errorMsg = ex.getMessage();
		}

		assertEquals("field #28 is not defined", errorMsg);
	}

	@Test
	public void packTest() throws Exception {
		byte[] packed = unpacked.pack();
		assertEquals(this.packed, new String(packed));

		boolean error = false;
		try {
			draftUnpacked.pack();
		} catch (Throwable t) {
			error = true;
		}
		assertTrue(error);

		draftUnpacked.attach(messageFactory);
		packed = draftUnpacked.pack();
		assertEquals(this.packed, new String(packed));

		draftUnpacked.detach();

		error = false;
		try {
			draftUnpacked.pack();
		} catch (Throwable t) {
			error = true;
		}
		assertTrue(error);
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

		unpacked.attach(messageFactory);

		byte[] packed = unpacked.pack();
		assertEquals(this.packed, new String(packed));
	}
}
