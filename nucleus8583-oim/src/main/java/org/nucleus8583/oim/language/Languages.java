package org.nucleus8583.oim.language;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.nucleus8583.oim.language.spi.CompiledExpression;
import org.nucleus8583.oim.language.spi.Language;
import org.nucleus8583.util.ResourceUtils;

public abstract class Languages {
	private static ConcurrentHashMap<String, Language> map = new ConcurrentHashMap<String, Language>();

	static {
		URL[] urls = ResourceUtils
				.getURLs("classpath:META-INF/nucleus8583/nucleus8583.languages");

		if (urls.length > 0) {
			for (int i = 0; i < urls.length; ++i) {
				addLanguages(urls[i]);
			}
		}
	}

	public static void addLanguages(URL url) {
		Reader reader = null;
		Properties prop = new Properties();

		try {
			reader = new InputStreamReader(url.openStream());
			prop.load(reader);
		} catch (Throwable t) {
			// do nothing
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Throwable t) {
					// do nothing
				}
			}
		}

		for (Map.Entry<Object, Object> entry : prop.entrySet()) {
			try {
				Class<?> cla = Class.forName((String) entry.getValue(), true,
						Thread.currentThread().getContextClassLoader());

				addLanguage((String) entry.getKey(),
						(Language) cla.newInstance());
			} catch (Throwable t) {
				// do nothing
			}
		}
	}

	public static void addLanguage(String name, Language lang) {
		if (map.putIfAbsent(name, lang) != null) {
			throw new IllegalArgumentException("language name " + name
					+ " already exists");
		}
	}

	public static void removeLanguage(String name) {
		map.remove(name);
	}

	public static CompiledExpression compile(String name, String expression)
			throws Exception {
		Language lang = map.get(name);
		if (lang == null) {
			throw new IllegalArgumentException("unsupported language " + name);
		}

		return lang.compile(expression);
	}
}
