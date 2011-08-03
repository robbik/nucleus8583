package org.nucleus8583.core.field.types;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.charset.CharsetDecoder;
import org.nucleus8583.core.charset.CharsetEncoder;
import org.nucleus8583.core.charset.Charsets;
import org.nucleus8583.core.field.type.FieldType;
import org.nucleus8583.core.field.type.FieldTypes;
import org.nucleus8583.core.xml.FieldDefinition;

public class UnicodeVarHexBinFieldTypeTest {

	private CharsetEncoder encoder;

	private CharsetDecoder decoder;

	private FieldType binaryL;

    private FieldType binaryLL;

    private FieldType binaryLLL;

	@Before
	public void before() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(FieldDefinition.class).createUnmarshaller();

		encoder = Charsets.getProvider("ASCII").getEncoder();
		decoder = Charsets.getProvider("ASCII").getDecoder();

		binaryL = FieldTypes.getType((FieldDefinition) unmarshaller
				.unmarshal(new ByteArrayInputStream(
						("<iso-field id=\"120\" type=\"b.\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />").getBytes())));
        binaryLL = FieldTypes.getType((FieldDefinition) unmarshaller
                .unmarshal(new ByteArrayInputStream(
                        ("<iso-field id=\"120\" type=\"b..\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />").getBytes())));
        binaryLLL = FieldTypes.getType((FieldDefinition) unmarshaller
                .unmarshal(new ByteArrayInputStream(
                        ("<iso-field id=\"120\" type=\"b...\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />").getBytes())));
	}

    @Test
	public void packBinary() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
		binaryL.write(out, encoder, new byte[] { 0x20 });

        assertEquals("120", out.toString());

        out = new ByteArrayOutputStream();
        binaryLL.write(out, encoder, new byte[] { 0x20 });

        assertEquals("0120", out.toString());

        out = new ByteArrayOutputStream();
        binaryLLL.write(out, encoder, new byte[] { 0x20 });

        assertEquals("00120", out.toString());
	}

    @Test(expected = UnsupportedOperationException.class)
	public void packString1() throws Exception {
		binaryL.write(new ByteArrayOutputStream(), encoder, "");
	}

    @Test(expected = UnsupportedOperationException.class)
    public void packString2() throws Exception {
        binaryLL.write(new ByteArrayOutputStream(), encoder, "");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void packString3() throws Exception {
        binaryLLL.write(new ByteArrayOutputStream(), encoder, "");
    }

	@Test
	public void packEmptyBinary() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		binaryL.write(out, encoder, new byte[0]);

		assertEquals("0", out.toString());

        out = new ByteArrayOutputStream();
        binaryLL.write(out, encoder, new byte[0]);

        assertEquals("00", out.toString());

        out = new ByteArrayOutputStream();
        binaryLLL.write(out, encoder, new byte[0]);

        assertEquals("000", out.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void packBinaryOverflow() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		binaryL.write(out, encoder, new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
		        0x07, 0x08, 0x09, 0x0A });
	}

	@Test(expected = UnsupportedOperationException.class)
	public void unpackString1() throws Exception {
		binaryL.readString(new ByteArrayInputStream("a".getBytes()), decoder);
	}

    @Test(expected = UnsupportedOperationException.class)
    public void unpackString2() throws Exception {
        binaryLL.readString(new ByteArrayInputStream("a".getBytes()), decoder);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unpackString3() throws Exception {
        binaryLLL.readString(new ByteArrayInputStream("a".getBytes()), decoder);
    }

	@Test
	public void unpackBinary() throws Exception {
	    byte[] val = binaryL.readBinary(new ByteArrayInputStream("120".getBytes()), decoder);
	    assertEquals(1, val.length);
		assertEquals(0x20, val[0]);

        val = binaryLL.readBinary(new ByteArrayInputStream("0120".getBytes()), decoder);
        assertEquals(1, val.length);
        assertEquals(0x20, val[0]);

        val = binaryLLL.readBinary(new ByteArrayInputStream("00120".getBytes()), decoder);
        assertEquals(1, val.length);
        assertEquals(0x20, val[0]);
	}

	@Test
	public void unpackEmptyBinary() throws Exception {
        assertEquals(0, binaryL.readBinary(new ByteArrayInputStream("0".getBytes()), decoder).length);
        assertEquals(0, binaryLL.readBinary(new ByteArrayInputStream("00".getBytes()), decoder).length);
        assertEquals(0, binaryLLL.readBinary(new ByteArrayInputStream("000".getBytes()), decoder).length);
	}

	@Test(expected = EOFException.class)
	public void unpackBinaryOverflow() throws Exception {
		binaryL.readBinary(new ByteArrayInputStream("5ab".getBytes()), decoder);
	}
}
