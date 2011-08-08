package org.nucleus8583.core.util;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.nucleus8583.core.Iso8583Field;

public abstract class FieldTypes {
	private static final Map<String, Constructor<?>> providers;

	static {
		providers = new HashMap<String, Constructor<?>>();

		URL[] urls = ResourceUtils
				.getURLs("classpath:META-INF/nucleus8583/nucleus8583.types");

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

				if (Iso8583Field.class.isAssignableFrom(cla)) {
					providers.put(((String) entry.getKey()).toLowerCase(), cla
							.getConstructor(int.class, int.class, int.class,
									char.class, char.class, String.class));
				}
			} catch (Throwable t) {
				// do nothing
			}
		}
	}

	public static Constructor<?> getConstructor(String type) {
		return providers.get(type.toLowerCase());
	}
}
