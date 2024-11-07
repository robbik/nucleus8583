package org.nucleus8583.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nucleus8583.core.util.BitmapHelper;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageTest {

	private Message msg1;

	private Message msg2;

	@BeforeEach
	void beforeEach() {
		msg1 = new Message(128);
		msg2 = new Message(128);
	}

	@Test
	void testInstantiateCountOutOfRangeCase1() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Message(63);
        });
	}

	@Test
	void testInstantiateCountOutOfRangeCase2() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Message(193);
        });
	}

	@Test
    void testSetMtiIfNull() {
        msg1.mti(null);
        assertThat(msg1.mti(), is(""));
    }

    @Test
    public void testSetMtiIf1Character() {
        msg1.mti("1");
        assertThat(msg1.mti(), is("1"));
    }

    @Test
    public void testSetMtiIf2Characters() {
        msg1.mti("11");
        assertThat(msg1.mti(), is("11"));
    }

    @Test
    public void testSetMtiIf3Characters() {
        msg1.mti("111");
        assertThat(msg1.mti(), is("111"));
    }

    @Test
    public void testSetMtiIf5Characters() {
        msg1.mti("12345");
        assertThat(msg1.mti(), is("12345"));
    }

	@Test
	public void testSetMtiIfValid() {
		msg1.mti("0200");
		assertThat(msg1.mti(), is("0200"));

		msg1.set(0, "0300");
		assertThat(msg1.mti(), is("0300"));
		assertThat(msg1.get(0), is("0300"));
	}

	@Test
	void testManipulateOutOfRangeFields() {
		boolean error;

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
            assertThrows(IllegalArgumentException.class, () -> {
                msg1.set(129, "ab");
            });

            assertThrows(IllegalArgumentException.class, () -> {
                msg1.set(129, new byte[0]);
            });
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
		msg1.set(2, "9000");
		assertThat(msg1.get(2), isA(String.class));

		msg1.unset(2);
		assertThat(msg1.get(2), nullValue());

		msg1.set(2, "0200");
		msg1.set(2, (String) null);

		assertThat(msg1.get(2), nullValue());

		msg1.unsafeSet(2, "9000");
		assertThat(msg1.get(2), isA(String.class));

		assertThat(msg1.unsafeGet(2), is("9000"));

		msg1.unsafeUnset(2);
		assertThat(msg1.get(2), nullValue());
	}

	@Test
	void testManipulateBinaryField() {
		byte[] ori = BitmapHelper.create(8);
		BitmapHelper.set(ori, 1);

		msg1.set(64, ori);
		assertThat(msg1.get(64), isA(byte[].class));

		assertArrayEquals(ori, msg1.get(64));

		msg1.clear();
		assertThat(msg1.get(64), nullValue());

		msg1.set(64, ori);
		msg1.set(64, (byte[]) null);

        assertThat(msg1.get(64), nullValue());

		msg1.unsafeSet(64, ori);
		assertThat(msg1.get(64), isA(byte[].class));

		assertArrayEquals(ori, msg1.get(64));
	}

	@Test
    void equalityTest() {
		assertThat(msg1, equalTo(msg1));
		assertThat(msg2, equalTo(msg1));

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
		msg1.set(2, "400");
		msg2.mti("0200");
		msg2.set(2, "401");
		assertThat(msg1.equals(msg2), is(false));
		assertThat(msg2.equals(msg1), is(false));

		msg1.clear();
		msg2.clear();

		msg1.mti("0200");
		msg1.set(2, "400");
		msg2.mti("0200");
		assertThat(msg1.equals(msg2), is(false));
		assertThat(msg2.equals(msg1), is(false));

		msg1.clear();
		msg2.clear();

		msg1.mti("0200");
		msg1.set(64, new byte[0]);
		msg2.mti("0200");
		assertThat(msg1.equals(msg2), is(false));
		assertThat(msg2.equals(msg1), is(false));

		msg1.clear();
		msg2.clear();

        byte[] bits = BitmapHelper.create(8);
        BitmapHelper.set(bits, 1);

		msg1.mti("0200");
		msg1.set(64, bits);

        bits = BitmapHelper.create(8);
        BitmapHelper.set(bits, 1);

		msg2.mti("0200");
		msg2.set(64, bits);
		assertThat(msg2, equalTo(msg1));
		assertThat(msg1, equalTo(msg2));

		msg1.mti("0200");
		msg1.set(2, "400");
		msg2.mti("0200");
		msg2.set(2, "400");
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
		msg1.set(2, "400");
		msg1.dump(dump);

		expected.put(Integer.valueOf(0), "0200");
		expected.put(Integer.valueOf(2), "400");
		assertThat(dump, equalTo(expected));

		expected.clear();
		dump.clear();
		msg1.clear();

        byte[] bits = BitmapHelper.create(8);
        BitmapHelper.set(bits, 1);

		msg1.mti("0200");
		msg1.set(64, bits);
		msg1.dump(dump);

		expected.put(Integer.valueOf(0), "0200");
		expected.put(Integer.valueOf(64), bits);
		assertThat(dump, equalTo(expected));

		expected.clear();
		dump.clear();
		msg1.clear();
	}

	@Test
	public void testSetResponseMti() {
	    char[] in = new char[] { '0', '2', '0', '0' };

	    Message msg = new Message();

	    for (int i = 0; i < 8; i += 2) {
	        in[2] = (char) (i + '0');

	        msg.mti(new String(in));
	        assertThat(msg.isRequest(), is(true));
            assertThat(msg.isResponse(), is(false));

            msg.setResponseMti();
            in[2] = (char) (i + '1');

            assertThat(msg.mti(), is(new String(in)));

            assertThat(msg.isRequest(), is(false));
            assertThat(msg.isResponse(), is(true));
	    }
	}

    @Test
    void testSetResponseMtiIfMtiIsRequest() {
        char[] in = new char[] { '0', '2', '0', '0' };

        Message msg = new Message();

        for (int i = 0; i < 8; i += 2) {
            in[2] = (char) (i + '1');

            msg.mti(new String(in));
            assertThat(msg.isRequest(), is(false));
            assertThat(msg.isResponse(), is(true));

            msg.setResponseMti();
            assertThat(msg.mti(), is(new String(in)));

            assertThat(msg.isRequest(), is(false));
            assertThat(msg.isResponse(), is(true));
        }
    }
}
