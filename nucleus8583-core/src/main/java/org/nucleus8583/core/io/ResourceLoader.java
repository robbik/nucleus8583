package org.nucleus8583.core.io;

import java.net.URL;
import java.util.List;

public interface ResourceLoader {

    List<URL> getURLs(String location);

    URL getURL(String location);

    Class<?> loadClass(String className);
}
