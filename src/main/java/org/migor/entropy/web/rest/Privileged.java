package org.migor.entropy.web.rest;

import org.migor.entropy.domain.PrivilegeName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by damoeb on 9/3/14.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Privileged {
    PrivilegeName[] value();
}
