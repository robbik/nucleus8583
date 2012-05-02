package rk.commons.loader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class OsgiBundleResourceLoader implements ResourceLoaderProvider {

    private static final String LOCATION_PREFIX_CLASSPATH = "classpath:";

    private Bundle bundle;

    public OsgiBundleResourceLoader(Bundle bundle) {
        this.bundle = bundle;

        if (bundle == null) {
            throw new NullPointerException("bundle");
        }
    }

    public OsgiBundleResourceLoader() {
        this(FrameworkUtil.getBundle(OsgiBundleResourceLoader.class));
    }

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

	public boolean canHandle(String location) {
		return location.startsWith(LOCATION_PREFIX_CLASSPATH);
	}

    @SuppressWarnings("unchecked")
    public List<URL> getURLs(String location) {
        List<URL> resolved = new ArrayList<URL>();
        Set<String> doubleChecker = new HashSet<String>();

        if (location.startsWith(LOCATION_PREFIX_CLASSPATH)) {
            location = location.substring(LOCATION_PREFIX_CLASSPATH.length());

            try {
                add(resolved, doubleChecker, bundle.getResources(location));
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
                resolved = bundle.getResource(location);
            } catch (Throwable t) {
                // do nothing
            }
        }

        return resolved;
    }

    public Class<?> loadClass(String className) {
        try {
            return bundle.loadClass(className);
        } catch (Throwable t) {
            return null;
        }
    }
}
