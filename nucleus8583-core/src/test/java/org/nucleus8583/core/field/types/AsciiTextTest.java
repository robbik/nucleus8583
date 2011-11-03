package org.nucleus8583.core.field.types;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.field.type.FieldType;
import org.nucleus8583.core.field.type.FieldTypes;
import org.nucleus8583.core.xml.FieldAlignments;
import org.nucleus8583.core.xml.FieldDefinition;

public class AsciiTextTest {

	private FieldType alignL;

	private FieldType alignR;

	private FieldType alignN;

	@Before
	public void before() throws Exception {
	    FieldTypes.initialize();

        FieldDefinition def = new FieldDefinition();
        def.setId(39);
        def.setType("a");
        def.setLength(2);

        alignL = FieldTypes.getType(def);

        def = new FieldDefinition();
        def.setId(39);
        def.setType("custom");
        def.setAlign(FieldAlignments.TRIMMED_RIGHT);
        def.setPadWith(" ");
        def.setLength(2);

        alignR = FieldTypes.getType(def);

        def = new FieldDefinition();
        def.setId(39);
        def.setType("custom");
        def.setAlign(FieldAlignments.NONE);
        def.setPadWith("");
        def.setLength(2);

		alignN = FieldTypes.getType(def);
	}

	@Test(expected = UnsupportedOperationException.class)
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

		assertEquals("value of field #39 is too long, expected 2 but actual is 15", errorMsg);
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

	@Test(expected = UnsupportedOperationException.class)
	public void unpackBinary1() throws Exception {
		alignL.read(new ByteArrayInputStream("a".getBytes()), new byte[0], 0, 0);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void unpackBinary2() throws Exception {
		alignL.readBinary(new ByteArrayInputStream("a".getBytes()));
	}

	@Test
	public void unpackStringNoUnpad() throws Exception {
		assertEquals("20", alignL.readString(new ByteArrayInputStream("20".getBytes())));

		assertEquals("20", alignR.readString(new ByteArrayInputStream("20".getBytes())));

		assertEquals("20", alignN.readString(new ByteArrayInputStream("20".getBytes())));
	}

	@Test
	public void unpackEmptyString() throws Exception {
		assertEquals("", alignL.readString(new ByteArrayInputStream("  ".getBytes())));

		assertEquals("", alignR.readString(new ByteArrayInputStream("  ".getBytes())));

		assertEquals("  ", alignN.readString(new ByteArrayInputStream("  ".getBytes())));
	}

	@Test
	public void unpackStringUnpad() throws Exception {
		assertEquals("j", alignL.readString(new ByteArrayInputStream("j ".getBytes())));

		assertEquals("j", alignR.readString(new ByteArrayInputStream(" j".getBytes())));

		assertEquals("j ", alignN.readString(new ByteArrayInputStream("j ".getBytes())));
	}

	@Test
	public void unpackStringUnpadOverflow() throws Exception {
		assertEquals("j", alignL.readString(new ByteArrayInputStream("j kl".getBytes())));

		assertEquals("j ", alignR.readString(new ByteArrayInputStream("j kl".getBytes())));

		assertEquals("j ", alignN.readString(new ByteArrayInputStream("j kl".getBytes())));
	}
}
