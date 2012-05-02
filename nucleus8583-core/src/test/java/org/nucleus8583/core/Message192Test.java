package org.nucleus8583.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.util.BitmapHelper;

public class Message192Test {
	private Message msg1;

	private Message msg2;

	@Before
	public void before() {
		msg1 = new Message(192);

		msg2 = new Message(192);
	}

	@Test
	public void testManipulateMti() {
		msg1.setMti("0200");
		assertEquals("0200", msg1.getMti());
	}

	@Test
	public void testManipulateOutOfRangeFields() {
		boolean error;

		error = false;
		try {
			msg1.set(1, "ab");
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			msg1.set(1, new byte[0]);
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			msg1.set(65, "ab");
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			msg1.set(65, new byte[0]);
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		for (int i = 129; i < 192; ++i) {
			error = false;
			try {
				msg1.set(129, "ab");
			} catch (IllegalArgumentException ex) {
				error = true;
			}

			try {
				msg1.set(129, new byte[0]);
				error &= false;
			} catch (IllegalArgumentException ex) {
				error &= true;
			}
			assertFalse(error);
		}

		error = false;
		try {
			msg1.set(198, "ab");
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			msg1.set(198, new byte[0]);
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);
	}

	@Test
	public void testManipulateStringField() {
		msg1.set(162, "9000");
		assertTrue(msg1.get(162) instanceof String);

		assertEquals("9000", msg1.get(162));

		msg1.unset(162);
		assertNull(msg1.get(162));

		msg1.set(162, "0200");
		msg1.set(162, (String) null);

		assertNull(msg1.get(162));

		msg1.unsafeSet(162, "9000");
		assertTrue(msg1.get(162) instanceof String);

		assertEquals("9000", msg1.unsafeGet(162));

		msg1.unsafeUnset(162);
		assertNull(msg1.get(162));
	}

	@Test
	public void testManipulateBinaryField() {
	    byte[] ori = BitmapHelper.create(8);
	    BitmapHelper.set(ori, 1);

		msg1.set(190, ori);
		assertTrue(msg1.get(190) instanceof byte[]);

		assertEquals(ori, msg1.get(190));

		msg1.clear();
		assertNull(msg1.get(190));

		msg1.set(190, ori);
		msg1.set(190, (byte[]) null);

		assertNull(msg1.get(190));

		msg1.unsafeSet(190, ori);
		assertTrue(msg1.get(190) instanceof byte[]);

		assertEquals(ori, msg1.get(190));
	}

	@Test
	public void equalityTest() {
		assertEquals(msg1, msg1);
		assertEquals(msg2, msg2);

		assertEquals(msg1, msg2);
		assertEquals(msg2, msg1);

		assertFalse(msg1.equals(null));
		assertFalse(msg1.equals("abcde"));

		msg1.setMti("0100");
		assertFalse(msg1.equals(msg2));
		assertFalse(msg2.equals(msg1));

		msg1.setMti("0100");
		msg2.setMti("0110");
		assertFalse(msg1.equals(msg2));
		assertFalse(msg2.equals(msg1));

		msg1.setMti("0200");
		msg1.set(163, "400");
		msg2.setMti("0200");
		msg2.set(163, "401");
		assertFalse(msg1.equals(msg2));
		assertFalse(msg2.equals(msg1));

		msg1.clear();
		msg2.clear();

        byte[] bits = BitmapHelper.create(8);
        BitmapHelper.set(bits, 0);

		msg1.setMti("0200");
		msg1.set(190, bits);

        bits = BitmapHelper.create(8);
        BitmapHelper.set(bits, 0);

		msg2.setMti("0200");
		msg2.set(190, bits);

		assertEquals(msg1, msg2);
		assertEquals(msg2, msg1);

		msg1.setMti("0200");
		msg1.set(163, "400");
		msg2.setMti("0200");
		msg2.set(163, "400");
		assertEquals(msg1, msg2);
		assertEquals(msg2, msg1);

		assertEquals(msg1.hashCode(), msg2.hashCode());
		assertEquals(msg1.toString(), msg2.toString());
	}

	@Test
	public void dumpTest() {
		Map<Integer, Object> dump = new HashMap<Integer, Object>();
		Map<Integer, Object> expected = new HashMap<Integer, Object>();

		msg1.setMti("0100");
		msg1.dump(dump);

		expected.put(Integer.valueOf(0), "0100");
		assertEquals(expected, dump);

		expected.clear();
		dump.clear();
		msg1.clear();

		msg1.setMti("0200");
		msg1.set(163, "400");
		msg1.dump(dump);

		expected.put(Integer.valueOf(0), "0200");
		expected.put(Integer.valueOf(163), "400");
		assertEquals(expected, dump);

		expected.clear();
		dump.clear();
		msg1.clear();

		byte[] bits = BitmapHelper.create(8);
		BitmapHelper.set(bits, 0);

		msg1.setMti("0200");
		msg1.set(190, bits);
		msg1.dump(dump);

		expected.put(Integer.valueOf(0), "0200");
		expected.put(Integer.valueOf(190), bits);
		assertEquals(expected, dump);

		expected.clear();
		dump.clear();
		msg1.clear();
	}
}
