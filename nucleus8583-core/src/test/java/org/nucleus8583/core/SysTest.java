package org.nucleus8583.core;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

public class SysTest {

	@Test
	public void sys1() throws Exception {
		Message msg = new Message();
		msg.setMti("0330");
		msg.set(2, "suka2 ");
		msg.set(4, "89");

		MessageSerializer serializer = new MessageSerializer("classpath:META-INF/sys1.xml");

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		serializer.write(msg, out);

		assertEquals("0330500000000000000006suka2 000000000089", new String(out.toByteArray()));
	}
}
