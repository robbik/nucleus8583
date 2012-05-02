package rk.commons.loader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public final class ResourceLoader {

//	private static final Logger log = LoggerFactory.getLogger(ResourceLoader.class);

	private final Object monitor;

	private volatile boolean initialized;

	private volatile ResourceLoaderProvider[] providers;

	public ResourceLoader() {
		providers = new ResourceLoaderProvider[] {
				new ClassResourceLoader(),
				new UrlResourceLoader() };
		
		monitor = new Object();
		
		initialized = false;
		
		initialize();
	}

	public void initialize() {
		synchronized (monitor) {
			if (!initialized) {
				if (OsgiBundleResourceLoaderFactory.osgiDetected()) {
					ResourceLoaderProvider osgi = OsgiBundleResourceLoaderFactory.create();
					if (osgi != null) {
						addResourceLoaderProvider(0, osgi);
					}
//	
//					log.info("OSGi environment detected");
//				} else {
//					log.info("OSGi environment not detected");
				}
				
				initialized = true;
			}
		}
	}

	public void addResourceLoaderProvider(ResourceLoaderProvider provider) {
		synchronized (monitor) {
			List<ResourceLoaderProvider> list = new ArrayList<ResourceLoaderProvider>(
					Arrays.asList(providers));
			list.add(provider);

			providers = (ResourceLoaderProvider[]) list.toArray();
		}
	}

	public void addResourceLoaderProvider(int index, ResourceLoaderProvider provider) {
		synchronized (monitor) {
			List<ResourceLoaderProvider> list = new ArrayList<ResourceLoaderProvider>(
					Arrays.asList(providers));
			list.add(index, provider);

			providers = (ResourceLoaderProvider[]) list.toArray();
		}
	}

	public URL[] getURLs(String location) {
		HashSet<URL> urls = new HashSet<URL>();

		synchronized (providers) {
			for (int i = 0; i < providers.length; ++i) {
				urls.addAll(providers[i].getURLs(location));
			}
		}

		return urls.toArray(new URL[0]);
	}

	public URL getURL(String location) {
		URL found = null;

		synchronized (providers) {
			for (int i = 0; i < providers.length; ++i) {
				found = providers[i].getURL(location);

				if (found != null) {
					break;
				}
			}
		}

		return found;
	}

	public Class<?> loadClass(String className) throws ClassNotFoundException {
		Class<?> found = tryLoadClass(className);

		if (found == null) {
			throw new ClassNotFoundException(className);
		}

		return found;
	}

	public Class<?> tryLoadClass(String className) {
		Class<?> found = null;

		synchronized (providers) {
			for (int i = 0; i < providers.length; ++i) {
				found = providers[i].loadClass(className);

				if (found != null) {
					break;
				}
			}
		}

		return found;
	}
}
