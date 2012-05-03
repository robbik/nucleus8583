package rk.commons.ioc.factory.xml;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class BeanDefinitionReader implements ErrorHandler, EntityResolver {

	private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

	private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

	private DocumentBuilderFactory factory;

	private DocumentBuilder docBuilder;

	private URL docURL;

	private NamespaceSchemaResolver resolver;

	public BeanDefinitionReader(URL docURL, NamespaceSchemaResolver resolver) {
		this.docURL = docURL;
		this.resolver = resolver;

		initialize();

		try {
			docBuilder = factory.newDocumentBuilder();
		} catch (RuntimeException ex) {
			throw ex;
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}

		docBuilder.setEntityResolver(this);

		docBuilder.setErrorHandler(this);
	}

	private void initialize() {
		factory = DocumentBuilderFactory.newInstance();

		factory.setIgnoringComments(true);
		factory.setIgnoringElementContentWhitespace(true);
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		factory.setXIncludeAware(true);
		try {
			factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
		} catch (Throwable t) {
			// do nothing
		}
	}

	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {

		URL schemaURL = resolver.tryResolve(systemId);

		if (schemaURL == null) {
			return null;
		} else {
			return new InputSource(schemaURL.openStream());
		}
	}

	public void error(SAXParseException ex) throws SAXException {
		throw ex;
	}

	public void fatalError(SAXParseException ex) throws SAXException {
		throw ex;
	}

	public void warning(SAXParseException ex) throws SAXException {
		// do nothing
	}

	public Element parse() throws IOException, SAXException {
		Document doc = docBuilder.parse(docURL.openStream());
		doc.setStrictErrorChecking(true);
		doc.normalizeDocument();

		return doc.getDocumentElement();
	}
}
