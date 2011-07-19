package org.nucleus8583.core.field.types;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.BitSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.field.type.Iso8583FieldType;
import org.nucleus8583.core.field.type.Iso8583FieldTypes;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public class Iso8583StringTest {
	private Iso8583FieldType stringFieldAlignL;

	private Iso8583FieldType stringFieldAlignR;

	private Iso8583FieldType stringFieldAlignN;

	@Before
	public void before() throws Exception {
		Unmarshaller unmarshaller = JAXBContext.newInstance(
				Iso8583FieldDefinition.class).createUnmarshaller();

		stringFieldAlignL = Iso8583FieldTypes.getType((Iso8583FieldDefinition) unmarshaller
				.unmarshal(new ByteArrayInputStream(
						("<iso-field id=\"39\" type=\"a\" length=\"2\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />")
								.getBytes())));

		stringFieldAlignR = Iso8583FieldTypes.getType((Iso8583FieldDefinition) unmarshaller
				.unmarshal(new ByteArrayInputStream(
						("<iso-field id=\"39\" type=\"custom\" align=\"right\" pad-with=\" \" length=\"2\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />")
								.getBytes())));

		stringFieldAlignN = Iso8583FieldTypes.getType((Iso8583FieldDefinition) unmarshaller
				.unmarshal(new ByteArrayInputStream(
						("<iso-field id=\"39\" type=\"custom\" align=\"none\" pad-with=\"\" length=\"2\" xmlns=\"http://www.nucleus8583.org/schema/iso-message\" />")
								.getBytes())));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void packBinary() throws Exception {
		stringFieldAlignL.write(new StringWriter(), new BitSet());
	}

	@Test
	public void packStringTooLong() throws Exception {
		StringWriter sw = new StringWriter();
		String errorMsg = null;

		try {
			stringFieldAlignL.write(sw, "1124134=2343434");
		} catch (IllegalArgumentException ex) {
			errorMsg = ex.getMessage();
		}

		assertEquals(
				"value of field #39 is too long, expected 2 but actual is 15",
				errorMsg);
	}

	@Test
	public void packStringNoPad() throws Exception {
		StringWriter sw = new StringWriter();
		stringFieldAlignL.write(sw, "20");
		assertEquals("20", sw.toString());

		sw = new StringWriter();
		stringFieldAlignR.write(sw, "20");
		assertEquals("20", sw.toString());

		sw = new StringWriter();
		stringFieldAlignN.write(sw, "20");
		assertEquals("20", sw.toString());
	}

	@Test
	public void packEmptyString() throws Exception {
		StringWriter sw = new StringWriter();
		stringFieldAlignL.write(sw, "");
		assertEquals("  ", sw.toString());

		sw = new StringWriter();
		stringFieldAlignR.write(sw, "");
		assertEquals("  ", sw.toString());

		sw = new StringWriter();
		stringFieldAlignN.write(sw, "");
		assertEquals("  ", sw.toString());
	}

	@Test
	public void packStringWithPad() throws Exception {
		StringWriter sw = new StringWriter();
		stringFieldAlignL.write(sw, "j");
		sw.flush();
		assertEquals("j ", sw.toString());

		sw = new StringWriter();
		stringFieldAlignR.write(sw, "j");
		sw.flush();
		assertEquals(" j", sw.toString());

		sw = new StringWriter();
		stringFieldAlignN.write(sw, "j");
		sw.flush();
		assertEquals("j ", sw.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void packStringOverflow() throws Exception {
		stringFieldAlignL.write(new StringWriter(), "300");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void unpackBinary1() throws Exception {
		stringFieldAlignL.read(new StringReader("a"), new BitSet());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void unpackBinary2() throws Exception {
		stringFieldAlignL.readBinary(new StringReader("a"));
	}

	@Test
	public void unpackStringNoUnpad() throws Exception {
		assertEquals("20", stringFieldAlignL.readString(new StringReader("20")));

		assertEquals("20", stringFieldAlignR.readString(new StringReader("20")));

		assertEquals("20", stringFieldAlignN.readString(new StringReader("20")));
	}

	@Test
	public void unpackEmptyString() throws Exception {
		assertEquals("", stringFieldAlignL.readString(new StringReader("  ")));

		assertEquals("", stringFieldAlignR.readString(new StringReader("  ")));

		assertEquals("  ", stringFieldAlignN.readString(new StringReader("  ")));
	}

	@Test
	public void unpackStringUnpad() throws Exception {
		assertEquals("j", stringFieldAlignL.readString(new StringReader("j ")));

		assertEquals("j", stringFieldAlignR.readString(new StringReader(" j")));

		assertEquals("j ", stringFieldAlignN.readString(new StringReader("j ")));
	}

	@Test
	public void unpackStringUnpadOverflow() throws Exception {
		assertEquals("j", stringFieldAlignL.readString(new StringReader("j kl")));

		assertEquals("j ", stringFieldAlignR.readString(new StringReader("j kl")));

		assertEquals("j ", stringFieldAlignN.readString(new StringReader("j kl")));
	}
}
