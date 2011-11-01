package org.nucleus8583.core.io;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.nucleus8583.core.util.ResourceUtils;

public class ClassResourceLoader implements ResourceLoader {

    private static final String LOCATION_PREFIX_CLASSPATH = "classpath:";

    private static void add(List<URL> resolved, Set<String> doubleChecker, Enumeration<URL> en) {
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

    public List<URL> getURLs(String location) {
        List<URL> resolved = new ArrayList<URL>();
        Set<String> doubleChecker = new HashSet<String>();

        if (location.startsWith(LOCATION_PREFIX_CLASSPATH)) {
            location = location.substring(LOCATION_PREFIX_CLASSPATH.length());

            try {
                add(resolved, doubleChecker, Thread.currentThread().getContextClassLoader().getResources(location));
            } catch (Throwable t) {
                // do nothing
            }

            try {
                add(resolved, doubleChecker, ResourceUtils.class.getClassLoader().getResources(location));
            } catch (Throwable t) {
                // do nothing
            }

            try {
                add(resolved, doubleChecker, ClassLoader.getSystemClassLoader().getResources(location));
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
                resolved = Thread.currentThread().getContextClassLoader()
                        .getResource(location);
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
                    resolved = ClassLoader.getSystemClassLoader().getResource(
                            location);
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
