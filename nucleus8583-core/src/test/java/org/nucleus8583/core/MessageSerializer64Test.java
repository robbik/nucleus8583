package org.nucleus8583.core;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.util.BitmapHelper;

import rk.commons.beans.factory.BeanInstantiationException;

public class MessageSerializer64Test {

	private MessageSerializer serializer;

    private MessageSerializer serializer2;

	private String packed;

    private String packed2;

	private byte[] bpacked;

    private byte[] bpacked2;

	private Message unpacked;

    private Message unpacked2;

	@Before
	public void initialize() throws Exception {
		serializer = new XmlContext("file:src/test/resources/META-INF/codec8583.xml").getMessageSerializer();
		serializer2 = new XmlContext("file:src/test/resources/META-INF/codec8583-noMTI.xml").getMessageSerializer();

		packed = "0200400000000001000106030000004999800000000000000000000000000000000";
        packed2 = "400000000001000106030000004999800000000000000000000000000000000";

		bpacked = packed.getBytes();
        bpacked2 = packed2.getBytes();

		unpacked = new Message();
		unpacked.setMti("0200");
		unpacked.set(2, "030000");
		unpacked.set(48, "9998");
		unpacked.set(64, BitmapHelper.create(128));

        unpacked2 = new Message();
        unpacked2.set(2, "030000");
        unpacked2.set(48, "9998");
        unpacked2.set(64, BitmapHelper.create(128));
	}

	@Test
	public void testCreateNewInstanceIfNoF28Defined() throws Exception {
		String errorMsg = null;

		try {
			new XmlContext("classpath:META-INF/codec8583-no28.xml").getMessageSerializer();
		} catch (BeanInstantiationException ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			
			errorMsg = sw.toString();
		}

		if ((errorMsg == null) || !errorMsg.contains("field #28 is not defined")) {
			throw new AssertionFailedError(errorMsg);
		}
	}

	@Test
	public void testReadFromBytes() throws Exception {
		Message unpacked = new Message();
		serializer.read(bpacked, unpacked);

		assertEquals(this.unpacked, unpacked);
	}

    @Test
    public void testReadFromBytes2() throws Exception {
        Message unpacked = new Message();
        serializer2.read(bpacked2, unpacked);

        assertEquals(unpacked2, unpacked);
    }

	@Test
	public void testReadFromString() throws Exception {
		Message unpacked = new Message();
		serializer.read(bpacked, unpacked);

		assertEquals(this.unpacked, unpacked);
	}

    @Test
    public void testReadFromString2() throws Exception {
        Message unpacked = new Message();
        serializer2.read(bpacked2, unpacked);

        assertEquals(unpacked2, unpacked);
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
    public void testWriteToOutputStream2() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        serializer2.write(unpacked2, baos);
        assertEquals(packed2, new String(baos.toByteArray()));
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
