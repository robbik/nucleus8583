package org.nucleus8583.core.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FastStringReaderTest {

	@Test
	void markSupportedTest() {
		try (FastStringReader reader = new FastStringReader("ance")){
			assertThat(reader.markSupported(), is(false));
		}
	}

	@Test
	void markTest() {
		try (FastStringReader reader = new FastStringReader("ance")) {
			assertThrows(IOException.class, () -> reader.mark(0));
		}
	}

	@Test
	void testNull() throws Exception {
		try (FastStringReader reader = new FastStringReader(null)) {
			assertThat(reader.ready(), is(false));
			assertThat(reader.read(), is(-1));
			assertThat(reader.skip(1), is(0L));
		}
	}

	@Test
	void testEmptyString() throws Exception {
		try (FastStringReader reader = new FastStringReader("")) {
			assertThat(reader.ready(), is(false));
			assertThat(reader.read(), is(-1));
			assertThat(reader.skip(1), is(0L));
		}
	}

	@Test
	void testReset() throws Exception {
		try (FastStringReader reader = new FastStringReader("abcd")) {
			assertThat(reader.ready(), is(true));
			assertThat(reader.read(), is((int) 'a'));

			reader.reset();
			assertThat(reader.read(), is((int) 'a'));
		}
	}

	@Test
	void testClose() throws Exception {
		try (FastStringReader reader = new FastStringReader("abcd")) {
			assertThat(reader.ready(), is(true));

			reader.close();
			assertThat(reader.ready(), is(false));
		}
	}

	@Test
	void testSkip() throws Exception {
		try (FastStringReader reader = new FastStringReader("abcd")) {
			reader.skip(1);
			assertThat(reader.read(), is((int) 'b'));

			reader.skip(7);
			assertThat(reader.read(), is(-1));

			reader.reset();
			reader.skip(2);

			char[] cbuf = new char[20];

			int readb = reader.read(cbuf);
			assertThat(readb, is(2));

			assertThat(new String(cbuf, 0, readb), is("cd"));
		}
	}

	@Test
	void testRead() throws Exception {
		try (FastStringReader reader = new FastStringReader("abcd")) {
			char[] cbuf = new char[20];

			int readb = reader.read(cbuf);
			assertThat(readb, is(4));
			assertThat(new String(cbuf, 0, readb), is("abcd"));

			reader.reset();

			readb = reader.read(cbuf, 0, 0);
			assertThat(readb, is(0));
		}
	}
}
