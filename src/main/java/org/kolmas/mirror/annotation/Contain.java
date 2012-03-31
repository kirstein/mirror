package org.kolmas.mirror.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.kolmas.mirror.Mirror;

/**
 * Annotation for {@link Mirror} API.
 * 
 * Fields annotated with {@link Contain} annotation must have proper getter and
 * setter methods. Getter and Setter methods must be public and non static.
 * 
 * Fields annotated with {@link Contain} must be private and non-static.
 * 
 * @author Mikk Kirstein
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Contain {
    /**
     * Change key name while storing data. {@link Mirror} will define given name
     * as its key value when calling {@link Mirror#store} and
     * {@link Mirror#retrieve}
     */
    String name() default Mirror.NOT_DEFINED;

    /**
     * Only overrides targets data when received data is not null. Default value
     * is true (default fields are nullable).
     */
    boolean nullable() default true;

    /**
     * Define custom method name. After defining methods mirror will search for
     * those given methods in container. Methods go in pairs. When defining
     * custom method name it is needed to generate both retrieve and store
     * methods. Created methods must have {@link ContainMethod} annotation where it is
     * needed to mark its name.
     * 
     * @return
     */
    String method() default Mirror.NOT_DEFINED;
}
