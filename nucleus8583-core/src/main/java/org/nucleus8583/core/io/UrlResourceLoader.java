package org.nucleus8583.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UrlResourceLoader implements ResourceLoader {

    private static final String LOCATION_PREFIX_FILE = "file:";

    public List<URL> getURLs(String location) {
        List<URL> resolved = new ArrayList<URL>();

        URL resolved1 = null;

        try {
            resolved1 = new URL(location);
        } catch (MalformedURLException ex) {
            if (location.startsWith(LOCATION_PREFIX_FILE)) {
                location = location.substring(LOCATION_PREFIX_FILE.length());
            }

            try {
                File f = new File(location);
                if (f.canRead()) {
                    resolved1 = new File(location).toURI().toURL();
                }
            } catch (MalformedURLException ex2) {
                throw new RuntimeException(new FileNotFoundException("unable to find " + location));
            }
        }

        if (resolved1 != null) {
            resolved.add(resolved1);
        }

        return resolved;
    }

    public URL getURL(String location) {
        URL resolved = null;

        try {
            resolved = new URL(location);
        } catch (MalformedURLException ex) {
            if (location.startsWith(LOCATION_PREFIX_FILE))
                location = location.substring(5);

            try {
                File f = new File(location);
                if (f.canRead()) {
                    resolved = new File(location).toURI().toURL();
                }
            } catch (MalformedURLException ex2) {
                throw new RuntimeException(new FileNotFoundException("unable to find " + location));
            }
        }

        return resolved;
    }

    public Class<?> loadClass(String className) {
        return null;
    }
}
