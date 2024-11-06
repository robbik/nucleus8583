package org.nucleus8583.core.config;

import org.nucleus8583.core.MessageSerializer;
import org.nucleus8583.core.type.TypeBuilder;
import org.nucleus8583.core.util.ObjectHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public class DefaultConfiguration implements Configuration {

    private final Map<String, TypeBuilder<?>> definedTypes;

    private final Map<String, MessageSerializer> serializers;

    public DefaultConfiguration() {
        definedTypes = new HashMap<>();
        serializers = new HashMap<>();

        final Map<Class<?>, Configurer> configurers = new HashMap<>();

        for (Configurer configurer : ServiceLoader.load(Configurer.class, Thread.currentThread().getContextClassLoader())) {
            if (configurer != null) {
                configurers.put(configurer.getClass(), configurer);
            }
        }

        for (Configurer configurer : ServiceLoader.load(Configurer.class, DefaultConfiguration.class.getClassLoader())) {
            if (configurer != null) {
                configurers.put(configurer.getClass(), configurer);
            }
        }

        final List<WithOrder<Configurer>> sorted = new ArrayList<>(configurers.size());

        for (Configurer configurer : configurers.values()) {
            sorted.add(new WithOrder<>(ObjectHelper.asInt(ObjectHelper.ifNotNull(ObjectHelper.getAnnotation(
                    configurer.getClass(), Order.class), Order::value), Order.DEFAULT), configurer));
        }

        sorted.sort(WithOrder.COMPARATOR_ASC);

        for (WithOrder<Configurer> configurerRef : sorted) {
            configurerRef.value().configure(this);
        }
    }

    @Override
    public DefaultConfiguration define(String name, TypeBuilder<?> typeBuilder) {
        if (typeBuilder == null) {
            definedTypes.remove(name);
        } else {
            definedTypes.put(name, typeBuilder);
        }
        return this;
    }

    @Override
    public TypeBuilder<?> getType(String name) {
        return definedTypes.get(name);
    }

    @Override
    public Configuration define(String name, MessageSerializer serializer) {
        if (serializer == null) {
            serializers.remove(name);
        } else {
            serializers.put(name, serializer);
        }
        return this;
    }

    @Override
    public MessageSerializer getSerializer(String name) {
        return serializers.get(name);
    }

    private record WithOrder<T> (int order, T value) {

        public static final Comparator<WithOrder<?>> COMPARATOR_ASC = (a, b) -> a.order - b.order;
    }
}
