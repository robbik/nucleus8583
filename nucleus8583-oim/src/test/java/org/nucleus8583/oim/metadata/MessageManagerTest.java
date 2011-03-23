package org.nucleus8583.oim.metadata;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.nucleus8583.Iso8583Message;
import org.nucleus8583.Iso8583MessageFactory;
import org.nucleus8583.oim.Iso8583MessageManager;

@Ignore
public class MessageManagerTest {
	private Iso8583MessageFactory factory;

	private Iso8583MessageManager manager;

	private Iso8583Message msg;

	@Before
	public void initialize() throws Exception {
		factory = new Iso8583MessageFactory(
				"file:src/test/resources/META-INF/nucleus8583.xml");

		manager = new Iso8583MessageManager(
				"file:src/test/resources/META-INF/oim-types.xml",
				"file:src/test/resources/META-INF/oim-sample.xml");

		msg = factory.createMessage();
	}

	@Test
	public void packShouldFast() throws Exception {
		Ipm1 ipm1 = new Ipm1();
		int loops = 1000000;

		long start = System.currentTimeMillis();
		for (int i = loops; i > 0; --i) {
			manager.convert("ipm1", ipm1, msg);
		}
		long elapsed = System.currentTimeMillis() - start;

		long memoryUsage = (Runtime.getRuntime().totalMemory() - Runtime
				.getRuntime().freeMemory());

		System.out.println("[pack] loops / sec: " + (loops * 1000 / elapsed));
		System.out.println("[pack] memory: " + memoryUsage);

		System.out.println("[pack] iso unpacked data:\n" + msg);
		System.out
				.println("[pack] iso packed data:\n" + new String(msg.pack()));
	}

	@Test
	public void unpackShouldFast() throws Exception {
		String packed = "0200403800080A0100001331254        0000021806160117C001800001762745214    03701000abcdefhgijklmn                  ";
		int loops = 1000000;

		msg.unpack(new StringReader(packed));

		Object pojo = null;

		long start = System.currentTimeMillis();
		for (int i = loops; i > 0; --i) {
			pojo = manager.convert("ipm1", msg);
		}
		long elapsed = System.currentTimeMillis() - start;

		long memoryUsage = (Runtime.getRuntime().totalMemory() - Runtime
				.getRuntime().freeMemory());

		System.out.println("[unpack] loops / sec: " + (loops * 1000 / elapsed));
		System.out.println("[unpack] memory: " + memoryUsage);
		System.out.println("[unpack] pojo:" + pojo);
	}
}
