package rk.commons.loader;

public abstract class OsgiBundleResourceLoaderFactory {

	public static boolean osgiDetected() {
		boolean detected;

		try {
			Class<?> cla = Class.forName(
					"rk.commons.support.loader.OsgiBundleResourceLoader");

			detected = (Class.forName("org.osgi.framework.FrameworkUtil")
					.getMethod("getBundle", Class.class).invoke(null, cla) != null);
		} catch (Throwable t) {
			detected = false;
		}

		return detected;
	}

	public static ResourceLoaderProvider create() {
		try {
			return (ResourceLoaderProvider) Class.forName(
					"rk.commons.support.loader.OsgiBundleResourceLoader").newInstance();
		} catch (Throwable t) {
			return null;
		}
	}
}
