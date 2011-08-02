package org.nucleus8583.oim.accessor;

import java.io.InputStream;

public interface ValueAccessor {

    void set(String className, Object obj, InputStream value);
}
