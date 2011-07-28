package org.nucleus8583.oim.type;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.nucleus8583.core.util.ResourceUtils;
import org.nucleus8583.oim.util.StringUtils;

public abstract class DataTypes {
	private static Map<String, DataType> map = new HashMap<String, DataType>();

	static {
		URL[] urls = ResourceUtils.getURLs("classpath:META-INF/nucleus8583/nucleus8583-oim.types");

		if (urls.length > 0) {
			for (int i = 0; i < urls.length; ++i) {
				add(urls[i]);
			}
		}
	}

	public static void add(URL url) {
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
			String entryValue = (String) entry.getValue();

			try {
				Class<?> cla = Class.forName(StringUtils.substring(entryValue, 0, '?'),
						true, Thread.currentThread().getContextClassLoader());

				add((String) entry.getKey(),
						((DataTypeFactory) cla.newInstance()).createType(
								StringUtils.substring(entryValue, '?')));
			} catch (Throwable t) {
				// do nothing
			}
		}
	}

	private static void add(String name, DataType type) {
		if (map.containsKey(name)) {
			throw new IllegalArgumentException("type " + name + " already exists");
		}
		
		map.put(name, type);
	}
	
	public static DataType get(String name) {
		return map.get(name);
	}
}
