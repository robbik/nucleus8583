package org.nucleus8583.core.field.types;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.field.Type;
import org.nucleus8583.core.field.spi.AsciiPrefixedBase16Binary;

public class AsciiPrefixedBase16BinaryTest {

	private Type binaryL;

    private Type binaryLL;

    private Type binaryLLL;

	@Before
	public void before() throws Exception {
		binaryL = new AsciiPrefixedBase16Binary();
		((AsciiPrefixedBase16Binary) binaryL).setPrefixLength(1);
		((AsciiPrefixedBase16Binary) binaryL).setMaxLength(9);

		binaryLL = new AsciiPrefixedBase16Binary();
		((AsciiPrefixedBase16Binary) binaryLL).setPrefixLength(2);
		((AsciiPrefixedBase16Binary) binaryLL).setMaxLength(99);

		binaryLLL = new AsciiPrefixedBase16Binary();
		((AsciiPrefixedBase16Binary) binaryLLL).setPrefixLength(3);
		((AsciiPrefixedBase16Binary) binaryLLL).setMaxLength(999);
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

    @Test(expected = ClassCastException.class)
	public void packString1() throws Exception {
		binaryL.write(new ByteArrayOutputStream(), "");
	}

    @Test(expected = ClassCastException.class)
    public void packString2() throws Exception {
        binaryLL.write(new ByteArrayOutputStream(), "");
    }

    @Test(expected = ClassCastException.class)
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

	@Test
	public void unpackBinary() throws Exception {
	    byte[] val = (byte[]) binaryL.read(new ByteArrayInputStream("120".getBytes()));
	    assertEquals(1, val.length);
		assertEquals(0x20, val[0]);

        val = (byte[]) binaryLL.read(new ByteArrayInputStream("0120".getBytes()));
        assertEquals(1, val.length);
        assertEquals(0x20, val[0]);

        val = (byte[]) binaryLLL.read(new ByteArrayInputStream("00120".getBytes()));
        assertEquals(1, val.length);
        assertEquals(0x20, val[0]);
	}

	@Test
	public void unpackEmptyBinary() throws Exception {
        assertArrayEquals(new byte[0], (byte[]) binaryL.read(new ByteArrayInputStream("0".getBytes())));
        assertArrayEquals(new byte[0], (byte[]) binaryLL.read(new ByteArrayInputStream("00".getBytes())));
        assertArrayEquals(new byte[0], (byte[]) binaryLLL.read(new ByteArrayInputStream("000".getBytes())));
	}

	@Test(expected = EOFException.class)
	public void unpackBinaryOverflow() throws Exception {
		binaryL.read(new ByteArrayInputStream("5ab".getBytes()));
	}
}
