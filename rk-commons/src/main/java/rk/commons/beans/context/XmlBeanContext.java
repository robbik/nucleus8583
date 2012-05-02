package rk.commons.beans.context;

import java.io.IOException;
import java.net.URL;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import rk.commons.beans.factory.BeanFactory;
import rk.commons.beans.factory.support.BeanDefinitionRegistry;
import rk.commons.beans.factory.xml.BeanDefinitionParserDelegate;
import rk.commons.beans.factory.xml.BeanDefinitionReader;
import rk.commons.beans.factory.xml.NamespaceHandlerResolver;
import rk.commons.beans.factory.xml.NamespaceSchemaResolver;
import rk.commons.loader.ResourceLoader;
import rk.commons.logging.Logger;
import rk.commons.logging.LoggerFactory;

public class XmlBeanContext {

	private static final Logger log = LoggerFactory.getLogger(XmlBeanContext.class);
	
	private ResourceLoader resourceLoader;

	private NamespaceHandlerResolver handlerResolver;

	private NamespaceSchemaResolver schemaResolver;

	protected BeanFactory beanFactory;

	protected BeanDefinitionRegistry beanDefinitionRegistry;
	
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
	
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;  
	}
	
	public void setBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) {
		this.beanDefinitionRegistry = beanDefinitionRegistry;
	}
	
	public void setXmlDefaultNamespace(String xmlDefaultNamespace) {
		this.xmlDefaultNamespace = xmlDefaultNamespace;
	}
	
	public void setLocations(String... locations) {
		this.locations = locations;
	}

	protected void loadBeanDefinitions(final URL url) {
		BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate(resourceLoader);
		delegate.setBeanDefinitionRegistry(beanDefinitionRegistry);
		delegate.setNamespaceHandlerResolver(handlerResolver);
		delegate.setPackageName(null);

		BeanDefinitionReader reader = new BeanDefinitionReader(url, schemaResolver);

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
						loadBeanDefinitions(refURLs[i]);
					}
				} else {
					delegate.parse((Element) node);
				}
			}
		}
	}

	public void refresh(boolean lazy) {
		for (int i = 0, n = locations.length; i < n; ++i) {
			loadBeanDefinitions(resourceLoader.getURL(locations[i]));
		}

		if (!lazy) {
			String[] beanQNames = beanDefinitionRegistry.getBeanQNames();
	
			for (int i = 0; i < beanQNames.length; ++i) {
				beanFactory.createBean(beanDefinitionRegistry.getBeanDefinition(beanQNames[i]));
			}
		}
	}

	public void refresh(String location) {
		loadBeanDefinitions(resourceLoader.getURL(location));
	}
}
