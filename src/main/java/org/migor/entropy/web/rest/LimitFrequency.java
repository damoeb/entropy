package org.migor.entropy.web.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Specifies how often a service method may be called. The DoormanAspect will watch and validate these conditions.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitFrequency {
    String resource();

    int freeze();

    TimeUnit timeUnit();
}
