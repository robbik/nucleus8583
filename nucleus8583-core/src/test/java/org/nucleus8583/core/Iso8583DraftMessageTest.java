package org.nucleus8583.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class Iso8583DraftMessageTest {
	private Iso8583Message draftMsg;

	private Iso8583Message msg;

	@Before
	public void before() {
		draftMsg = new Iso8583Message(128);

		Iso8583MessageFactory fact = Iso8583MessageFactory
				.create("classpath:META-INF/codec8583.xml");

		msg = fact.createMessage();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInstantiateCountOutOfRange1() {
		new Iso8583Message(63);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInstantiateCountOutOfRange2() {
		new Iso8583Message(193);
	}

	@Test
	public void testManipulateMti() {
		draftMsg.setMti(null);
		assertEquals("", draftMsg.getMti());

		draftMsg.setMti("0200");
		assertEquals("0200", draftMsg.getMti());

		draftMsg.unsetMti();
		assertEquals("", draftMsg.getMti());

		draftMsg.set(0, "0200");
		assertEquals("0200", draftMsg.getMti());
		assertEquals("0200", draftMsg.get(0));
		assertEquals("0200", draftMsg.getString(0));
	}

	@Test
	public void testOutOfRange() {
		boolean error;

		error = false;
		try {
			draftMsg.set(0, new BitSet());
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			draftMsg.getBinary(0);
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			draftMsg.set(1, "ab");
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			draftMsg.set(1, new BitSet());
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			draftMsg.set(65, "ab");
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			draftMsg.set(65, new BitSet());
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		for (int i = 129; i < 192; ++i) {
			error = false;
			try {
				draftMsg.set(129, "ab");
			} catch (IllegalArgumentException ex) {
				error = true;
			}
			assertTrue(error);

			error = false;
			try {
				draftMsg.set(129, new BitSet());
			} catch (IllegalArgumentException ex) {
				error = true;
			}
			assertTrue(error);
		}

		error = false;
		try {
			draftMsg.set(198, "ab");
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			draftMsg.set(198, new BitSet());
		} catch (IllegalArgumentException ex) {
			error = true;
		}
		assertTrue(error);
	}

	@Test
	public void testTypeNotMatch() {
		draftMsg.set(2, new BitSet());
		assertNotNull(draftMsg.get(2));

		draftMsg.set(64, "abcd");
		assertEquals(draftMsg.get(64), "abcd");
	}

	@Test
	public void testManipulateStringField() {
		draftMsg.set(2, "9000");

		assertNull(draftMsg.getBinary(2));
		assertEquals("9000", draftMsg.getString(2));
		assertTrue(draftMsg.get(2) instanceof String);

		draftMsg.unset(2);
		assertNull(draftMsg.getBinary(2));
		assertNull(draftMsg.getString(2));

		draftMsg.set(2, "0200");
		draftMsg.set(2, (String) null);

		assertNull(draftMsg.getBinary(2));
		assertNull(draftMsg.getString(2));

		draftMsg.unsafeSet(2, "9000");
		assertNull(draftMsg.unsafeGetBinary(2));
		assertEquals("9000", draftMsg.unsafeGetString(2));
		assertTrue(draftMsg.get(2) instanceof String);

		draftMsg.unsafeUnset(2);
		assertNull(draftMsg.getBinary(2));
		assertNull(draftMsg.getString(2));
	}

	@Test
	public void testManipulateBinaryField() {
		BitSet ori = new BitSet();
		ori.set(1, true);

		draftMsg.set(64, ori);

		assertNull(draftMsg.getString(64));
		assertEquals(ori, draftMsg.getBinary(64));
		assertTrue(draftMsg.get(64) instanceof BitSet);

		draftMsg.clear();
		assertNull(draftMsg.getBinary(64));
		assertNull(draftMsg.getString(64));

		draftMsg.set(64, ori);
		draftMsg.set(64, (BitSet) null);

		assertNull(draftMsg.getBinary(64));
		assertNull(draftMsg.getString(64));

		draftMsg.unsafeSet(64, ori);
		assertNull(draftMsg.getString(64));
		assertEquals(ori, draftMsg.getBinary(64));
		assertTrue(draftMsg.get(64) instanceof BitSet);
	}

	@Test
	public void equalityTest() {
		assertEquals(draftMsg, draftMsg);
		assertEquals(draftMsg, msg);

		assertFalse(draftMsg.equals(null));
		assertFalse(draftMsg.equals("abcde"));

		draftMsg.setMti("0100");
		assertFalse(draftMsg.equals(msg));
		assertFalse(msg.equals(draftMsg));

		draftMsg.setMti("0100");
		msg.setMti("0110");
		assertFalse(draftMsg.equals(msg));
		assertFalse(msg.equals(draftMsg));

		draftMsg.setMti("0200");
		draftMsg.set(2, "400");
		msg.setMti("0200");
		msg.set(2, "401");
		assertFalse(draftMsg.equals(msg));
		assertFalse(msg.equals(draftMsg));

		draftMsg.clear();
		msg.clear();

		draftMsg.setMti("0200");
		draftMsg.set(2, "400");
		msg.setMti("0200");
		assertFalse(draftMsg.equals(msg));
		assertFalse(msg.equals(draftMsg));

		draftMsg.clear();
		msg.clear();

		draftMsg.setMti("0200");
		draftMsg.set(64, new BitSet());
		msg.setMti("0200");
		assertFalse(draftMsg.equals(msg));
		assertFalse(msg.equals(draftMsg));

		draftMsg.clear();
		msg.clear();

		BitSet bits = new BitSet();
		bits.set(0, true);
		draftMsg.setMti("0200");
		draftMsg.set(64, bits);

		BitSet bits2 = new BitSet();
		bits2.set(0, true);
		msg.setMti("0200");
		msg.set(64, bits2);
		assertEquals(draftMsg, msg);
		assertEquals(msg, draftMsg);

		draftMsg.setMti("0200");
		draftMsg.set(2, "400");
		msg.setMti("0200");
		msg.set(2, "400");
		assertEquals(draftMsg, msg);
		assertEquals(msg, draftMsg);

		assertEquals(draftMsg.hashCode(), msg.hashCode());
		assertEquals(draftMsg.toString(), msg.toString());
	}

	@Test
	public void dumpTest() {
		Map<Integer, Object> dump = new HashMap<Integer, Object>();
		Map<Integer, Object> expected = new HashMap<Integer, Object>();

		draftMsg.setMti("0100");
		draftMsg.dump(dump);

		expected.put(Integer.valueOf(0), "0100");
		assertEquals(expected, dump);

		expected.clear();
		dump.clear();
		draftMsg.clear();

		draftMsg.setMti("0200");
		draftMsg.set(2, "400");
		draftMsg.dump(dump);

		expected.put(Integer.valueOf(0), "0200");
		expected.put(Integer.valueOf(2), "400");
		assertEquals(expected, dump);

		expected.clear();
		dump.clear();
		draftMsg.clear();

		BitSet bits = new BitSet();
		bits.set(0, true);
		draftMsg.setMti("0200");
		draftMsg.set(64, bits);
		draftMsg.dump(dump);

		expected.put(Integer.valueOf(0), "0200");
		expected.put(Integer.valueOf(64), bits);
		assertEquals(expected, dump);

		expected.clear();
		dump.clear();
		draftMsg.clear();
	}
}
