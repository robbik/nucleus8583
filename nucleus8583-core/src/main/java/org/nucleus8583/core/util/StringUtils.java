package org.nucleus8583.core.util;

public abstract class StringUtils {

    public static boolean isEmpty(String value) {
        if (value == null) {
            return true;
        }

        return value.length() == 0;
    }
}
