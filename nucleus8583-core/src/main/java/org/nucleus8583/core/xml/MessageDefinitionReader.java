package org.nucleus8583.core.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.nucleus8583.core.util.ResourceUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class MessageDefinitionReader implements ErrorHandler {

    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    private static final String SCHEMA_LOCATION = "classpath:META-INF/nucleus8583/schema/iso-message.xsd";

    private static final String NAMESPACE_URI = "http://www.nucleus8583.org/schema/iso-message";

    private static final DocumentBuilderFactory factory;

    private static Schema schema;

    static {
        factory = DocumentBuilderFactory.newInstance();

        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        factory.setXIncludeAware(true);

        URL schemaURL = ResourceUtils.getURL("classpath:META-INF/nucleus8583/schema/iso-message.xsd");

        schema = null;

        if (schemaURL != null) {
            try {
                schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaURL);
            } catch (SAXException e) {
                // do nothing
            }
        }

        if (schema == null) {
            try {
                factory.setAttribute(MessageDefinitionReader.JAXP_SCHEMA_LANGUAGE,
                        MessageDefinitionReader.W3C_XML_SCHEMA);
            } catch (Throwable t) {
                // do nothing
            }

            try {
                factory.setAttribute(MessageDefinitionReader.JAXP_SCHEMA_SOURCE,
                        new File(ResourceUtils.getURL(MessageDefinitionReader.SCHEMA_LOCATION).toURI()));
            } catch (Throwable t) {
                // do nothing
            }
        }
    }

    private DocumentBuilder docBuilder;

    public MessageDefinitionReader() {
        try {
            docBuilder = factory.newDocumentBuilder();
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        docBuilder.setErrorHandler(this);
    }

    private String getAttributeValue(Element el, String localName) throws DOMException {
        if (!el.hasAttribute(localName)) {
            throw new IllegalArgumentException(localName + " attribute is required under element " + el.getLocalName());
        }

        Attr attr = el.getAttributeNode(localName);
        if (!attr.getSpecified()) {
            throw new IllegalArgumentException(localName + " attribute is required under element " + el.getLocalName());
        }

        return attr.getValue();
    }

    private String getAttributeValue(Element el, String localName, String defaultValue) throws DOMException {
        if (!el.hasAttribute(localName)) {
            return defaultValue;
        }

        Attr attr = el.getAttributeNode(localName);
        if (!attr.getSpecified()) {
            return defaultValue;
        }

        return attr.getValue();
    }

    private FieldDefinition unmarshalField(Element el) {
        FieldDefinition bean = new FieldDefinition();

        bean.setId(Integer.parseInt(getAttributeValue(el, "id")));
        bean.setType(getAttributeValue(el, "type"));
        bean.setLength(Integer.parseInt(getAttributeValue(el, "length", "0")));
        bean.setAlign(FieldAlignments.enumValueOf(getAttributeValue(el, "align", null)));
        bean.setPadWith(getAttributeValue(el, "pad-with", null));
        bean.setEmptyValue(getAttributeValue(el, "empty-value", null));

        return bean;
    }

    public MessageDefinition unmarshal(Node node) throws SAXException {
        if (node instanceof Document) {
            return unmarshal(((Document) node).getDocumentElement());
        }

        if (!(node instanceof Element)) {
            throw new IllegalArgumentException("node must be an element");
        }

        if (schema != null) {
            try {
                Validator validator = schema.newValidator();

                validator.setErrorHandler(this);
                validator.validate(new DOMSource(node));
            } catch (IOException e) {
                // should not be here
            }
        }

        Element el = (Element) node;
        ArrayList<FieldDefinition> fields = new ArrayList<FieldDefinition>();

        MessageDefinition bean = new MessageDefinition();
        bean.setFields(fields);

        NodeList nodes = el.getElementsByTagNameNS(NAMESPACE_URI, "iso-field");

        int len = nodes.getLength();
        for (int i = 0; i < len; ++i) {
            fields.add(unmarshalField((Element) nodes.item(i)));
        }

        return bean;
    }

    public MessageDefinition unmarshal(InputStream in) throws IOException, SAXException {
        Document doc = docBuilder.parse(in);
        doc.setStrictErrorChecking(true);
        doc.normalizeDocument();

        return unmarshal(doc.getDocumentElement());
    }

    public MessageDefinition unmarshal(URL url) throws URISyntaxException, IOException, SAXException {
        Document doc = docBuilder.parse(url.toURI().toString());
        doc.setStrictErrorChecking(true);
        doc.normalizeDocument();

        return unmarshal(doc.getDocumentElement());
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
}
