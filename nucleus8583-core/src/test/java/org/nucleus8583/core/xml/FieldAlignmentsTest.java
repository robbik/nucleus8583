package org.nucleus8583.core.xml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FieldAlignmentsTest {

	@Test
	public void symbolicValueTest() {
		assertEquals('l', FieldAlignments.TRIMMED_LEFT.symbolicValue());
		assertEquals('r', FieldAlignments.TRIMMED_RIGHT.symbolicValue());
		assertEquals('n', FieldAlignments.NONE.symbolicValue());
	}

	@Test
	public void enumValueOfTest() {
		assertEquals(FieldAlignments.TRIMMED_LEFT, FieldAlignments.enumValueOf("left"));
		assertEquals(FieldAlignments.TRIMMED_LEFT, FieldAlignments.enumValueOf("lefT"));

		assertEquals(FieldAlignments.TRIMMED_RIGHT, FieldAlignments.enumValueOf("right"));
		assertEquals(FieldAlignments.TRIMMED_RIGHT, FieldAlignments.enumValueOf("rIgHt"));

		assertEquals(FieldAlignments.NONE, FieldAlignments.enumValueOf("none"));
		assertEquals(FieldAlignments.NONE, FieldAlignments.enumValueOf("nOnE"));

		assertEquals(null, FieldAlignments.enumValueOf("l3ft"));
		assertEquals(null, FieldAlignments.enumValueOf(""));
		assertEquals(null, FieldAlignments.enumValueOf(null));
		assertEquals(null, FieldAlignments.enumValueOf("zzz"));
	}
}
