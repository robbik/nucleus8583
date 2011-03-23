package org.nucleus8583.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.BitSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public class Iso8583BinaryTest {
	private Iso8583Binary binaryField;

	@Before
	public void before() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(
				Iso8583FieldDefinition.class).createUnmarshaller();

		binaryField = (Iso8583Binary) ((Iso8583FieldDefinition) unmarshaller
				.unmarshal(new ByteArrayInputStream(
						("<iso-field id=\"35\" type=\"b\" length=\"2\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />")
								.getBytes()))).createField();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void packString() throws Exception {
		binaryField.pack(new StringWriter(), "a");
	}

	@Test
	public void packEmptyBinary() throws Exception {
		StringWriter sw = new StringWriter();
		binaryField.pack(sw, new BitSet());

		assertEquals("00", sw.toString());
	}

	@Test
	public void packBinary() throws Exception {
		StringWriter sw = new StringWriter();
		BitSet bs = new BitSet();

		binaryField.pack(sw, bs);
		assertEquals("00", sw.toString());

		bs.clear();
		bs.set(0, true);

		binaryField.pack(sw, bs);
		assertEquals("0080", sw.toString());

		bs.clear();
		bs.set(1, true);

		binaryField.pack(sw, bs);
		assertEquals("008040", sw.toString());

		bs.clear();
		bs.set(2, true);

		binaryField.pack(sw, bs);
		assertEquals("00804020", sw.toString());

		bs.clear();
		bs.set(3, true);

		binaryField.pack(sw, bs);
		assertEquals("0080402010", sw.toString());

		bs.clear();
		bs.set(0, 4, true);

		binaryField.pack(sw, bs);
		assertEquals("0080402010F0", sw.toString());
	}

	@Test
	public void unpackBinary1() throws Exception {
		BitSet bits = new BitSet();

		bits.clear();
		binaryField.unpackBinary(new StringReader("00"), bits);
		assertTrue(bits.cardinality() == 0);

		bits.clear();
		binaryField.unpackBinary(new StringReader("10"), bits);
		assertTrue(!bits.get(0) && !bits.get(1) && !bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 1));

		bits.clear();
		binaryField.unpackBinary(new StringReader("20"), bits);
		assertTrue(!bits.get(0) && !bits.get(1) && bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 1));

		bits.clear();
		binaryField.unpackBinary(new StringReader("30"), bits);
		assertTrue(!bits.get(0) && !bits.get(1) && bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 2));

		bits.clear();
		binaryField.unpackBinary(new StringReader("40"), bits);
		assertTrue(!bits.get(0) && bits.get(1) && !bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 1));

		bits.clear();
		binaryField.unpackBinary(new StringReader("50"), bits);
		assertTrue(!bits.get(0) && bits.get(1) && !bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 2));

		bits.clear();
		binaryField.unpackBinary(new StringReader("60"), bits);
		assertTrue(!bits.get(0) && bits.get(1) && bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 2));

		bits.clear();
		binaryField.unpackBinary(new StringReader("70"), bits);
		assertTrue(!bits.get(0) && bits.get(1) && bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 3));

		bits.clear();
		binaryField.unpackBinary(new StringReader("80"), bits);
		assertTrue(bits.get(0) && !bits.get(1) && !bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 1));

		bits.clear();
		binaryField.unpackBinary(new StringReader("90"), bits);
		assertTrue(bits.get(0) && !bits.get(1) && !bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 2));

		bits.clear();
		binaryField.unpackBinary(new StringReader("A0"), bits);
		assertTrue(bits.get(0) && !bits.get(1) && bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 2));

		bits.clear();
		binaryField.unpackBinary(new StringReader("B0"), bits);
		assertTrue(bits.get(0) && !bits.get(1) && bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 3));

		bits.clear();
		binaryField.unpackBinary(new StringReader("C0"), bits);
		assertTrue(bits.get(0) && bits.get(1) && !bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 2));

		bits.clear();
		binaryField.unpackBinary(new StringReader("D0"), bits);
		assertTrue(bits.get(0) && bits.get(1) && !bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 3));

		bits.clear();
		binaryField.unpackBinary(new StringReader("E0"), bits);
		assertTrue(bits.get(0) && bits.get(1) && bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 3));

		bits.clear();
		binaryField.unpackBinary(new StringReader("F0"), bits);
		assertTrue(bits.get(0) && bits.get(1) && bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 4));
	}

	@Test
	public void unpackBinary2() throws Exception {
		BitSet bits = binaryField.unpackBinary(new StringReader("00"));
		assertTrue(bits.cardinality() == 0);

		bits = binaryField.unpackBinary(new StringReader("10"));
		assertTrue(!bits.get(0) && !bits.get(1) && !bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 1));

		bits = binaryField.unpackBinary(new StringReader("20"));
		assertTrue(!bits.get(0) && !bits.get(1) && bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 1));

		bits = binaryField.unpackBinary(new StringReader("30"));
		assertTrue(!bits.get(0) && !bits.get(1) && bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 2));

		bits = binaryField.unpackBinary(new StringReader("40"));
		assertTrue(!bits.get(0) && bits.get(1) && !bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 1));

		bits = binaryField.unpackBinary(new StringReader("50"));
		assertTrue(!bits.get(0) && bits.get(1) && !bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 2));

		bits = binaryField.unpackBinary(new StringReader("60"));
		assertTrue(!bits.get(0) && bits.get(1) && bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 2));

		bits = binaryField.unpackBinary(new StringReader("70"));
		assertTrue(!bits.get(0) && bits.get(1) && bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 3));

		bits = binaryField.unpackBinary(new StringReader("80"));
		assertTrue(bits.get(0) && !bits.get(1) && !bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 1));

		bits = binaryField.unpackBinary(new StringReader("90"));
		assertTrue(bits.get(0) && !bits.get(1) && !bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 2));

		bits = binaryField.unpackBinary(new StringReader("A0"));
		assertTrue(bits.get(0) && !bits.get(1) && bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 2));

		bits = binaryField.unpackBinary(new StringReader("B0"));
		assertTrue(bits.get(0) && !bits.get(1) && bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 3));

		bits = binaryField.unpackBinary(new StringReader("C0"));
		assertTrue(bits.get(0) && bits.get(1) && !bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 2));

		bits = binaryField.unpackBinary(new StringReader("D0"));
		assertTrue(bits.get(0) && bits.get(1) && !bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 3));

		bits = binaryField.unpackBinary(new StringReader("E0"));
		assertTrue(bits.get(0) && bits.get(1) && bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 3));

		bits = binaryField.unpackBinary(new StringReader("F0"));
		assertTrue(bits.get(0) && bits.get(1) && bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 4));
	}

	@Test(expected = EOFException.class)
	public void unpackEmptyBinary1() throws Exception {
		binaryField.unpackBinary(new StringReader(""));
	}

	@Test(expected = EOFException.class)
	public void unpackEmptyBinary2() throws Exception {
		binaryField.unpackBinary(new StringReader(""), new BitSet());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void unpackString() throws Exception {
		binaryField.unpackString(new StringReader(""));
	}
}
