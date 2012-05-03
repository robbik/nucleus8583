package rk.commons.ioc.factory.xml;

import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import rk.commons.loader.ResourceLoader;
import rk.commons.logging.Logger;
import rk.commons.logging.LoggerFactory;

public class NamespaceSchemaResolver {

	private static final Logger log = LoggerFactory.getLogger(NamespaceSchemaResolver.class);
	
	private final ResourceLoader resourceLoader;

	private final String path;

	private final Map<String, URL> schemas;

	public NamespaceSchemaResolver(ResourceLoader resourceLoader, String path) {
		this.resourceLoader = resourceLoader;
		this.path = path;

		schemas = new HashMap<String, URL>();

		AccessController.doPrivileged(new PrivilegedAction<Void>() {

			public Void run() {
				initialize();
				return null;
			}
		});
	}

	private void process(URL url) {
		Properties prop = new Properties();

		try {
			prop.load(url.openStream());
		} catch (Throwable t) {
			log.warning("unable to process namespace schema file (" + url + ")", t);
			return;
		}
		
		for (Map.Entry<Object, Object> entry : prop.entrySet()) {
			String namespaceURI = ((String) entry.getKey()).trim();
			String schemaFile = ((String) entry.getValue()).trim();

			URL schemaURL = resourceLoader.getURL(schemaFile);

			if (schemaURL == null) {
				log.warning("unable to instantiate schema for namespace '"
						+ namespaceURI + "', unable to load file " + schemaFile);
			} else {
				schemas.put(namespaceURI, schemaURL);
			}
		}
	}

	private void initialize() {
		URL[] found = resourceLoader.getURLs(path);
		
		for (int i = 0, n = found.length; i < n; ++i) {
			process(found[i]);
		}
	}

	public URL tryResolve(String namespaceURI) {
		return schemas.get(namespaceURI);
	}
}
