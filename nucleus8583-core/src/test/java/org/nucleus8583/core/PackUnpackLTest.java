package org.nucleus8583.core;

import static org.junit.Assert.assertEquals;

import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

public class PackUnpackLTest {
	private Iso8583MessageFactory messageFactory;

	private String packed;

	private Iso8583Message unpacked;

	@Before
	public void initialize() throws Exception {
		messageFactory = new Iso8583MessageFactory(
				"classpath:META-INF/codec8583L.xml");

		packed = "020040000000000100008000000000000000060300000049998000000001000000400000000000000003301002000000000";

		unpacked = messageFactory.createMessage();
		unpacked.setMti("0200");
		unpacked.set(2, "030000");
		unpacked.set(48, "9998");
		unpacked.set(164, "301");
		
		BitSet bs = new BitSet();
		bs.set(10, true);
		unpacked.set(190, bs);
	}

	@Test
	public void unpackTest() throws Exception {
		Iso8583Message unpacked = messageFactory.createMessage();
		unpacked.unpack(packed.getBytes());

		assertEquals(this.unpacked, unpacked);
	}

	@Test
	public void packTest() throws Exception {
		byte[] packed = unpacked.pack();
		assertEquals(this.packed, new String(packed));
	}
}
