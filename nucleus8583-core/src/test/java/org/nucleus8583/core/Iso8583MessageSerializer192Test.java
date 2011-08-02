package org.nucleus8583.core;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.util.BitmapHelper;

public class Iso8583MessageSerializer192Test {
	private Iso8583MessageSerializer serializer;

	private String packed;

	private Iso8583Message unpacked;

	@Before
	public void initialize() throws Exception {
		serializer = new Iso8583MessageSerializer("classpath:META-INF/codec8583L.xml");

		packed = "020040000000000100008000000000000000060300000049998000000001000000400000000000000003301002000000000000000000000";

		unpacked = new Iso8583Message();
		unpacked.setMti("0200");
		unpacked.set(2, "030000");
		unpacked.set(48, "9998");
		unpacked.set(164, "301");

		byte[] bs = BitmapHelper.create(96);
		BitmapHelper.set(bs, 10);

		unpacked.set(190, bs);
	}

	@Test
	public void testRead() throws Exception {
		Iso8583Message unpacked = new Iso8583Message();

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
