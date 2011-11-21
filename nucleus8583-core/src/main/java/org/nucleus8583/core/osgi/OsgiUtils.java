package org.nucleus8583.core.osgi;

import org.nucleus8583.core.io.ResourceLoader;

public abstract class OsgiUtils {

    public static boolean detectOsgiEnvironment() {
        boolean detected;

        try {
            Class<?> cla = Class.forName("org.nucleus8583.core.io.OsgiBundleResourceLoader");

            detected = (Class.forName("org.osgi.framework.FrameworkUtil").getMethod("getBundle", Class.class)
                    .invoke(null, cla) != null);
        } catch (Throwable t) {
            detected = false;
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
