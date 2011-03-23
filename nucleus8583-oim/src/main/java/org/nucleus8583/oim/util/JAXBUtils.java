package org.nucleus8583.oim.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.cheetah.core.ServiceContextDefinition;
import org.cheetah.core.ServiceDefinition;

public abstract class JAXBUtils {
	private static JAXBContext ctx;

	private static Set<Class<?>> classes;

	static {
		classes = Collections.synchronizedSet(new HashSet<Class<?>>());

		classes.add(ServiceContextDefinition.class);
		classes.add(ServiceDefinition.class);

		URL[] urls = ResourceUtils
				.getURLs("classpath:META-INF/cheetah/cheetah.actions");

		if (urls.length > 0) {
			for (int i = 0; i < urls.length; ++i) {
				addClasses(urls[i]);
			}
		}
	}

	public static void addClass(Class<?> _class) {
		classes.add(_class);
	}

	public static void addClasses(URL url) {
		BufferedReader reader = null;

		String line;
		ClassLoader cl = Thread.currentThread().getContextClassLoader();

		try {
			reader = new BufferedReader(new InputStreamReader(url.openStream()));

			while ((line = reader.readLine()) != null) {
				line = line.trim();

				if ((line.length() == 0) || line.startsWith(";")) {
					continue;
				}

				try {
					classes.add(Class.forName(line, true, cl));
				} catch (Throwable t) {
					// do nothing
				}
			}
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
	}

	private static synchronized void createContext() {
		if (ctx == null) {
			try {
				ctx = JAXBContext.newInstance(classes.toArray(new Class<?>[0]));
			} catch (JAXBException e) {
				StringWriter sw = new StringWriter();

				PrintWriter w = new PrintWriter(sw);
				e.printStackTrace(w);

				w.flush();

				throw new InternalError(sw.toString());
			}
		}
	}

	public static Object unmarshal(URL url) throws JAXBException {
		createContext();

		return ctx.createUnmarshaller().unmarshal(url);
	}
}
