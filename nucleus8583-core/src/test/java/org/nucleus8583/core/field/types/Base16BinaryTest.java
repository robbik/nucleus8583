package org.nucleus8583.core.field.types;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.field.Type;
import org.nucleus8583.core.field.spi.Base16Binary;
import org.nucleus8583.core.util.BitmapHelper;
import org.nucleus8583.core.util.NullOutputStream;

public class Base16BinaryTest {

	private Type binaryField;

	@Before
	public void before() throws Exception {
		
		binaryField = new Base16Binary();
		((Base16Binary) binaryField).setLength(1);
	}

	@Test(expected = ClassCastException.class)
	public void packString() throws Exception {
		binaryField.write(new NullOutputStream(), "a");
	}

	@Test(expected = IllegalArgumentException.class)
	public void packEmptyBinary() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		binaryField.write(out, new byte[0]);
	}

	@Test
	public void packBinary() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

//		binaryField.write(out, new byte[0]);
//		assertEquals("00", out.toString());

		byte[] bs = BitmapHelper.create(8);
		BitmapHelper.set(bs, 0);

		binaryField.write(out, bs);
		assertEquals("80", out.toString());

		BitmapHelper.clear(bs);
		BitmapHelper.set(bs, 1);

		binaryField.write(out, bs);
		assertEquals("8040", out.toString());

		BitmapHelper.clear(bs);
		BitmapHelper.set(bs, 2);

		binaryField.write(out, bs);
		assertEquals("804020", out.toString());

		BitmapHelper.clear(bs);
		BitmapHelper.set(bs, 3);

		binaryField.write(out, bs);
		assertEquals("80402010", out.toString());

		BitmapHelper.clear(bs);
		BitmapHelper.set(bs, 0);
        BitmapHelper.set(bs, 1);
        BitmapHelper.set(bs, 2);
        BitmapHelper.set(bs, 3);

		binaryField.write(out, bs);
		assertEquals("80402010F0", out.toString());
	}

	@Test(expected = EOFException.class)
	public void unpackEmptyBinary1() throws Exception {
		binaryField.read(new ByteArrayInputStream("".getBytes()));
	}
}
