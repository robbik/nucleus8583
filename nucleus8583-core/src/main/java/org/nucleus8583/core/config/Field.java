package org.nucleus8583.core.config;

import org.nucleus8583.core.type.Type;

import java.util.Comparator;

public record Field(int no, Type<?> type) {

    public static final Comparator<Field> COMPARATOR_ASC = (a, b) -> a.no - b.no;
}
