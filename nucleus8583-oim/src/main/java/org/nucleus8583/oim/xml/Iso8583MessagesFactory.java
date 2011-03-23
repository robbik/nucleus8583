package org.nucleus8583.oim.xml;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.nucleus8583.oim.component.DataStructureComponent;
import org.nucleus8583.oim.component.Iso8583MessageComponent;
import org.nucleus8583.oim.converter.TypeConverter;
import org.nucleus8583.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class Iso8583MessagesFactory {
	private final Map<String, TypeConverter> types;

	private final Map<String, DataStructureComponent> dataStructures;

	private final Map<String, Iso8583MessageComponent> messages;

	public Iso8583MessagesFactory(String... locations) throws IOException {
		types = new HashMap<String, TypeConverter>();
		dataStructures = new HashMap<String, DataStructureComponent>();
		messages = new HashMap<String, Iso8583MessageComponent>();

		int count = locations.length;

		for (int i = 0; i < count; ++i) {
			readType(locations[i]);
		}
		for (int i = 0; i < count; ++i) {
			readDataStructure(locations[i]);
		}
		for (int i = 0; i < count; ++i) {
			readEntity(locations[i]);
		}
	}

	private Document read(String location) throws IOException {
		Document doc;
		try {
			doc = DocumentBuilderFactory
					.newInstance()
					.newDocumentBuilder()
					.parse(ResourceUtils.getURL(location).toURI()
							.toASCIIString());
		} catch (SAXException e) {
			throw new RuntimeException("unable to parse configuration file", e);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("unable to parse configuration file", e);
		} catch (URISyntaxException e) {
			throw new RuntimeException("unable to parse configuration file", e);
		}

		return doc;
	}

	private void readType(String location) throws IOException {
		Element root = read(location).getDocumentElement();

		NodeList children = root.getChildNodes();
		int count = children.getLength();

		for (int i = 0; i < count; ++i) {
			Node childNode = children.item(i);
			if (!Element.class.isInstance(childNode)) {
				continue;
			}

			Element childEl = (Element) childNode;

			if ("type".equals(childEl.getTagName())) {
				TypeConverter converter = TypeConverterFactory.parse(childEl);
				types.put(converter.getName(), converter);
			}
		}
	}

	private void readDataStructure(String location) throws IOException {
		Element root = read(location).getDocumentElement();

		NodeList children = root.getChildNodes();
		int count = children.getLength();

		for (int i = 0; i < count; ++i) {
			Node childNode = children.item(i);
			if (!Element.class.isInstance(childNode)) {
				continue;
			}

			Element el = (Element) childNode;

			if ("data-structure".equals(el.getTagName())) {
				DataStructureComponent comp = DataStructureComponentFactory
						.parse(this, null, false, el);

				dataStructures.put(comp.getName(), comp);
			}
		}
	}

	private void readEntity(String location) throws IOException {
		Element root = read(location).getDocumentElement();

		NodeList list = root.getChildNodes();
		int count = list.getLength();

		for (int i = 0; i < count; ++i) {
			Object node = list.item(i);
			if (!Element.class.isInstance(node)) {
				continue;
			}

			Element el = (Element) node;
			String tagName = el.getTagName();

			if ("message".equals(tagName)) {
				Iso8583MessageComponent comp = Iso8583MessageComponentFactory
						.parse(this, el);
				messages.put(comp.getName(), comp);
			}
		}
	}

	public Map<String, Iso8583MessageComponent> getMessages() {
		return messages;
	}

	public Iso8583MessageComponent findMessage(String name) {
		return messages.containsKey(name) ? messages.get(name) : null;
	}

	public DataStructureComponent findDataStructure(String name) {
		return dataStructures.containsKey(name) ? dataStructures.get(name)
				: null;
	}

	public TypeConverter findTypeConverter(String name) {
		return types.containsKey(name) ? types.get(name) : null;
	}
}
