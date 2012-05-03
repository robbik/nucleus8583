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

public class NamespaceHandlerResolver {

	private static final Logger log = LoggerFactory.getLogger(NamespaceHandlerResolver.class);
	
	private final ResourceLoader resourceLoader;
	
	private final String path;

	private final Map<String, NamespaceHandler> handlers;

	public NamespaceHandlerResolver(ResourceLoader resourceLoader, String path) {
		this.resourceLoader = resourceLoader;
		this.path = path;
		
		handlers = new HashMap<String, NamespaceHandler>();

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
			log.warning("unable to process namespace handle file (" + url + ")", t);
			return;
		}

		for (Map.Entry<Object, Object> entry : prop.entrySet()) {
			String namespaceURI = ((String) entry.getKey()).trim();
			String handlerClassName = ((String) entry.getValue()).trim();

			Class<?> handlerClass = resourceLoader.tryLoadClass(handlerClassName);
			if (handlerClass == null) {
				log.warning("unable to instantiate handler for namespace '"
						+ namespaceURI + "', unable to load class "
						+ handlerClassName);
			} else if (!NamespaceHandler.class.isAssignableFrom(handlerClass)) {
				log.warning("unable to instantiate handler for namespace '"
						+ namespaceURI + "', class " + handlerClassName
						+ " is not derived from class "
						+ NamespaceHandler.class.getName());
			} else {
				NamespaceHandler handler = null;

				try {
					handler = (NamespaceHandler) handlerClass.newInstance();
				} catch (InstantiationException e) {
					log.warning("unable to instantiate handler for namespace '"
							+ namespaceURI + "', unable to instantiate class "
							+ handlerClassName);
				} catch (IllegalAccessException e) {
					log.warning("unable to instantiate handler for namespace '"
							+ namespaceURI
							+ "', unable to access class / constructor for class "
							+ handlerClassName);
				}

				if (handler != null) {
					handler.init();

					handlers.put(namespaceURI, handler);
				}
			}
		}
	}

	private void initialize() {
		URL[] found = resourceLoader.getURLs(path);

		for (int i = 0, n = found.length; i < n; ++i) {
			process(found[i]);
		}
	}

	public NamespaceHandler resolve(String namespaceURI) {
		NamespaceHandler handler = handlers.get(namespaceURI);

		if (handler == null) {
			throw new IllegalArgumentException(
					"unable to resolve handler for namespace '" + namespaceURI + "'");
		}

		return handler;
	}
}
