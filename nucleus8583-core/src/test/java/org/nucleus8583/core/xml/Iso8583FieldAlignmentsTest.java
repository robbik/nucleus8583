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

	@Test
	public void enumValueOfTest() {
		assertEquals(Iso8583FieldAlignments.LEFT, Iso8583FieldAlignments.enumValueOf("left"));
		assertEquals(Iso8583FieldAlignments.LEFT, Iso8583FieldAlignments.enumValueOf("lefT"));

		assertEquals(Iso8583FieldAlignments.RIGHT, Iso8583FieldAlignments.enumValueOf("right"));
		assertEquals(Iso8583FieldAlignments.RIGHT, Iso8583FieldAlignments.enumValueOf("rIgHt"));

		assertEquals(Iso8583FieldAlignments.NONE, Iso8583FieldAlignments.enumValueOf("none"));
		assertEquals(Iso8583FieldAlignments.NONE, Iso8583FieldAlignments.enumValueOf("nOnE"));

		assertEquals(null, Iso8583FieldAlignments.enumValueOf("l3ft"));
		assertEquals(null, Iso8583FieldAlignments.enumValueOf(""));
		assertEquals(null, Iso8583FieldAlignments.enumValueOf(null));
		assertEquals(null, Iso8583FieldAlignments.enumValueOf("zzz"));
	}
}
