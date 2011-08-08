package org.nucleus8583.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.nucleus8583.core.charset.Charsets;
import org.nucleus8583.core.util.ResourceUtils;
import org.w3c.dom.Document;

public class Iso8583MessageFactoryTest {

	@Test
	public void createInstanceLocation() throws Exception {
		new Iso8583MessageFactory("classpath:META-INF/codec8583.xml")
				.createMessage();
	}

	@Test
	public void createInstanceLocation2() throws Exception {
		assertNotNull(Iso8583MessageFactory
				.create("classpath:META-INF/codec8583.xml"));
	}

	@Test(expected = RuntimeException.class)
	public void createInstanceLocationError() throws Exception {
		new Iso8583MessageFactory("classpath:META-INF/error8583.xml")
				.createMessage();
	}

	@Test(expected = RuntimeException.class)
	public void createInstanceLocationError2() throws Exception {
		assertNotNull(Iso8583MessageFactory
				.create("classpath:META-INF/error8583.xml"));
	}

	@Test(expected = RuntimeException.class)
	public void createInstanceLocationNotFound() throws Exception {
		new Iso8583MessageFactory("classpath:META-INF/codec8583.xml2")
				.createMessage();
	}

	@Test(expected = RuntimeException.class)
	public void createInstanceLocationNotFound2() throws Exception {
		assertNotNull(Iso8583MessageFactory
				.create("classpath:META-INF/codec8583.xml2"));
	}

	@Test
	public void createInstanceInputStream() throws Exception {
		new Iso8583MessageFactory(ResourceUtils.getURL(
				"classpath:META-INF/codec8583.xml").openStream())
				.createMessage();
	}

	@Test
	public void createInstanceInputStream2() throws Exception {
		assertNotNull(Iso8583MessageFactory.create(ResourceUtils.getURL(
				"classpath:META-INF/codec8583.xml").openStream()));
	}

	@Test(expected = RuntimeException.class)
	public void createInstanceInputStreamError() throws Exception {
		new Iso8583MessageFactory(ResourceUtils.getURL(
				"classpath:META-INF/error8583.xml").openStream())
				.createMessage();
	}

	@Test(expected = RuntimeException.class)
	public void createInstanceInputStreamError2() throws Exception {
		assertNotNull(Iso8583MessageFactory.create(ResourceUtils.getURL(
				"classpath:META-INF/error8583.xml").openStream()));
	}

	@Test
	public void createInstanceNode() throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);

		Document doc = dbf.newDocumentBuilder().parse(
				ResourceUtils.getURL("classpath:META-INF/codec8583.xml")
						.openStream());

		new Iso8583MessageFactory(doc.getFirstChild()).createMessage();
	}

	@Test
	public void createInstanceNode2() throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);

		Document doc = dbf.newDocumentBuilder().parse(
				ResourceUtils.getURL("classpath:META-INF/codec8583.xml")
						.openStream());

		assertNotNull(Iso8583MessageFactory.create(doc.getFirstChild()));
	}

	@Test
	public void createMessageTest() {
		assertNotNull(Iso8583MessageFactory.create(
				"classpath:META-INF/codec8583.xml").createMessage());
	}

	@Test
	public void getEncodingTest() {
		assertEquals("ASCII", Iso8583MessageFactory.create(
				"classpath:META-INF/codec8583.xml").getEncoding());
	}

	@Test
	public void getCharsetProviderTest() {
		assertEquals(Charsets.getProvider("ASCII"), Iso8583MessageFactory
				.create("classpath:META-INF/codec8583.xml")
				.getCharsetProvider());
	}

	@Test
	public void getFieldsTest() {
		assertEquals(129, Iso8583MessageFactory.create(
				"classpath:META-INF/codec8583.xml").getFields().length);
	}
}
