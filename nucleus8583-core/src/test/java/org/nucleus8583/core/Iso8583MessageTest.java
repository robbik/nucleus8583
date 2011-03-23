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

public class Iso8583MessageTest {
	private Iso8583Message msg;

	private Iso8583Message msg2;

	private Iso8583Message msg3;

	private Iso8583Message msg4;

	@Before
	public void before() {
		Iso8583MessageFactory fact = Iso8583MessageFactory
				.create("classpath:META-INF/codec8583.xml");

		msg = fact.createMessage();
		msg2 = fact.createMessage();

		fact = Iso8583MessageFactory
				.create("classpath:META-INF/codec8583-2.xml");

		msg3 = fact.createMessage();

		fact = Iso8583MessageFactory
				.create("classpath:META-INF/codec8583-3.xml");

		msg4 = fact.createMessage();
	}

	@Test
	public void testManipulateMti() {
		msg.setMti(null);
		assertEquals("", msg.getMti());

		msg.setMti("0200");
		assertEquals("0200", msg.getMti());

		msg.unsetMti();
		assertEquals("", msg.getMti());

		msg.set(0, "0200");
		assertEquals("0200", msg.getMti());
		assertEquals("0200", msg.get(0));
		assertEquals("0200", msg.getString(0));
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
			assertTrue(error);

			error = false;
			try {
				msg.set(129, new BitSet());
			} catch (IllegalArgumentException ex) {
				error = true;
			}
			assertTrue(error);
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
			msg.set(2, new BitSet());
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		assertNull(msg.get(2));

		error = false;
		try {
			msg.set(64, "abcd");
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		assertNull(msg.get(64));
	}

	@Test
	public void testManipulateStringField() {
		boolean error;

		msg.set(2, "9000");

		error = false;
		try {
			msg.getBinary(2);
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		assertEquals("9000", msg.getString(2));
		assertTrue(msg.get(2) instanceof String);

		msg.unset(2);
		error = false;
		try {
			assertNull(msg.getBinary(2));
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);
		assertNull(msg.getString(2));

		msg.set(2, "0200");
		msg.set(2, (String) null);
		error = false;
		try {
			msg.getBinary(2);
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);
		assertNull(msg.getString(2));

		msg.unsafeSet(2, "9000");
		assertNull(msg.unsafeGetBinary(2));
		assertEquals("9000", msg.unsafeGetString(2));
		assertTrue(msg.get(2) instanceof String);

		msg.unsafeUnset(2);
		error = false;
		try {
			msg.getBinary(2);
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);
		assertNull(msg.getString(2));
	}

	@Test
	public void testManipulateBinaryField() {
		boolean error;

		BitSet ori = new BitSet();
		ori.set(1, true);

		msg.set(64, ori);

		error = false;
		try {
			assertNull(msg.getString(64));
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);
		assertEquals(ori, msg.getBinary(64));
		assertTrue(msg.get(64) instanceof BitSet);

		msg.clear();
		assertNull(msg.getBinary(64));
		error = false;
		try {
			msg.getString(64);
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		msg.set(64, ori);
		msg.set(64, (BitSet) null);
		assertNull(msg.getBinary(64));
		error = false;
		try {
			msg.getString(64);
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		msg.unsafeSet(64, ori);
		error = false;
		try {
			msg.getString(64);
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);
		assertEquals(ori, msg.getBinary(64));
		assertTrue(msg.get(64) instanceof BitSet);
	}

	@Test
	public void equalityTest() {
		assertEquals(msg, msg);
		assertEquals(msg, msg2);
		assertFalse(msg.equals(msg3));
		assertFalse(msg2.equals(msg3));
		assertFalse(msg2.equals(msg4));
		assertFalse(msg3.equals(msg));
		assertFalse(msg3.equals(msg2));
		assertFalse(msg3.equals(msg4));
		assertFalse(msg4.equals(msg));
		assertFalse(msg4.equals(msg2));
		assertFalse(msg4.equals(msg3));

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
		msg.set(2, "400");
		msg2.setMti("0200");
		msg2.set(2, "401");
		assertFalse(msg.equals(msg2));
		assertFalse(msg2.equals(msg));

		msg.clear();
		msg2.clear();

		msg.setMti("0200");
		msg.set(2, "400");
		msg2.setMti("0200");
		assertFalse(msg.equals(msg2));
		assertFalse(msg2.equals(msg));

		msg.clear();
		msg2.clear();

		msg.setMti("0200");
		msg.set(64, new BitSet());
		msg2.setMti("0200");
		assertFalse(msg.equals(msg2));
		assertFalse(msg2.equals(msg));

		msg.clear();
		msg2.clear();

		BitSet bits = new BitSet();
		bits.set(0, true);
		msg.setMti("0200");
		msg.set(64, bits);

		BitSet bits2 = new BitSet();
		bits2.set(0, true);
		msg2.setMti("0200");
		msg2.set(64, bits2);
		assertEquals(msg, msg2);
		assertEquals(msg2, msg);

		msg.setMti("0200");
		msg.set(2, "400");
		msg2.setMti("0200");
		msg2.set(2, "400");
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
		msg.set(2, "400");
		msg.dump(dump);

		expected.put(Integer.valueOf(0), "0200");
		expected.put(Integer.valueOf(2), "400");
		assertEquals(expected, dump);

		expected.clear();
		dump.clear();
		msg.clear();

		BitSet bits = new BitSet();
		bits.set(0, true);
		msg.setMti("0200");
		msg.set(64, bits);
		msg.dump(dump);

		expected.put(Integer.valueOf(0), "0200");
		expected.put(Integer.valueOf(64), bits);
		assertEquals(expected, dump);

		expected.clear();
		dump.clear();
		msg.clear();
	}
}
