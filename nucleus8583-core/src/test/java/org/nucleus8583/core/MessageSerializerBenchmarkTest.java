package org.nucleus8583.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.format.OutputFormatFactory;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.openjdk.jmh.runner.options.VerboseMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

@EnabledIfSystemProperty(named = "m2.profile", matches = "benchmark-test")
public class MessageSerializerBenchmarkTest {

	@Test
	void runBenchmarkTests() throws Exception {
		final Options opts = new OptionsBuilder() //
				.include(getClass().getName() + ".*") //
				.mode(Mode.Throughput) //
				.verbosity(VerboseMode.NORMAL) //
				.timeUnit(TimeUnit.SECONDS) //
				.warmupIterations(1) //
				.warmupTime(TimeValue.seconds(5))
				.measurementTime(TimeValue.seconds(5))
				.measurementIterations(1) //
				.operationsPerInvocation(1_000)
				.threads(1) //
				.shouldFailOnError(true) //
				.shouldDoGC(true) //
				.forks(1) //
				.build();

		new Runner(opts, OutputFormatFactory.createFormatInstance(System.out, VerboseMode.NORMAL)).run();
	}

	@Benchmark
	public void read(Context context) throws Exception {
		Message message = new Message();

		for (int i = 0; i < 1_000; ++i) {
			context.serializer.read(context.source, message);
			context.source.close();
		}
	}

	@Benchmark
	public void write(Context context) throws Exception {
		for (int i = 0; i < 1_000; ++i) {
			Message msg = new Message();

			msg.mti("0200");
			msg.set(2, "3125");
			msg.set(7, "0104132431");
			msg.set(11, "1");
			msg.set(12, "132431");
			msg.set(13, "0104");
			msg.set(29, "C01000000");
			msg.set(37, "1762745214");
			msg.set(39, "00");
			msg.set(48, "01000abcdefghijkl                    ");

			context.serializer.write(msg, context.sink);
		}
	}

	@State(Scope.Thread)
	public static class Context {

		private MessageSerializer serializer;

		private InputStream source;

		private OutputStream sink;

		@Setup
		public void setup() {
			serializer = MessageSerializer.get("codec8583");

			final byte[] bpacked = "0200423800080A010000000000000000000004312501041324311     1324310104C010000001762745214  0003701000abcdefghijkl                    ".getBytes();

			source = new InputStream() {

				private int readerIndex = 0;

				private int remaining = bpacked.length;

				@Override
				public int read() {
					if (remaining == 0) {
						return -1;
					}

					int readb = bpacked[readerIndex] & 0xFF;

					++readerIndex;
					--remaining;

					return readb;
				}

				@Override
				public int read(byte[] b, int off, int len) {
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
						System.err.println("error: " + "bpacked.length = " + bpacked.length + ", readerIndex = ");
					}

					remaining = 0;
					readerIndex = bpacked.length;

					return len;
				}

				@Override
				public void close() {
					readerIndex = 0;
					remaining = bpacked.length;
				}
			};

			sink = new OutputStream() {
				public void write(int b) throws IOException {
					// do nothing
				}
			};
		}
	}
}
