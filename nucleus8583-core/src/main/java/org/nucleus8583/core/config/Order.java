package org.nucleus8583.core.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Order {

    int FIRST = Integer.MIN_VALUE;

    int LAST = Integer.MAX_VALUE;

    int DEFAULT = 0;

    int value() default DEFAULT;
}
