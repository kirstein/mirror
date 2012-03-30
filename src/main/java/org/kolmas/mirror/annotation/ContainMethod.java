package org.kolmas.mirror.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.kolmas.mirror.Mirror;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ContainMethod {
    
    /**
     * Marks method as storage method.
     * @return
     */
    String store() default Mirror.NOT_DEFINED;
    /**
     * Marks method as fetching method.
     * @return
     */
    String retrieve() default Mirror.NOT_DEFINED;
    
}
