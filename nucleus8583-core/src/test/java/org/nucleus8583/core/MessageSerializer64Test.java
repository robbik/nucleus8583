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
		serializer = new MessageSerializer("file:src/test/resources/META-INF/codec8583.xml");
		serializer2 = new MessageSerializer("file:src/test/resources/META-INF/codec8583-5.xml");

		packed = "020040000000000100010603000000499980000000000000000";
        packed2 = "40000000000100010603000000499980000000000000000";

		bpacked = packed.getBytes();
        bpacked2 = packed2.getBytes();

		unpacked = new Message();
		unpacked.setMti("0200");
		unpacked.set(2, "030000");
		unpacked.set(48, "9998");
		unpacked.set(64, BitmapHelper.create(64));

        unpacked2 = new Message();
        unpacked2.set(2, "030000");
        unpacked2.set(48, "9998");
        unpacked2.set(64, BitmapHelper.create(64));
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
    public void testReadFromBytes2() throws Exception {
        Message unpacked = new Message();
        serializer2.read(bpacked2, unpacked);

        assertEquals(this.unpacked2, unpacked);
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

        assertEquals(this.unpacked2, unpacked);
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
		assertEquals(this.packed, out.toString());
	}
}
