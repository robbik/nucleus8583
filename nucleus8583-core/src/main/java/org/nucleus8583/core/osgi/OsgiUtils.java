package org.nucleus8583.core.osgi;

import java.util.logging.Logger;

import org.nucleus8583.core.io.ResourceLoader;

public abstract class OsgiUtils {

    private static final Logger log = Logger.getLogger(OsgiUtils.class.getName());

    public static boolean detectOsgiEnvironment() {
        boolean detected;

        try {
            Class<?> cla = Class.forName("org.nucleus8583.core.io.OsgiBundleResourceLoader");

            detected = (Class.forName("org.osgi.framework.FrameworkUtil").getMethod("getBundle", Class.class)
                    .invoke(null, cla) != null);
        } catch (Throwable t) {
            detected = false;
        }

        if (detected) {
            log.info("OSGi environment detected");
        } else {
            log.info("OSGi environment not detected");
        }

        return detected;
    }

    public static ResourceLoader createOsgiBundleResourceLoader() {
        try {
            return (ResourceLoader) Class.forName("org.nucleus8583.core.io.OsgiBundleResourceLoader").newInstance();
        } catch (Throwable t) {
            return null;
        }
    }
}
