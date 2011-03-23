package org.nucleus8583.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class Iso8583Message192Test {
	private Iso8583Message msg;

	private Iso8583Message msg2;

	@Before
	public void before() {
		Iso8583MessageFactory fact = Iso8583MessageFactory
				.create("classpath:META-INF/codec8583L.xml");

		msg = fact.createMessage();
		msg2 = fact.createMessage();
	}

	@Test
	public void testManipulateMti() {
		msg.setMti("0200");
		assertEquals("0200", msg.getMti());

		msg.unsetMti();
		assertEquals("", msg.getMti());
	}

	@Test
	public void testOutOfRange() {
		boolean error;

		error = false;
		try {
			msg.set(0, new BitSet());
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			msg.getBinary(0);
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			msg.set(1, "ab");
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			msg.set(1, new BitSet());
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			msg.set(65, "ab");
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			msg.set(65, new BitSet());
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		for (int i = 129; i < 192; ++i) {
			error = false;
			try {
				msg.set(129, "ab");
			} catch (IllegalArgumentException ex) {
				error = true;
			}

			try {
				msg.set(129, new BitSet());
				error &= false; 
			} catch (IllegalArgumentException ex) {
				error &= true;
			}
			assertFalse(error);
		}

		error = false;
		try {
			msg.set(198, "ab");
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			msg.set(198, new BitSet());
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);
	}

	@Test
	public void testTypeNotMatch() {
		boolean error;

		error = false;
		try {
			msg.set(162, new BitSet());
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		assertNull(msg.get(162));

		error = false;
		try {
			msg.set(190, "abcd");
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		assertNull(msg.get(190));
	}

	@Test
	public void testManipulateStringField() {
		boolean error;

		msg.set(162, "9000");

		error = false;
		try {
			msg.getBinary(162);
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		assertEquals("9000", msg.getString(162));
		assertTrue(msg.get(162) instanceof String);

		msg.unset(162);
		error = false;
		try {
			assertNull(msg.getBinary(162));
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);
		assertNull(msg.getString(162));

		msg.set(162, "0200");
		msg.set(162, (String) null);
		error = false;
		try {
			msg.getBinary(162);
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);
		assertNull(msg.getString(2));

		msg.unsafeSet(162, "9000");
		assertNull(msg.unsafeGetBinary(162));
		assertEquals("9000", msg.unsafeGetString(162));
		assertTrue(msg.get(162) instanceof String);

		msg.unsafeUnset(162);
		error = false;
		try {
			msg.getBinary(162);
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);
		assertNull(msg.getString(162));
	}

	@Test
	public void testManipulateBinaryField() {
		boolean error;

		BitSet ori = new BitSet();
		ori.set(1, true);

		msg.set(190, ori);

		error = false;
		try {
			assertNull(msg.getString(190));
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);
		assertEquals(ori, msg.getBinary(190));
		assertTrue(msg.get(190) instanceof BitSet);

		msg.clear();
		assertNull(msg.getBinary(190));
		error = false;
		try {
			msg.getString(190);
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		msg.set(190, ori);
		msg.set(190, (BitSet) null);
		assertNull(msg.getBinary(190));
		error = false;
		try {
			msg.getString(190);
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		msg.unsafeSet(190, ori);
		error = false;
		try {
			msg.getString(190);
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);
		assertEquals(ori, msg.getBinary(190));
		assertTrue(msg.get(190) instanceof BitSet);
	}

	@Test
	public void equalityTest() {
		assertEquals(msg, msg);
		assertEquals(msg2, msg2);

		assertEquals(msg, msg2);
		assertEquals(msg2, msg);

		assertFalse(msg.equals(null));
		assertFalse(msg.equals("abcde"));

		msg.setMti("0100");
		assertFalse(msg.equals(msg2));
		assertFalse(msg2.equals(msg));

		msg.setMti("0100");
		msg2.setMti("0110");
		assertFalse(msg.equals(msg2));
		assertFalse(msg2.equals(msg));

		msg.setMti("0200");
		msg.set(163, "400");
		msg2.setMti("0200");
		msg2.set(163, "401");
		assertFalse(msg.equals(msg2));
		assertFalse(msg2.equals(msg));

		msg.clear();
		msg2.clear();

		BitSet bits = new BitSet();
		bits.set(0, true);
		msg.setMti("0200");
		msg.set(190, bits);

		BitSet bits2 = new BitSet();
		bits2.set(0, true);
		msg2.setMti("0200");
		msg2.set(190, bits2);
		assertEquals(msg, msg2);
		assertEquals(msg2, msg);

		msg.setMti("0200");
		msg.set(163, "400");
		msg2.setMti("0200");
		msg2.set(163, "400");
		assertEquals(msg, msg2);
		assertEquals(msg2, msg);

		assertEquals(msg.hashCode(), msg2.hashCode());
		assertEquals(msg.toString(), msg2.toString());
	}

	@Test
	public void dumpTest() {
		Map<Integer, Object> dump = new HashMap<Integer, Object>();
		Map<Integer, Object> expected = new HashMap<Integer, Object>();

		msg.setMti("0100");
		msg.dump(dump);

		expected.put(Integer.valueOf(0), "0100");
		assertEquals(expected, dump);

		expected.clear();
		dump.clear();
		msg.clear();

		msg.setMti("0200");
		msg.set(163, "400");
		msg.dump(dump);

		expected.put(Integer.valueOf(0), "0200");
		expected.put(Integer.valueOf(163), "400");
		assertEquals(expected, dump);

		expected.clear();
		dump.clear();
		msg.clear();

		BitSet bits = new BitSet();
		bits.set(0, true);
		msg.setMti("0200");
		msg.set(190, bits);
		msg.dump(dump);

		expected.put(Integer.valueOf(0), "0200");
		expected.put(Integer.valueOf(190), bits);
		assertEquals(expected, dump);

		expected.clear();
		dump.clear();
		msg.clear();
	}
}
