package org.nucleus8583.core.field.types;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.field.Type;
import org.nucleus8583.core.field.spi.AsciiAmount;
import org.nucleus8583.core.xml.Alignment;

public class AsciiAmountTest {

	private Type type;
	
	@Before
	public void before() throws Exception {
		type = new AsciiAmount();
		((AsciiAmount) type).initialize();
		
		((AsciiAmount) type).setLength(9);
		
		((AsciiAmount) type).setAlignment(Alignment.UNTRIMMED_RIGHT);
		
		((AsciiAmount) type).initialize();
	}
	
	@Test
	public void testWrite() throws Exception {
		ByteArrayOutputStream mock = new ByteArrayOutputStream();

		type.write(mock, "775525");
		
		assertEquals("700075525", new String(mock.toByteArray()));
	}
	
	@Test
	public void testWrite2() throws Exception {
		ByteArrayOutputStream mock = new ByteArrayOutputStream();

		type.write(mock, "1");
		
		assertEquals("100000000", new String(mock.toByteArray()));
	}
}
