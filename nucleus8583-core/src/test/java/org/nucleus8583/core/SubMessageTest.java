package org.nucleus8583.core;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

public class SubMessageTest {

	@Test
	public void testNoSubMessage() throws Exception {
		Message msg = new Message();
		msg.setMti("0330");
		msg.set(2, "suka2 ");
		msg.set(4, "89");

		MessageSerializer serializer = new MessageSerializer("sub-message", "classpath:META-INF/sub-message.xml");

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		serializer.write(msg, out);

		assertEquals("0330500000000000000006suka2 89          ", new String(out.toByteArray()));
	}

	@Test
	public void testSubMessageWrite() throws Exception {
		Message subMsg = new Message();
		subMsg.set(2, "sub");
		
		Message msg = new Message();
		msg.setMti("0330");
		msg.set(2, "suka2 ");
		msg.set(4, "89");
		msg.set(48, subMsg);

		MessageSerializer serializer = new MessageSerializer("sub-message", "classpath:META-INF/sub-message.xml");

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		serializer.write(msg, out);

		assertEquals("0330500000000001000006suka2 89          021400000000000000003sub", new String(out.toByteArray()));
	}

	@Test
	public void testSubMessageRead() throws Exception {
		Message subMsg = new Message();
		subMsg.set(2, "sub");
		
		Message msg = new Message();
		msg.setMti("0330");
		msg.set(2, "suka2 ");
		msg.set(4, "89");
		msg.set(48, subMsg);

		MessageSerializer serializer = new MessageSerializer("sub-message", "classpath:META-INF/sub-message.xml");
		
		Message msg2 = new Message();
		
		serializer.read("0330500000000001000006suka2 89          021400000000000000003sub".getBytes(), msg2);
		
		assertEquals(msg, msg2);
		
		assertEquals(subMsg, msg.get(48));
	}
}
