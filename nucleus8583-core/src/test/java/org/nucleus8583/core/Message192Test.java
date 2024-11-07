package org.nucleus8583.core;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nucleus8583.core.util.BitmapHelper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Message192Test {

	private Message msg1;

	private Message msg2;

	@BeforeEach
	void beforeEach() {
		msg1 = new Message(192);

		msg2 = new Message(192);
	}

	@Test
	public void testManipulateMti() {
		msg1.mti("0200");
		assertThat(msg1.mti(), is("0200"));
	}

	@Test
	void testManipulateOutOfRangeFields() {
        assertThrows(IllegalArgumentException.class, () -> {
            msg1.set(1, "ab");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            msg1.set(1, new byte[0]);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            msg1.set(65, "ab");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            msg1.set(65, new byte[0]);
        });

		for (int i = 129; i < 192; ++i) {
			boolean error = false;

			try {
				msg1.set(i, "ab");
			} catch (IllegalArgumentException ex) {
				error = true;
			}

			try {
				msg1.set(i, new byte[0]);
				error = false;
			} catch (IllegalArgumentException ex) {
				// do nothing
			}

			assertThat(error, is(false));
		}

        assertThrows(IllegalArgumentException.class, () -> {
            msg1.set(198, "ab");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            msg1.set(198, new byte[0]);
        });
	}

	@Test
	void testManipulateStringField() {
		msg1.set(162, "9000");
		assertThat(msg1.get(162), isA(String.class));

		assertThat(msg1.get(162), is("9000"));

		msg1.unset(162);
		assertThat(msg1.get(162), nullValue());

		msg1.set(162, "0200");
		msg1.set(162, (String) null);

        assertThat(msg1.get(162), nullValue());

		msg1.unsafeSet(162, "9000");
		assertThat(msg1.get(162), isA(String.class));

		assertThat(msg1.unsafeGet(162), is("9000"));

		msg1.unsafeUnset(162);
		assertThat(msg1.get(162), nullValue());
	}

	@Test
	void testManipulateBinaryField() {
	    byte[] ori = BitmapHelper.create(8);
	    BitmapHelper.set(ori, 1);

		msg1.set(190, ori);
		assertThat(msg1.get(190), isA(byte[].class));

		assertThat(msg1.get(190), equalTo(ori));

		msg1.clear();
		assertThat(msg1.get(190), nullValue());

		msg1.set(190, ori);
		msg1.set(190, (byte[]) null);

		assertThat(msg1.get(190), nullValue());

		msg1.unsafeSet(190, ori);
		assertThat(msg1.get(190), isA(byte[].class));

		assertThat(msg1.get(190), equalTo(ori));
	}

	@Test
	void equalityTest() {
		assertThat(msg1, equalTo(msg1));
		assertThat(msg2, equalTo(msg2));

		assertThat(msg2, equalTo(msg1));
		assertThat(msg1, equalTo(msg2));

		assertThat(msg1.equals(null), is(false));
		assertThat(msg1.equals("abcde"), is(false));

		msg1.mti("0100");
		assertThat(msg1.equals(msg2), is(false));
		assertThat(msg2.equals(msg1), is(false));

		msg1.mti("0100");
		msg2.mti("0110");
		assertThat(msg1.equals(msg2), is(false));
		assertThat(msg2.equals(msg1), is(false));

		msg1.mti("0200");
		msg1.set(163, "400");
		msg2.mti("0200");
		msg2.set(163, "401");
		assertThat(msg1.equals(msg2), is(false));
		assertThat(msg2.equals(msg1), is(false));

		msg1.clear();
		msg2.clear();

        byte[] bits = BitmapHelper.create(8);
        BitmapHelper.set(bits, 0);

		msg1.mti("0200");
		msg1.set(190, bits);

        bits = BitmapHelper.create(8);
        BitmapHelper.set(bits, 0);

		msg2.mti("0200");
		msg2.set(190, bits);

		assertThat(msg2, equalTo(msg1));
		assertThat(msg1, equalTo(msg2));

		msg1.mti("0200");
		msg1.set(163, "400");
		msg2.mti("0200");
		msg2.set(163, "400");
		assertThat(msg2, equalTo(msg1));
		assertThat(msg1, equalTo(msg2));

		assertThat(msg2.hashCode(), is(msg1.hashCode()));
		assertThat(msg2.toString(), is(msg1.toString()));
	}

	@Test
	void dumpTest() {
		Map<Integer, Object> dump = new HashMap<>();
		Map<Integer, Object> expected = new HashMap<>();

		msg1.mti("0100");
		msg1.dump(dump);

		expected.put(Integer.valueOf(0), "0100");
		assertThat(dump, equalTo(expected));

		expected.clear();
		dump.clear();
		msg1.clear();

		msg1.mti("0200");
		msg1.set(163, "400");
		msg1.dump(dump);

		expected.put(Integer.valueOf(0), "0200");
		expected.put(Integer.valueOf(163), "400");
		assertThat(dump, equalTo(expected));

		expected.clear();
		dump.clear();
		msg1.clear();

		byte[] bits = BitmapHelper.create(8);
		BitmapHelper.set(bits, 0);

		msg1.mti("0200");
		msg1.set(190, bits);
		msg1.dump(dump);

		expected.put(Integer.valueOf(0), "0200");
		expected.put(Integer.valueOf(190), bits);
		assertThat(dump, equalTo(expected));

		expected.clear();
		dump.clear();
		msg1.clear();
	}
}
