package org.nucleus8583.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ResourceUtils {
	private static final String LOCATION_PREFIX_FILE = "file:";
	private static final String LOCATION_PREFIX_CLASSPATH = "classpath:";

	private static void addResources(List<URL> resolved,
			Set<String> doubleChecker, ClassLoader cl, String name) {
		Enumeration<URL> en;

		try {
			if (cl == null) {
				en = ClassLoader.getSystemResources(name);
			} else {
				en = cl.getResources(name);
			}
		} catch (IOException e) {
			en = null;
		}

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

	public static URL[] getURLs(String location) {
		List<URL> resolved = new ArrayList<URL>();
		Set<String> doubleChecker = new HashSet<String>();

		if (location.startsWith(LOCATION_PREFIX_CLASSPATH)) {
			location = location.substring(10);

			addResources(resolved, doubleChecker, Thread.currentThread()
					.getContextClassLoader(), location);
			addResources(resolved, doubleChecker,
					ResourceUtils.class.getClassLoader(), location);
			addResources(resolved, doubleChecker,
					ClassLoader.getSystemClassLoader(), location);
			addResources(resolved, doubleChecker, null, location);
		} else {
			URL resolved1;

			try {
				resolved1 = new URL(location);
			} catch (MalformedURLException ex) {
				if (location.startsWith(LOCATION_PREFIX_FILE))
					location = location.substring(5);

				try {
					resolved1 = new File(location).toURI().toURL();
				} catch (MalformedURLException ex2) {
					throw new RuntimeException(new FileNotFoundException(
							"unable to find " + location));
				}
			}

			if (resolved1 != null) {
				resolved.add(resolved1);
			}
		}

		return resolved.toArray(new URL[0]);
	}

	public static URL getURL(String location) {
		URL resolved;

		if (location.startsWith(LOCATION_PREFIX_CLASSPATH)) {
			location = location.substring(10);

			resolved = Thread.currentThread().getContextClassLoader()
					.getResource(location);
			if (resolved == null) {
				resolved = ResourceUtils.class.getResource(location);
			}
			if (resolved == null) {
				resolved = ClassLoader.getSystemClassLoader().getResource(
						location);
			}
			if (resolved == null) {
				resolved = ClassLoader.getSystemResource(location);
			}
		} else {
			try {
				resolved = new URL(location);
			} catch (MalformedURLException ex) {
				if (location.startsWith(LOCATION_PREFIX_FILE))
					location = location.substring(5);

				try {
					resolved = new File(location).toURI().toURL();
				} catch (MalformedURLException ex2) {
					throw new RuntimeException(new FileNotFoundException(
							"unable to find " + location));
				}
			}
		}

		return resolved;
	}
}
