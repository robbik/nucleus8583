package org.nucleus8583.externaltest.jpos;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.packager.ISO87APackager;
import org.nucleus8583.core.Message;
import org.nucleus8583.core.MessageSerializer;

public class ReadPerformanceTest {
	private static int LOOPS = 1000;

	private MessageSerializer serializer;

	private ISOPackager packager;

	private InputStream ipacked;

	public void initialize() throws Exception {
		serializer = new MessageSerializer("classpath:META-INF/nucleus8583/packagers/iso87ascii.xml");

		packager = new ISO87APackager();

		final byte[] bpacked = "0200423800080A010000000000000000000004312501041324311     1324310104C010000001762745214  0003701000abcdefghijkl                    "
				.getBytes();

		ipacked = new InputStream() {
			private int readerIndex = 0;

			private int remaining = bpacked.length;

			private final int oriRemaining = bpacked.length;

			@Override
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

			@Override
			public void close() throws IOException {
				readerIndex = 0;
				remaining = oriRemaining;
			}
		};
	}

	private long doJobForNucleus8583() throws Exception {
		long startDate = System.currentTimeMillis();

		for (int i = LOOPS - 1; i >= 0; --i) {
			Message msg = new Message();
			serializer.read(ipacked, msg);

			ipacked.close();
		}

		long endDate = System.currentTimeMillis();

		return endDate - startDate;
	}

	private long doJobForJPos() throws Exception {
		long startDate = System.currentTimeMillis();

		for (int i = LOOPS - 1; i >= 0; --i) {
			ISOMsg msg = new ISOMsg();
			msg.setPackager(packager);

			msg.unpack(ipacked);

			ipacked.close();
		}

		long endDate = System.currentTimeMillis();

		return endDate - startDate;
	}

	public static void main(String[] args) throws Exception {
		boolean nucleus8583 = false;
		int recount = 10;

		if (args.length > 0) {
			LOOPS = Integer.parseInt(args[0]);

			if (args.length > 1) {
				nucleus8583 = args[1].equals("nuc");

				if (args.length > 2) {
					recount = Integer.parseInt(args[2]);
				}
			}
		}

		DecimalFormat fmt = new DecimalFormat("#,##0.00");

		long jposElapsed = 0;
		long nucleus8583Elapsed = 0;

		ReadPerformanceTest tester = new ReadPerformanceTest();
		tester.initialize();

		System.out.println();

		for (int i = 0; i < recount; ++i) {
			if (nucleus8583) {
				nucleus8583Elapsed = tester.doJobForNucleus8583();
			} else {
				jposElapsed = tester.doJobForJPos();
			}

			if (nucleus8583Elapsed > 0) {
				if (i == 0) {
					System.out.println("NUCLEUS8583 3.0.0 DETAILS");
					System.out.println("===========================");
					System.out.println("[x] Number of Data Samples  = "
							+ fmt.format(LOOPS));
					System.out.println();

					System.out.println(String.format("%1$10s %2$20s",
							"Times (sec)", "Throughput (tps)"));
				}

				System.out.println(String.format("%1$10s %2$20s",
						fmt.format(nucleus8583Elapsed / 1000.0f),
						fmt.format(LOOPS * 1000.0f / nucleus8583Elapsed)));
			}

			if (jposElapsed > 0) {
				if (i == 0) {
					System.out.println("JPOS 1.8.2 DETAILS");
					System.out.println("===========================");
					System.out.println("[x] Number of Data Samples  = "
							+ fmt.format(LOOPS));
					System.out.println();

					System.out.println(String.format("%1$10s %2$20s",
							"Times (sec)", "Throughput (tps)"));
				}

				System.out.println(String.format("%1$10s %2$20s",
						fmt.format(jposElapsed / 1000.0f),
						fmt.format(LOOPS * 1000.0f / jposElapsed)));
			}

			Thread.yield();
		}

		System.out.println();
	}
}
