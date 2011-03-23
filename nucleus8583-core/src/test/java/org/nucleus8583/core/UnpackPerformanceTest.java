package org.nucleus8583.core;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.nucleus8583.core.Iso8583Message;
import org.nucleus8583.core.Iso8583MessageFactory;

@Ignore
public class UnpackPerformanceTest {
	private Iso8583MessageFactory messageFactory;

	private InputStream ipacked;

	@Before
	public void initialize() throws Exception {
		messageFactory = new Iso8583MessageFactory(
				"src/test/resources/META-INF/codec8583.xml");

		final byte[] bpacked = "0200423800080A010000000000000000000004312501041324311     1324310104C010000001762745214  0003701000abcdefghijkl                    "
				.getBytes();

		ipacked = new InputStream() {
			private int readerIndex = 0;

			private int remaining = bpacked.length;

			public int read() throws IOException {
				if (remaining == 0) {
					return -1;
				}

				int readb = bpacked[readerIndex] & 0xFF;

				++readerIndex;
				--remaining;

				return readb;
			}

			@Override
			public int read(byte b[], int off, int len) throws IOException {
				if (remaining == 0) {
					return -1;
				}

				if (remaining >= len) {
					System.arraycopy(bpacked, readerIndex, b, off, len);

					readerIndex += len;
					remaining -= len;

					return len;
				}

				try {
					System.arraycopy(bpacked, readerIndex, b, off, remaining);
				} catch (Throwable e) {
					System.err.println("error: " + "bpacked.length = "
							+ bpacked.length + ", readerIndex = ");
				}

				remaining = 0;
				readerIndex = bpacked.length;

				return len;
			}

			public void close() throws IOException {
				readerIndex = 0;
				remaining = bpacked.length;
			}
		};
	}

	private long measure(int loops) throws Exception {
		Iso8583Message msg = messageFactory.createMessage();
		msg.clear();

		long startDate = System.currentTimeMillis();
		for (int i = 0; i < loops; ++i) {
			msg.unpack(ipacked);
			ipacked.close();
		}
		long endDate = System.currentTimeMillis();

		return endDate - startDate;
	}

	@Test
	public void shouldVeryFast() throws Exception {
		int loops = 1000000;

		for (int i = 0; i < 1; ++i) {
			long elapsed = measure(loops);

			System.out.println("[unpack] loops/ms = "
					+ (loops * 1000 / elapsed));
			Thread.yield();
		}
	}
}
