package org.nucleus8583.core;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.BitSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public class Iso8583StringTest {
	private Iso8583String stringFieldAlignL;

	private Iso8583String stringFieldAlignR;

	private Iso8583String stringFieldAlignN;

	@Before
	public void before() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(
				Iso8583FieldDefinition.class).createUnmarshaller();

		stringFieldAlignL = (Iso8583String) ((Iso8583FieldDefinition) unmarshaller
				.unmarshal(new ByteArrayInputStream(
						("<iso-field id=\"39\" type=\"a\" length=\"2\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />")
								.getBytes()))).createField();

		stringFieldAlignR = (Iso8583String) ((Iso8583FieldDefinition) unmarshaller
				.unmarshal(new ByteArrayInputStream(
						("<iso-field id=\"39\" type=\"custom\" align=\"right\" pad-with=\" \" length=\"2\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />")
								.getBytes()))).createField();

		stringFieldAlignN = (Iso8583String) ((Iso8583FieldDefinition) unmarshaller
				.unmarshal(new ByteArrayInputStream(
						("<iso-field id=\"39\" type=\"custom\" align=\"none\" pad-with=\"\" length=\"2\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />")
								.getBytes()))).createField();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void packBinary() throws Exception {
		stringFieldAlignL.pack(new StringWriter(), new BitSet());
	}

	@Test
	public void packStringNoPad() throws Exception {
		StringWriter sw = new StringWriter();
		stringFieldAlignL.pack(sw, "20");
		assertEquals("20", sw.toString());

		sw = new StringWriter();
		stringFieldAlignR.pack(sw, "20");
		assertEquals("20", sw.toString());

		sw = new StringWriter();
		stringFieldAlignN.pack(sw, "20");
		assertEquals("20", sw.toString());
	}

	@Test
	public void packEmptyString() throws Exception {
		StringWriter sw = new StringWriter();
		stringFieldAlignL.pack(sw, "");
		assertEquals("  ", sw.toString());

		sw = new StringWriter();
		stringFieldAlignR.pack(sw, "");
		assertEquals("  ", sw.toString());

		sw = new StringWriter();
		stringFieldAlignN.pack(sw, "");
		assertEquals("  ", sw.toString());
	}

	@Test
	public void packStringWithPad() throws Exception {
		StringWriter sw = new StringWriter();
		stringFieldAlignL.pack(sw, "j");
		sw.flush();
		assertEquals("j ", sw.toString());

		sw = new StringWriter();
		stringFieldAlignR.pack(sw, "j");
		sw.flush();
		assertEquals(" j", sw.toString());

		sw = new StringWriter();
		stringFieldAlignN.pack(sw, "j");
		sw.flush();
		assertEquals("j ", sw.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void packStringOverflow() throws Exception {
		stringFieldAlignL.pack(new StringWriter(), "300");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void unpackBinary1() throws Exception {
		stringFieldAlignL.unpackBinary(new StringReader("a"), new BitSet());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void unpackBinary2() throws Exception {
		stringFieldAlignL.unpackBinary(new StringReader("a"));
	}

	@Test
	public void unpackStringNoUnpad() throws Exception {
		assertEquals("20", stringFieldAlignL
				.unpackString(new StringReader("20")));

		assertEquals("20", stringFieldAlignR
				.unpackString(new StringReader("20")));

		assertEquals("20", stringFieldAlignN
				.unpackString(new StringReader("20")));
	}

	@Test
	public void unpackEmptyString() throws Exception {
		assertEquals("", stringFieldAlignL.unpackString(new StringReader("  ")));

		assertEquals("", stringFieldAlignR.unpackString(new StringReader("  ")));

		assertEquals("  ", stringFieldAlignN
				.unpackString(new StringReader("  ")));
	}

	@Test
	public void unpackStringUnpad() throws Exception {
		assertEquals("j", stringFieldAlignL
				.unpackString(new StringReader("j ")));

		assertEquals("j", stringFieldAlignR
				.unpackString(new StringReader(" j")));

		assertEquals("j ", stringFieldAlignN
				.unpackString(new StringReader("j ")));
	}

	@Test
	public void unpackStringUnpadOverflow() throws Exception {
		assertEquals("j", stringFieldAlignL.unpackString(new StringReader(
				"j kl")));

		assertEquals("j ", stringFieldAlignR.unpackString(new StringReader(
				"j kl")));

		assertEquals("j ", stringFieldAlignN.unpackString(new StringReader(
				"j kl")));
	}
}
