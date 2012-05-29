package org.nucleus8583.core;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.util.BitmapHelper;

public class MessageSerializer192Test {
	private MessageSerializer serializer;

	private String packed;

	private Message unpacked;

	@Before
	public void initialize() throws Exception {
		serializer = new XmlMessageSerializerFactory("classpath:META-INF/codec8583L.xml").getMessageSerializer();

		packed = "0200C000000000010000800000000000000006030000004999800000000100000043301002000000000000000000000000000000000000000000000";

		unpacked = new Message();
		unpacked.setMti("0200");
		unpacked.set(2, "030000");
		unpacked.set(48, "9998");
		unpacked.set(164, "301");

		byte[] bs = BitmapHelper.create(192);
		BitmapHelper.set(bs, 10);

		unpacked.set(190, bs);
	}

	@Test
	public void testRead() throws Exception {
		Message unpacked = new Message();

		serializer.read(packed.getBytes(), unpacked);
		assertEquals(this.unpacked, unpacked);
	}

	@Test
	public void testWrite() throws Exception {
		ByteArrayOutputStream sw = new ByteArrayOutputStream();

		serializer.write(unpacked, sw);
		assertEquals(packed, sw.toString());
	}
}
