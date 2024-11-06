package org.nucleus8583.core.config;

import org.nucleus8583.core.MessageSerializer;
import org.nucleus8583.core.type.TypeBuilder;

public interface Configuration {

    Configuration DEFAULT = new DefaultConfiguration();

    Configuration define(String name, TypeBuilder<?> typeBuilder);

    TypeBuilder<?> getType(String name);

    Configuration define(String name, MessageSerializer serializer);

    MessageSerializer getSerializer(String name);
}
