package org.nucleus8583.core;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class ReadPerformanceTest {
	private MessageSerializer serializer;

	private InputStream ipacked;

	@Before
	public void initialize() throws Exception {
		serializer = new MessageSerializer("classpath:META-INF/codec8583.xml");

		final byte[] bpacked = "0200423800080A010000000000000000000004312501041324311     1324310104C010000001762745214  0003701000abcdefghijkl                    ".getBytes();

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
		Message msg = new Message();

		long startDate = System.currentTimeMillis();
		for (int i = 0; i < loops; ++i) {
			serializer.read(ipacked, msg);
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

			System.out.println("[read] loops/ms = " + (loops * 1000 / elapsed));
			Thread.yield();
		}
	}
}
