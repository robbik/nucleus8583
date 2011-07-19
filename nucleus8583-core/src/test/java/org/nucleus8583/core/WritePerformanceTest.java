package org.nucleus8583.core;

import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.util.NullOutputStream;

// @Ignore
public class WritePerformanceTest {
	private Iso8583MessageSerializer serializer;

	private OutputStream nullOut;

	@Before
	public void initialize() throws Exception {
		serializer = new Iso8583MessageSerializer("classpath:META-INF/codec8583.xml");

		nullOut = new NullOutputStream();
	}

	private long measure(int loops) throws Exception {
		long startDate = System.currentTimeMillis();

		for (int i = loops - 1; i >= 0; --i) {
			Iso8583Message msg = new Iso8583Message();

			msg.setMti("0200");
			msg.set(2, "3125");
			msg.set(7, "0104132431");
			msg.set(11, "1");
			msg.set(12, "132431");
			msg.set(13, "0104");
			msg.set(29, "C01000000");
			msg.set(37, "1762745214");
			msg.set(39, "00");
			msg.set(48, "01000abcdefghijkl                    ");

			serializer.write(msg, nullOut);
		}
		long endDate = System.currentTimeMillis();

		return endDate - startDate;
	}

	@Test
	public void shouldVeryFast() throws Exception {
		int loops = 1000000;

		for (int i = 0; i < 1; ++i) {
			long elapsed = measure(loops);

			System.out.println("[write] loops / sec = " + (loops * 1000 / elapsed));
			Thread.yield();
		}
	}
}
