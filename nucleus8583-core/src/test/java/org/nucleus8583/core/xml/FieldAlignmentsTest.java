package org.nucleus8583.core.xml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FieldAlignmentsTest {

	@Test
	public void symbolicValueTest() {
		assertEquals('l', FieldAlignments.LEFT.symbolicValue());
		assertEquals('r', FieldAlignments.RIGHT.symbolicValue());
		assertEquals('n', FieldAlignments.NONE.symbolicValue());
	}

	@Test
	public void enumValueOfTest() {
		assertEquals(FieldAlignments.LEFT, FieldAlignments.enumValueOf("left"));
		assertEquals(FieldAlignments.LEFT, FieldAlignments.enumValueOf("lefT"));

		assertEquals(FieldAlignments.RIGHT, FieldAlignments.enumValueOf("right"));
		assertEquals(FieldAlignments.RIGHT, FieldAlignments.enumValueOf("rIgHt"));

		assertEquals(FieldAlignments.NONE, FieldAlignments.enumValueOf("none"));
		assertEquals(FieldAlignments.NONE, FieldAlignments.enumValueOf("nOnE"));

		assertEquals(null, FieldAlignments.enumValueOf("l3ft"));
		assertEquals(null, FieldAlignments.enumValueOf(""));
		assertEquals(null, FieldAlignments.enumValueOf(null));
		assertEquals(null, FieldAlignments.enumValueOf("zzz"));
	}
}
