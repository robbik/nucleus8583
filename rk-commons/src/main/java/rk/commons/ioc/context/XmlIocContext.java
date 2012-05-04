package rk.commons.ioc.context;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import rk.commons.ioc.factory.IocObjectFactory;
import rk.commons.ioc.factory.ObjectNotFoundException;
import rk.commons.ioc.factory.config.ObjectDefinition;
import rk.commons.ioc.factory.support.ObjectDefinitionRegistry;
import rk.commons.ioc.factory.xml.NamespaceHandlerResolver;
import rk.commons.ioc.factory.xml.NamespaceSchemaResolver;
import rk.commons.ioc.factory.xml.ObjectDefinitionParserDelegate;
import rk.commons.ioc.factory.xml.ObjectDefinitionReader;
import rk.commons.loader.ResourceLoader;
import rk.commons.logging.Logger;
import rk.commons.logging.LoggerFactory;

public class XmlIocContext {

	private static final Logger log = LoggerFactory.getLogger(XmlIocContext.class);
	
	private ResourceLoader resourceLoader;

	private NamespaceHandlerResolver handlerResolver;

	private NamespaceSchemaResolver schemaResolver;

	protected IocObjectFactory iocObjectFactory;

	protected ObjectDefinitionRegistry objectDefinitionRegistry;
	
	protected String xmlDefaultNamespace;

	private String[] locations;

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	public void setNamespaceHandlerPath(String path) {
		handlerResolver = new NamespaceHandlerResolver(resourceLoader, path);
	}
	
	public void setNamespaceSchemaPath(String path) {
		schemaResolver = new NamespaceSchemaResolver(resourceLoader, path);
	}
	
	public void setIocObjectFactory(IocObjectFactory iocObjectFactory) {
		this.iocObjectFactory = iocObjectFactory;  
	}
	
	public void setObjectDefinitionRegistry(ObjectDefinitionRegistry objectDefinitionRegistry) {
		this.objectDefinitionRegistry = objectDefinitionRegistry;
	}
	
	public void setXmlDefaultNamespace(String xmlDefaultNamespace) {
		this.xmlDefaultNamespace = xmlDefaultNamespace;
	}
	
	public void setLocations(String... locations) {
		this.locations = locations;
	}

	protected void loadObjectDefinitions(URL url, Set<URL> importedURLs) {
		if (!importedURLs.add(url)) {
			// already loaded, skip it!
			return;
		}
		
		ObjectDefinitionParserDelegate delegate = new ObjectDefinitionParserDelegate(resourceLoader);
		delegate.setObjectDefinitionRegistry(objectDefinitionRegistry);
		delegate.setNamespaceHandlerResolver(handlerResolver);
		delegate.setPackageName(null);

		ObjectDefinitionReader reader = new ObjectDefinitionReader(url, schemaResolver);

		Element rootElement = null;

		try {
			rootElement = reader.parse();
		} catch (SAXException e) {
			if (e instanceof SAXParseException) {
				SAXParseException pex = (SAXParseException) e;

				log.error(
						pex.getMessage() + " (" + url + ":"
								+ pex.getLineNumber() + ":"
								+ pex.getColumnNumber() + ")", e.getCause());
			} else {
				log.error(e.getMessage() + "(" + url + ")", e);
			}
		} catch (IOException e) {
			log.error("unable to parse " + url, e);
		}

		if (rootElement == null) {
			return;
		}

		NodeList nodes = rootElement.getChildNodes();

		for (int i = 0, n = nodes.getLength(); i < n; ++i) {
			Node node = nodes.item(i);

			if (node instanceof Element) {
				Element element = (Element) node;

				if (xmlDefaultNamespace.equals(node.getNamespaceURI()) && "import".equals(node.getLocalName())) {
					URL[] refURLs = resourceLoader.getURLs(element.getAttribute("url"));

					for (int j = 0, m = refURLs.length; j < m; ++j) {
						loadObjectDefinitions(refURLs[i], importedURLs);
					}
				} else {
					delegate.parse((Element) node);
				}
			}
		}
	}

	public void refresh(boolean lazy) {
		Set<URL> importedURLs = new HashSet<URL>();
		
		for (int i = 0, n = locations.length; i < n; ++i) {
			loadObjectDefinitions(resourceLoader.getURL(locations[i]), importedURLs);
		}

		if (!lazy) {
			Set<String> objectQNames = objectDefinitionRegistry.getObjectQNames();
	
			for (String objectQName : objectQNames) {
				ObjectDefinition def;
				
				try {
					def = objectDefinitionRegistry.getObjectDefinition(objectQName);
				} catch (ObjectNotFoundException e) {
					continue;
				}
				
				iocObjectFactory.createObject(def);
			}
		}
	}

	public void refresh(String location) {
		loadObjectDefinitions(resourceLoader.getURL(location), new HashSet<URL>());
	}
}
