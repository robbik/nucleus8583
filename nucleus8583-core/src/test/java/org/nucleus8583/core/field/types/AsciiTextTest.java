package org.nucleus8583.core.field.types;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.field.Type;
import org.nucleus8583.core.field.spi.AsciiText;
import org.nucleus8583.core.xml.Alignment;

public class AsciiTextTest {

	private Type alignL;

	private Type alignR;

	private Type alignN;

	@Before
	public void before() throws Exception {
        alignL = new AsciiText();
        ((AsciiText) alignL).setAlignment(Alignment.TRIMMED_LEFT);
        ((AsciiText) alignL).setLength(2);

        alignR = new AsciiText();
        ((AsciiText) alignR).setAlignment(Alignment.TRIMMED_RIGHT);
        ((AsciiText) alignR).setPadWith(" ");
        ((AsciiText) alignR).setLength(2);

        alignN = new AsciiText();
        ((AsciiText) alignN).setAlignment(Alignment.NONE);
        ((AsciiText) alignN).setLength(2);
        
        ((AsciiText) alignL).initialize();
        ((AsciiText) alignR).initialize();
        ((AsciiText) alignN).initialize();
	}

	@Test(expected = ClassCastException.class)
	public void packBinary() throws Exception {
		alignL.write(new ByteArrayOutputStream(), new byte[0]);
	}

	@Test
	public void packStringTooLong() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String errorMsg = null;

		try {
			alignL.write(out, "1124134=2343434");
		} catch (IllegalArgumentException ex) {
			errorMsg = ex.getMessage();
		}

		assertEquals("value too long, expected 2 but actual is 15", errorMsg);
	}

	@Test
	public void packStringNoPad() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		alignL.write(out, "20");
		assertEquals("20", out.toString());

		out = new ByteArrayOutputStream();
		alignR.write(out, "20");
		assertEquals("20", out.toString());

		out = new ByteArrayOutputStream();
		alignN.write(out, "20");
		assertEquals("20", out.toString());
	}

	@Test
	public void packEmptyString() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		alignL.write(out, "");
		assertEquals("  ", out.toString());

		out = new ByteArrayOutputStream();
		alignR.write(out, "");
		assertEquals("  ", out.toString());

		out = new ByteArrayOutputStream();
		alignN.write(out, "");
		assertEquals("  ", out.toString());
	}

	@Test
	public void packStringWithPad() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		alignL.write(out, "j");
		out.flush();
		assertEquals("j ", out.toString());

		out = new ByteArrayOutputStream();
		alignR.write(out, "j");
		out.flush();
		assertEquals(" j", out.toString());

		out = new ByteArrayOutputStream();
		alignN.write(out, "j");
		out.flush();
		assertEquals("j ", out.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void packStringOverflow() throws Exception {
		alignL.write(new ByteArrayOutputStream(), "300");
	}

	@Test
	public void unpackStringNoUnpad() throws Exception {
		assertEquals("20", alignL.read(new ByteArrayInputStream("20".getBytes())));

		assertEquals("20", alignR.read(new ByteArrayInputStream("20".getBytes())));

		assertEquals("20", alignN.read(new ByteArrayInputStream("20".getBytes())));
	}

	@Test
	public void unpackEmptyString() throws Exception {
		assertEquals("", alignL.read(new ByteArrayInputStream("  ".getBytes())));

		assertEquals("", alignR.read(new ByteArrayInputStream("  ".getBytes())));

		assertEquals("  ", alignN.read(new ByteArrayInputStream("  ".getBytes())));
	}

	@Test
	public void unpackStringUnpad() throws Exception {
		assertEquals("j", alignL.read(new ByteArrayInputStream("j ".getBytes())));

		assertEquals("j", alignR.read(new ByteArrayInputStream(" j".getBytes())));

		assertEquals("j ", alignN.read(new ByteArrayInputStream("j ".getBytes())));
	}

	@Test
	public void unpackStringUnpadOverflow() throws Exception {
		assertEquals("j", alignL.read(new ByteArrayInputStream("j kl".getBytes())));

		assertEquals("j ", alignR.read(new ByteArrayInputStream("j kl".getBytes())));

		assertEquals("j ", alignN.read(new ByteArrayInputStream("j kl".getBytes())));
	}
}
