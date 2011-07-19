package org.nucleus8583.core.field.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.util.BitSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.charset.Charsets;
import org.nucleus8583.core.field.type.FieldType;
import org.nucleus8583.core.field.type.FieldTypes;
import org.nucleus8583.core.util.NullOutputStream;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public class BinaryTest {

	private FieldType binaryField;

	private CharsetEncoder encoder;

	private CharsetDecoder decoder;

	@Before
	public void before() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(
				Iso8583FieldDefinition.class).createUnmarshaller();

		binaryField = FieldTypes.getType((Iso8583FieldDefinition) unmarshaller
				.unmarshal(new ByteArrayInputStream(
						("<iso-field id=\"35\" type=\"b\" length=\"1\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />")
								.getBytes())));

		encoder = Charsets.getProvider("ASCII").getEncoder();
		decoder = Charsets.getProvider("ASCII").getDecoder();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void packString() throws Exception {
		binaryField.write(new NullOutputStream(), encoder, "a");
	}

	@Test
	public void packEmptyBinary() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		binaryField.write(out, encoder, new BitSet());

		assertEquals("00", out.toString());
	}

	@Test
	public void packBinary() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BitSet bs = new BitSet();

		binaryField.write(out, encoder, bs);
		assertEquals("00", out.toString());

		bs.clear();
		bs.set(0, true);

		binaryField.write(out, encoder, bs);
		assertEquals("0080", out.toString());

		bs.clear();
		bs.set(1, true);

		binaryField.write(out, encoder, bs);
		assertEquals("008040", out.toString());

		bs.clear();
		bs.set(2, true);

		binaryField.write(out, encoder, bs);
		assertEquals("00804020", out.toString());

		bs.clear();
		bs.set(3, true);

		binaryField.write(out, encoder, bs);
		assertEquals("0080402010", out.toString());

		bs.clear();
		bs.set(0, 4, true);

		binaryField.write(out, encoder, bs);
		assertEquals("0080402010F0", out.toString());
	}

	@Test
	public void unpackBinary1() throws Exception {
		BitSet bits = new BitSet();

		bits.clear();
		binaryField.read(new ByteArrayInputStream("00".getBytes()), decoder, bits);
		assertTrue(bits.cardinality() == 0);

		bits.clear();
		binaryField.read(new ByteArrayInputStream("10".getBytes()), decoder, bits);
		assertTrue(!bits.get(0) && !bits.get(1) && !bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 1));

		bits.clear();
		binaryField.read(new ByteArrayInputStream("20".getBytes()), decoder, bits);
		assertTrue(!bits.get(0) && !bits.get(1) && bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 1));

		bits.clear();
		binaryField.read(new ByteArrayInputStream("30".getBytes()), decoder, bits);
		assertTrue(!bits.get(0) && !bits.get(1) && bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 2));

		bits.clear();
		binaryField.read(new ByteArrayInputStream("40".getBytes()), decoder, bits);
		assertTrue(!bits.get(0) && bits.get(1) && !bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 1));

		bits.clear();
		binaryField.read(new ByteArrayInputStream("50".getBytes()), decoder, bits);
		assertTrue(!bits.get(0) && bits.get(1) && !bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 2));

		bits.clear();
		binaryField.read(new ByteArrayInputStream("60".getBytes()), decoder, bits);
		assertTrue(!bits.get(0) && bits.get(1) && bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 2));

		bits.clear();
		binaryField.read(new ByteArrayInputStream("70".getBytes()), decoder, bits);
		assertTrue(!bits.get(0) && bits.get(1) && bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 3));

		bits.clear();
		binaryField.read(new ByteArrayInputStream("80".getBytes()), decoder, bits);
		assertTrue(bits.get(0) && !bits.get(1) && !bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 1));

		bits.clear();
		binaryField.read(new ByteArrayInputStream("90".getBytes()), decoder, bits);
		assertTrue(bits.get(0) && !bits.get(1) && !bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 2));

		bits.clear();
		binaryField.read(new ByteArrayInputStream("A0".getBytes()), decoder, bits);
		assertTrue(bits.get(0) && !bits.get(1) && bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 2));

		bits.clear();
		binaryField.read(new ByteArrayInputStream("B0".getBytes()), decoder, bits);
		assertTrue(bits.get(0) && !bits.get(1) && bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 3));

		bits.clear();
		binaryField.read(new ByteArrayInputStream("C0".getBytes()), decoder, bits);
		assertTrue(bits.get(0) && bits.get(1) && !bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 2));

		bits.clear();
		binaryField.read(new ByteArrayInputStream("D0".getBytes()), decoder, bits);
		assertTrue(bits.get(0) && bits.get(1) && !bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 3));

		bits.clear();
		binaryField.read(new ByteArrayInputStream("E0".getBytes()), decoder, bits);
		assertTrue(bits.get(0) && bits.get(1) && bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 3));

		bits.clear();
		binaryField.read(new ByteArrayInputStream("F0".getBytes()), decoder, bits);
		assertTrue(bits.get(0) && bits.get(1) && bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 4));
	}

	@Test
	public void unpackBinary2() throws Exception {
		BitSet bits = binaryField.readBinary(new ByteArrayInputStream("00".getBytes()), decoder);
		assertTrue(bits.cardinality() == 0);

		bits = binaryField.readBinary(new ByteArrayInputStream("10".getBytes()), decoder);
		assertTrue(!bits.get(0) && !bits.get(1) && !bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 1));

		bits = binaryField.readBinary(new ByteArrayInputStream("20".getBytes()), decoder);
		assertTrue(!bits.get(0) && !bits.get(1) && bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 1));

		bits = binaryField.readBinary(new ByteArrayInputStream("30".getBytes()), decoder);
		assertTrue(!bits.get(0) && !bits.get(1) && bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 2));

		bits = binaryField.readBinary(new ByteArrayInputStream("40".getBytes()), decoder);
		assertTrue(!bits.get(0) && bits.get(1) && !bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 1));

		bits = binaryField.readBinary(new ByteArrayInputStream("50".getBytes()), decoder);
		assertTrue(!bits.get(0) && bits.get(1) && !bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 2));

		bits = binaryField.readBinary(new ByteArrayInputStream("60".getBytes()), decoder);
		assertTrue(!bits.get(0) && bits.get(1) && bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 2));

		bits = binaryField.readBinary(new ByteArrayInputStream("70".getBytes()), decoder);
		assertTrue(!bits.get(0) && bits.get(1) && bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 3));

		bits = binaryField.readBinary(new ByteArrayInputStream("80".getBytes()), decoder);
		assertTrue(bits.get(0) && !bits.get(1) && !bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 1));

		bits = binaryField.readBinary(new ByteArrayInputStream("90".getBytes()), decoder);
		assertTrue(bits.get(0) && !bits.get(1) && !bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 2));

		bits = binaryField.readBinary(new ByteArrayInputStream("A0".getBytes()), decoder);
		assertTrue(bits.get(0) && !bits.get(1) && bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 2));

		bits = binaryField.readBinary(new ByteArrayInputStream("B0".getBytes()), decoder);
		assertTrue(bits.get(0) && !bits.get(1) && bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 3));

		bits = binaryField.readBinary(new ByteArrayInputStream("C0".getBytes()), decoder);
		assertTrue(bits.get(0) && bits.get(1) && !bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 2));

		bits = binaryField.readBinary(new ByteArrayInputStream("D0".getBytes()), decoder);
		assertTrue(bits.get(0) && bits.get(1) && !bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 3));

		bits = binaryField.readBinary(new ByteArrayInputStream("E0".getBytes()), decoder);
		assertTrue(bits.get(0) && bits.get(1) && bits.get(2) && !bits.get(3)
				&& (bits.cardinality() == 3));

		bits = binaryField.readBinary(new ByteArrayInputStream("F0".getBytes()), decoder);
		assertTrue(bits.get(0) && bits.get(1) && bits.get(2) && bits.get(3)
				&& (bits.cardinality() == 4));
	}

	@Test(expected = EOFException.class)
	public void unpackEmptyBinary1() throws Exception {
		binaryField.readBinary(new ByteArrayInputStream("".getBytes()), decoder);
	}

	@Test(expected = EOFException.class)
	public void unpackEmptyBinary2() throws Exception {
		binaryField.read(new ByteArrayInputStream("".getBytes()), decoder, new BitSet());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void unpackString() throws Exception {
		binaryField.readString(new ByteArrayInputStream("".getBytes()), decoder);
	}
}
