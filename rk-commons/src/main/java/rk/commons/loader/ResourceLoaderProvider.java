package rk.commons.loader;

import java.net.URL;
import java.util.List;

public interface ResourceLoaderProvider {
	
	boolean canHandle(String location);

    List<URL> getURLs(String location);

    URL getURL(String location);

    Class<?> loadClass(String className);
}
