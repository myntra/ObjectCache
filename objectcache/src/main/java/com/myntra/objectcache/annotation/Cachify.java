package com.myntra.objectcache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mario
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Cachify {
    String cacheKey() default "";
    long cacheDuration() default 0;
}