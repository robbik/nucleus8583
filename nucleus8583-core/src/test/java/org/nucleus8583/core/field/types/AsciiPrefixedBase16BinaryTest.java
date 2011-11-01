package org.nucleus8583.core.field.types;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.field.type.FieldType;
import org.nucleus8583.core.field.type.FieldTypes;
import org.nucleus8583.core.xml.FieldDefinition;

public class AsciiPrefixedBase16BinaryTest {

	private FieldType binaryL;

    private FieldType binaryLL;

    private FieldType binaryLLL;

	@Before
	public void before() throws Exception {
	    FieldTypes.initialize();

        FieldDefinition def = new FieldDefinition();
        def.setId(120);
        def.setType("b.");

		binaryL = FieldTypes.getType(def);

        def = new FieldDefinition();
        def.setId(120);
        def.setType("b..");

        binaryLL = FieldTypes.getType(def);

        def = new FieldDefinition();
        def.setId(120);
        def.setType("b...");

        binaryLLL = FieldTypes.getType(def);
	}

    @Test
	public void packBinary() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
		binaryL.write(out, new byte[] { 0x20 });

        assertEquals("220", out.toString());

        out = new ByteArrayOutputStream();
        binaryLL.write(out, new byte[] { 0x20 });

        assertEquals("0220", out.toString());

        out = new ByteArrayOutputStream();
        binaryLLL.write(out, new byte[] { 0x20 });

        assertEquals("00220", out.toString());
	}

    @Test(expected = UnsupportedOperationException.class)
	public void packString1() throws Exception {
		binaryL.write(new ByteArrayOutputStream(), "");
	}

    @Test(expected = UnsupportedOperationException.class)
    public void packString2() throws Exception {
        binaryLL.write(new ByteArrayOutputStream(), "");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void packString3() throws Exception {
        binaryLLL.write(new ByteArrayOutputStream(), "");
    }

	@Test
	public void packEmptyBinary() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		binaryL.write(out, new byte[0]);

		assertEquals("0", out.toString());

        out = new ByteArrayOutputStream();
        binaryLL.write(out, new byte[0]);

        assertEquals("00", out.toString());

        out = new ByteArrayOutputStream();
        binaryLLL.write(out, new byte[0]);

        assertEquals("000", out.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void packBinaryOverflow() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		binaryL.write(out, new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
		        0x07, 0x08, 0x09, 0x0A });
	}

	@Test(expected = UnsupportedOperationException.class)
	public void unpackString1() throws Exception {
		binaryL.readString(new ByteArrayInputStream("a".getBytes()));
	}

    @Test(expected = UnsupportedOperationException.class)
    public void unpackString2() throws Exception {
        binaryLL.readString(new ByteArrayInputStream("a".getBytes()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unpackString3() throws Exception {
        binaryLLL.readString(new ByteArrayInputStream("a".getBytes()));
    }

	@Test
	public void unpackBinary() throws Exception {
	    byte[] val = binaryL.readBinary(new ByteArrayInputStream("120".getBytes()));
	    assertEquals(1, val.length);
		assertEquals(0x20, val[0]);

        val = binaryLL.readBinary(new ByteArrayInputStream("0120".getBytes()));
        assertEquals(1, val.length);
        assertEquals(0x20, val[0]);

        val = binaryLLL.readBinary(new ByteArrayInputStream("00120".getBytes()));
        assertEquals(1, val.length);
        assertEquals(0x20, val[0]);
	}

	@Test
	public void unpackEmptyBinary() throws Exception {
        assertEquals(0, binaryL.readBinary(new ByteArrayInputStream("0".getBytes())).length);
        assertEquals(0, binaryLL.readBinary(new ByteArrayInputStream("00".getBytes())).length);
        assertEquals(0, binaryLLL.readBinary(new ByteArrayInputStream("000".getBytes())).length);
	}

	@Test(expected = EOFException.class)
	public void unpackBinaryOverflow() throws Exception {
		binaryL.readBinary(new ByteArrayInputStream("5ab".getBytes()));
	}
}
