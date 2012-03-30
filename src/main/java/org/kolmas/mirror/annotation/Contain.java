package org.kolmas.mirror.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.kolmas.mirror.Mirror;
import org.kolmas.mirror.container.Container;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Contain {
	String name() default "";
	boolean nullable() default true;
	String method() default Mirror.NOT_DEFINED;
}
