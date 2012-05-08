package org.nucleus8583.oim.expression;

import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.nucleus8583.oim.field.spi.Expression;

import rk.commons.loader.ResourceLoader;
import rk.commons.logging.Logger;
import rk.commons.logging.LoggerFactory;

public class ExpressionHandlerResolver {

	private static final String PATH_HANDLER = "classpath:META-INF/nucleus8583/nucleus8583-oim.languages";

	private static final Logger log = LoggerFactory.getLogger(ExpressionHandlerResolver.class);

	private final Map<String, ExpressionHandler> handlers;
	
	private final ResourceLoader resourceLoader;

	public ExpressionHandlerResolver(ResourceLoader resourceLoader) {
		handlers = new HashMap<String, ExpressionHandler>();
		
		this.resourceLoader = resourceLoader;
		
		AccessController.doPrivileged(new PrivilegedAction<Void>() {

			public Void run() {
				initialize();
				return null;
			}
		});
	}

	private void initialize() {
		URL[] found = resourceLoader.getURLs(PATH_HANDLER);

		for (int i = 0, n = found.length; i < n; ++i) {
			process(found[i]);
		}
	}

	private void process(URL url) {
		Properties prop = new Properties();

		try {
			prop.load(url.openStream());
		} catch (Throwable t) {
			log.warning("unable to process language handler file (" + url + ")", t);
			return;
		}

		for (Map.Entry<Object, Object> entry : prop.entrySet()) {
			String language = ((String) entry.getKey()).trim();
			String handlerClassName = ((String) entry.getValue()).trim();

			Class<?> handlerClass = resourceLoader.tryLoadClass(handlerClassName);
			if (handlerClass == null) {
				log.warning("unable to instantiate handler for language '"
						+ language + "', unable to load class "
						+ handlerClassName);
			} else if (!ExpressionHandler.class.isAssignableFrom(handlerClass)) {
				log.warning("unable to instantiate handler for language '"
						+ language + "', class " + handlerClassName
						+ " is not derived from class "
						+ ExpressionHandler.class.getName());
			} else {
				ExpressionHandler handler = null;

				try {
					handler = (ExpressionHandler) handlerClass.newInstance();
				} catch (InstantiationException e) {
					log.warning("unable to instantiate handler for language '"
							+ language + "', unable to instantiate class "
							+ handlerClassName);
				} catch (IllegalAccessException e) {
					log.warning("unable to instantiate handler for language '"
							+ language
							+ "', unable to access class / constructor for class "
							+ handlerClassName);
				}

				if (handler != null) {
					handlers.put(language, handler);
				}
			}
		}
	}

	public Expression parse(String language, String expression)
			throws Exception {
		if (!handlers.containsKey(language)) {
			throw new IllegalArgumentException("unsupported language " + language);
		}

		return handlers.get(language).parse(expression);
	}
}
