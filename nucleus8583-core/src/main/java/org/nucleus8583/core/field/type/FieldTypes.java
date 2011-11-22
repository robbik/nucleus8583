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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.nucleus8583.core.logging.Logger;
import org.nucleus8583.core.logging.LoggerFactory;
import org.nucleus8583.core.util.ResourceUtils;
import org.nucleus8583.core.xml.FieldAlignments;
import org.nucleus8583.core.xml.FieldDefinition;

public abstract class FieldTypes {

    private static final Logger log = LoggerFactory.getLogger(FieldTypes.class.getName());

	private static final class Entry {

		public Class<?> clazz;

		public FieldAlignments align;

		public String padWith;

		public String emptyValue;
	}

	private static final Map<String, Entry> types;

	private static final ReadLock slock;

	private static final WriteLock xlock;

	private static final AtomicBoolean initialized;

	static {
	    types = new HashMap<String, Entry>();

	    ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
	    slock = lock.readLock();
	    xlock = lock.writeLock();

	    initialized = new AtomicBoolean(false);
	}

	private static Entry createEntry(List<String> lines) {
		int size = lines.size();
		if (size == 0) {
			return null;
		}

		Entry entry = new Entry();

		try {
			entry.clazz = ResourceUtils.loadClass(lines.get(0));
		} catch (ClassNotFoundException ex) {
		    log.warning("class " + lines.get(0) + " cannot be found.");
		    return null;
		} catch (Throwable t) {
		    log.warning("unable to load class " + lines.get(0) + ".", t);
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

    public static void initialize() {
        if (initialized.compareAndSet(false, true)) {
            try {
                refresh();
            } catch (RuntimeException ex) {
                initialized.set(false);
                throw ex;
            }
        }
    }

    public static void refresh() {
        URL[] urls = ResourceUtils.getURLs("classpath:META-INF/nucleus8583/nucleus8583.types");

        xlock.lock();
        try {
            for (int i = 0; i < urls.length; ++i) {
                load(urls[i]);
            }
        } finally {
            xlock.unlock();
        }
    }

	public static FieldType getType(FieldDefinition def) {
	    Entry entry;

	    slock.lock();
	    try {
	        entry = types.get(def.getType().toUpperCase());
	    } finally {
	        slock.unlock();
	    }

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
