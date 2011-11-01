package org.nucleus8583.core.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.nucleus8583.core.io.ClassResourceLoader;
import org.nucleus8583.core.io.ResourceLoader;
import org.nucleus8583.core.io.UrlResourceLoader;
import org.nucleus8583.core.osgi.OsgiUtils;

public abstract class ResourceUtils {

    private static ReadLock slock;

    private static WriteLock xlock;

    private static ResourceLoader[] loaders;

    private static final AtomicBoolean initialized;

    static {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

        slock = lock.readLock();
        xlock = lock.writeLock();

        loaders = new ResourceLoader[] { new ClassResourceLoader(), new UrlResourceLoader() };

        initialized = new AtomicBoolean(false);
    }

    private static void initialize() {
        if (!initialized.compareAndSet(false, true)) {
            return;
        }

        if (OsgiUtils.detectOsgiEnvironment()) {
            ResourceLoader osgi = OsgiUtils.createOsgiBundleResourceLoader();
            if (osgi != null) {
                addResourceLoader(0, osgi);
            }
        }
    }

    public static void addResourceLoader(ResourceLoader loader) {
        initialize();

        xlock.lock();
        try {
            List<ResourceLoader> list = new ArrayList<ResourceLoader>(Arrays.asList(loaders));
            list.add(loader);

            loaders = list.toArray(new ResourceLoader[0]);
        } finally {
            xlock.unlock();
        }
    }

    public static void addResourceLoader(int index, ResourceLoader loader) {
        initialize();

        xlock.lock();
        try {
            List<ResourceLoader> list = new ArrayList<ResourceLoader>(Arrays.asList(loaders));
            list.add(index, loader);

            loaders = list.toArray(new ResourceLoader[0]);
        } finally {
            xlock.unlock();
        }
    }

    public static URL[] getURLs(String location) {
        initialize();

        HashSet<URL> urls = new HashSet<URL>();

        slock.lock();
        try {
            for (int i = 0; i < loaders.length; ++i) {
                urls.addAll(loaders[i].getURLs(location));
            }
        } finally {
            slock.unlock();
        }

        return urls.toArray(new URL[0]);
    }

    public static URL getURL(String location) {
        initialize();

        URL found = null;

        slock.lock();
        try {
            for (int i = 0; i < loaders.length; ++i) {
                found = loaders[i].getURL(location);

                if (found != null) {
                    break;
                }
            }
        } finally {
            slock.unlock();
        }

        return found;
    }

    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        initialize();

        Class<?> found = null;

        slock.lock();
        try {
            for (int i = 0; i < loaders.length; ++i) {
                found = loaders[i].loadClass(className);

                if (found != null) {
                    break;
                }
            }
        } finally {
            slock.unlock();
        }

        if (found == null) {
            throw new ClassNotFoundException(className);
        }

        return found;
    }
}
