package org.nucleus8583.core.osgi;

import org.nucleus8583.core.io.ResourceLoader;

public abstract class OsgiUtils {

    public static boolean inOsgiEnvironment() {
        try {
            Class.forName("org.osgi.framework.FrameworkUtil");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    public static ResourceLoader createOsgiBundleResourceLoader() {
        try {
            return (ResourceLoader) Class.forName("org.nucleus8583.core.io.OsgiBundleResourceLoader").newInstance();
        } catch (Throwable t) {
            return null;
        }
    }
}
