package org.nucleus8583.oim.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class BeanUtils {

    private static final Map<String, Field> map;

    static {
        map = new HashMap<String, Field>();
    }

    public static Object newInstance(Class<?> _class) {
        try {
            return _class.newInstance();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> forClassName(String className) {
        try {
            return Class.forName(className, false, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static Field forFieldName(Class<?> theClass, String className, String fieldName) {
        Class<?> current = theClass;

        while ((current != null) && (current != Object.class)) {
            Field[] arrFields = current.getDeclaredFields();

            for (int i = arrFields.length - 1; i >= 0; --i) {
                Field f = arrFields[i];

                if (f.getName().equals(fieldName)) {
                    f.setAccessible(true);

                    return f;
                }
            }

            current = current.getSuperclass();
        }

        throw new IllegalArgumentException("field " + fieldName + " doesn't exist in class " + className);
    }

    public static void addToCache(String className, String fieldName) {
        Class<?> theClass = forClassName(className);

        if (!theClass.isAssignableFrom(Map.class)) {
            BeanUtils.map.put(className + "?field=" + fieldName, forFieldName(theClass, className, fieldName));
        }
    }

    @SuppressWarnings("unchecked")
    public static void set(String className, String fieldName, Object obj, Object value) {
        if (obj instanceof Map<?, ?>) {
            ((Map<String, Object>) obj).put(fieldName, value);
        } else {
            Field f = BeanUtils.map.get(className + "?field=" + fieldName);

            if (f == null) {
                throw new IllegalArgumentException("unable to find field " + fieldName + " on class " + className);
            }

            try {
                f.set(obj, value);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static<T> T get(String className, String fieldName, Object obj, Class<T> type) {
        if (obj instanceof Map<?, ?>) {
            return (T) ((Map<String, Object>) obj).get(fieldName);
        }

        Field f = BeanUtils.map.get(className + "?field=" + fieldName);

        if (f == null) {
            throw new IllegalArgumentException("unable to find field " + fieldName + " on class " + className);
        }

        try {
            return (T) f.get(obj);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
