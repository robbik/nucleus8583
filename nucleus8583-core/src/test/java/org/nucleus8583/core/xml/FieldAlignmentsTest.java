package org.nucleus8583.core.xml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.nucleus8583.core.field.Alignment;

public class FieldAlignmentsTest {

	@Test
	public void symbolicValueTest() {
		assertEquals('l', Alignment.TRIMMED_LEFT.symbolicValue());
		assertEquals('r', Alignment.TRIMMED_RIGHT.symbolicValue());
		assertEquals('n', Alignment.NONE.symbolicValue());
	}

	@Test
	public void enumValueOfTest() {
		assertEquals(Alignment.TRIMMED_LEFT, Alignment.enumValueOf("left"));
		assertEquals(Alignment.TRIMMED_LEFT, Alignment.enumValueOf("lefT"));

		assertEquals(Alignment.TRIMMED_RIGHT, Alignment.enumValueOf("right"));
		assertEquals(Alignment.TRIMMED_RIGHT, Alignment.enumValueOf("rIgHt"));

		assertEquals(Alignment.NONE, Alignment.enumValueOf("none"));
		assertEquals(Alignment.NONE, Alignment.enumValueOf("nOnE"));

		assertEquals(null, Alignment.enumValueOf("l3ft"));
		assertEquals(null, Alignment.enumValueOf(""));
		assertEquals(null, Alignment.enumValueOf(null));
		assertEquals(null, Alignment.enumValueOf("zzz"));
	}
}
