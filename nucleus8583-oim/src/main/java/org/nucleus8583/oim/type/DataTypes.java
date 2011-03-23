package org.nucleus8583.oim.type;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.nucleus8583.oim.language.spi.CompiledExpression;
import org.nucleus8583.oim.type.spi.DataType;
import org.nucleus8583.oim.type.spi.DataTypeFactory;
import org.nucleus8583.util.ResourceUtils;

public abstract class DataTypes {
	private static ConcurrentHashMap<String, DataType> map = new ConcurrentHashMap<String, DataType>();

	static {
		URL[] urls = ResourceUtils
				.getURLs("classpath:META-INF/nucleus8583/nucleus8583.types");

		if (urls.length > 0) {
			for (int i = 0; i < urls.length; ++i) {
				addDataType(urls[i]);
			}
		}
	}

	private static String substring(String s, int start, char untilBefore) {
		int end = s.indexOf(untilBefore, start);
		if (end < 0) {
			return s.substring(start);
		}
		return s.substring(start, end);
	}

	private static String substring(String s, char startAfter) {
		int start = s.indexOf(startAfter);
		if (start < 0) {
			return "";
		}
		return s.substring(start + 1);
	}

	public static void addDataType(URL url) {
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
				Class<?> cla = Class.forName(substring(entryValue, 0, '?'),
						true, Thread.currentThread().getContextClassLoader());

				DataTypeFactory factory = (DataTypeFactory) cla.newInstance();

				addDataType((String) entry.getKey(),
						factory.createDataType(substring(entryValue, '?')));
			} catch (Throwable t) {
				// do nothing
			}
		}
	}

	public static void addDataType(String name, DataType lang) {
		if (map.putIfAbsent(name, lang) != null) {
			throw new IllegalArgumentException("language name " + name
					+ " already exists");
		}
	}

	public static void removeDataType(String name) {
		map.remove(name);
	}

	public static DataType compile(String name, String expression)
			throws Exception {
		DataType lang = map.get(name);
		if (lang == null) {
			throw new IllegalArgumentException("unsupported language " + name);
		}

		return lang.compile(expression);
	}
}
