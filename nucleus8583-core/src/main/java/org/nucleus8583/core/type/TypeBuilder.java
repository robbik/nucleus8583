package org.nucleus8583.core.type;

import java.util.Properties;

public interface TypeBuilder<T> {

    Class<? extends Type<T>> getObjectClass();

    TypeBuilder<T> with(Properties properties);

    Type<T> build();

    TypeBuilder<T> copy();
}
