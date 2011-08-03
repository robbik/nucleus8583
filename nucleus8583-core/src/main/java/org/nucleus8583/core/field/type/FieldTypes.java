package org.nucleus8583.core.field.type;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.nucleus8583.core.util.ResourceUtils;
import org.nucleus8583.core.xml.FieldAlignments;
import org.nucleus8583.core.xml.FieldDefinition;

public abstract class FieldTypes {

	private static final class Entry {

		public Class<?> clazz;

		public FieldAlignments align;

		public String padWith;

		public String emptyValue;
	}

	private static final Map<String, Entry> types;

	static {
		types = new HashMap<String, Entry>();

		URL[] urls = ResourceUtils.getURLs("classpath:META-INF/nucleus8583/nucleus8583.types");

		for (int i = 0; i < urls.length; ++i) {
			load(urls[i]);
		}
	}

	private static Entry createEntry(List<String> lines) {
		int size = lines.size();
		if (size == 0) {
			return null;
		}

		Entry entry = new Entry();

		try {
			entry.clazz = Class.forName(lines.get(0), true, Thread.currentThread().getContextClassLoader());
		} catch (Throwable t) {
			return null;
		}

		for (int i = 1; i < size; ++i) {
			String line = lines.get(i);
			int eqidx = line.indexOf('=');

			if (eqidx >= 0) {
				String name = line.substring(0, eqidx);
				String value = line.substring(eqidx + 1);

				if ("align".equals(name)) {
					entry.align = FieldAlignments.enumValueOf(value);
				} else if ("pad-with".equals(name)) {
					entry.padWith = value;
				} else if ("empty-value".equals(name)) {
					entry.emptyValue = value;
				}
			}
		}

		return entry;
	}

	private static Entry createEntry(String rawdef) {
		boolean escaped = false;

		StringBuilder sb = new StringBuilder();

		char[] craw = rawdef.toCharArray();
		int size = craw.length;

		ArrayList<String> lines = new ArrayList<String>();

		for (int i = 0; i < size; ++i) {
			char cc = craw[i];

			if ((cc == '\\') && !escaped) {
				escaped = true;
			} else {
				if ((cc == ',') && !escaped) {
					if (sb.length() > 0) {
						lines.add(sb.toString());
						sb.setLength(0);
					}
				} else {
					sb.append(cc);
				}

				escaped = false;
			}
		}

		if (escaped) {
			sb.append('\\');
		}
		if (sb.length() > 0) {
			lines.add(sb.toString());
		}

		return createEntry(lines);
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
			Entry obj = createEntry((String) entry.getValue());

			if (obj != null) {
				types.put(((String) entry.getKey()).toUpperCase(), obj);
			}
		}
	}

	public static FieldType getType(FieldDefinition def) {
		Entry entry = types.get(def.getType().toUpperCase());
		if (entry == null) {
			throw new RuntimeException("an error occured while retrieving type " + def.getType() + ", type not found.");
		}

		try {
			Constructor<?> ctor = entry.clazz.getConstructor(FieldDefinition.class, FieldAlignments.class, String.class, String.class);

			return (FieldType) ctor.newInstance(def, entry.align, entry.padWith, entry.emptyValue);
		} catch (InvocationTargetException ex) {
			Throwable t = ex.getCause();

			if (t instanceof RuntimeException) {
				throw (RuntimeException) t;
			}
			throw new RuntimeException("an error occured while retrieving type " + def.getType() + ", unable to instantiate class " + entry.clazz + ".", t);
		} catch (RuntimeException ex) {
			throw ex;
		} catch (Throwable t) {
			throw new RuntimeException("an error occured while retrieving type " + def.getType() + ", unable to instantiate class " + entry.clazz + ".", t);
		}
	}
}
