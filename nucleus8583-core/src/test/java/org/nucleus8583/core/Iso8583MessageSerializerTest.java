package org.nucleus8583.core;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

public class Iso8583MessageSerializerTest {
	private Iso8583MessageSerializer serializer;

	private String packed;

	private Iso8583Message unpacked;

	@Before
	public void initialize() throws Exception {
		serializer = new Iso8583MessageSerializer("classpath:META-INF/codec8583.xml");

		packed = "0200C00000000001000104000000000000000603000000499980000000000000000301";

		unpacked = new Iso8583Message();
		unpacked.setMti("0200");
		unpacked.set(2, "030000");
		unpacked.set(48, "9998");
		unpacked.set(64, new BitSet());
		unpacked.set(70, "301");
	}

	@Test(expected = RuntimeException.class)
	public void testCreateNewInstanceIfEncodingIsUnsupported() {
		Iso8583MessageSerializer.create("classpath:META-INF/error8583.xml");
	}

	@Test
	public void testCreateNewInstanceIfNoF28Defined() throws Exception {
		String errorMsg = null;

		try {
			Iso8583MessageSerializer.create("classpath:META-INF/codec8583-4.xml");
		} catch (IllegalArgumentException ex) {
			errorMsg = ex.getMessage();
		}

		assertEquals("field #28 is not defined", errorMsg);
	}

	@Test
	public void testReadFromBytes() throws Exception {
		Iso8583Message unpacked = new Iso8583Message();
		serializer.read(packed.getBytes(), unpacked);

		assertEquals(this.unpacked, unpacked);
	}

	@Test
	public void testReadFromString() throws Exception {
		Iso8583Message unpacked = new Iso8583Message();
		serializer.read(packed, unpacked);

		assertEquals(this.unpacked, unpacked);
	}

	@Test(expected = IOException.class)
	public void testReadFromEmptyString() throws Exception {
		serializer.read("", unpacked);
	}

	@Test
	public void testReadAfterSerialized() throws Exception {
		Iso8583Message unpacked = new Iso8583Message();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		new ObjectOutputStream(out).writeObject(unpacked);

		unpacked = (Iso8583Message) new ObjectInputStream(new ByteArrayInputStream(out.toByteArray())).readObject();
		serializer.read(packed, unpacked);

		assertEquals(this.unpacked, unpacked);
	}

	@Test
	public void testWriteToWriter() throws Exception {
		StringWriter sw = new StringWriter();

		serializer.write(unpacked, sw);
		assertEquals(packed, sw.toString());
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

		unpacked = (Iso8583Message) new ObjectInputStream(new ByteArrayInputStream(out.toByteArray())).readObject();

		StringWriter sw = new StringWriter();
		serializer.write(unpacked, sw);
		assertEquals(this.packed, sw.toString());
	}
}
