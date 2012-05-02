package rk.commons.loader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassResourceLoader implements ResourceLoaderProvider {

	private static final String LOCATION_PREFIX_CLASSPATH = "classpath:";

	private void add(List<URL> resolved, Set<String> doubleChecker, Enumeration<URL> en) {
		if (en != null) {
			while (en.hasMoreElements()) {
				URL el = en.nextElement();
				String strEl = el.toString();

				if (!doubleChecker.contains(strEl)) {
					resolved.add(el);
					doubleChecker.add(strEl);
				}
			}
		}
	}

	private void add(List<URL> resolved, Set<String> doubleChecker, ClassLoader cl, String location) throws IOException {
		add(resolved, doubleChecker, cl.getResources(location));
	}
	
	public boolean canHandle(String location) {
		return location.startsWith(LOCATION_PREFIX_CLASSPATH);
	}

	public List<URL> getURLs(String location) {
		List<URL> resolved = new ArrayList<URL>();
		Set<String> doubleChecker = new HashSet<String>();

		if (location.startsWith(LOCATION_PREFIX_CLASSPATH)) {
			location = location.substring(LOCATION_PREFIX_CLASSPATH.length());

			try {
				add(resolved, doubleChecker, Thread.currentThread().getContextClassLoader(), location);
			} catch (Throwable t) {
				// do nothing
			}

			try {
				add(resolved, doubleChecker, ClassResourceLoader.class.getClassLoader(), location);
			} catch (Throwable t) {
				// do nothing
			}

			try {
				add(resolved, doubleChecker, ClassLoader.getSystemClassLoader(), location);
			} catch (Throwable t) {
				// do nothing
			}

			try {
				add(resolved, doubleChecker, ClassLoader.getSystemResources(location));
			} catch (Throwable t) {
				// do nothing
			}
		}

		return resolved;
	}

	public URL getURL(String location) {
		URL resolved = null;

		if (location.startsWith(LOCATION_PREFIX_CLASSPATH)) {
			location = location.substring(LOCATION_PREFIX_CLASSPATH.length());

			try {
				resolved = Thread.currentThread().getContextClassLoader().getResource(location);
			} catch (Throwable t) {
				// do nothing
			}

			if (resolved == null) {
				try {
					resolved = ClassResourceLoader.class.getResource(location);
				} catch (Throwable t) {
					// do nothing
				}
			}

			if (resolved == null) {
				try {
					resolved = ClassLoader.getSystemClassLoader().getResource(location);
				} catch (Throwable t) {
					// do nothing
				}
			}

			if (resolved == null) {
				try {
					resolved = ClassLoader.getSystemResource(location);
				} catch (Throwable t) {
					// do nothing
				}
			}
		}

		return resolved;
	}

	public Class<?> loadClass(String className) {
		Class<?> found = null;

		try {
			found = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
		} catch (Throwable t) {
			// do nothing
		}

		if (found == null) {
			try {
				found = Class.forName(className, true, ClassResourceLoader.class.getClassLoader());
			} catch (Throwable t) {
				// do nothing
			}
		}

		if (found == null) {
			try {
				found = Class.forName(className, true, ClassLoader.getSystemClassLoader());
			} catch (Throwable t) {
				// do nothing
			}
		}

		return found;
	}
}
