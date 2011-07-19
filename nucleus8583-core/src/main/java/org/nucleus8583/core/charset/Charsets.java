package org.nucleus8583.core.charset;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.nucleus8583.core.util.ResourceUtils;

public abstract class Charsets {
	private static final Map<String, CharsetProvider> providers;

	static {
		providers = new HashMap<String, CharsetProvider>();

		URL[] urls = ResourceUtils
				.getURLs("classpath:META-INF/nucleus8583/nucleus8583.charsets");

		for (int i = 0; i < urls.length; ++i) {
			load(urls[i]);
		}
	}

	private static void load(URL url) {
		InputStream in = null;
		Properties prop = new Properties();

		try {
			in = url.openStream();
			prop.load(in);
		} catch (Throwable t) {
			// do nothing
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Throwable t) {
					// do nothing
				}
			}
		}

		for (Map.Entry<Object, Object> entry : prop.entrySet()) {
			try {
				Class<?> cla = Class.forName((String) entry.getValue(), true,
						Thread.currentThread().getContextClassLoader());

				providers.put(((String) entry.getKey()).toUpperCase(),
						(CharsetProvider) cla.newInstance());
			} catch (Throwable t) {
				// do nothing
			}
		}
	}

	public static CharsetProvider getProvider(String encoding) {
		return providers.get(encoding.toUpperCase());
	}
}
