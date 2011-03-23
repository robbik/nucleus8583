package org.nucleus8583.core.xml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Iso8583FieldAlignmentsTest {

	@Test
	public void symbolicValueTest() {
		assertEquals('l', Iso8583FieldAlignments.LEFT.symbolicValue());
		assertEquals('r', Iso8583FieldAlignments.RIGHT.symbolicValue());
		assertEquals('n', Iso8583FieldAlignments.NONE.symbolicValue());
	}
}
