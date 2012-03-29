package org.kolmas.mirror.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.kolmas.mirror.container.Container;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Contain {
	String name() default "";
	boolean nullable() default true;
	boolean storeCollection() default false;
	String setCollection() default Container.COLLECTION_SET_METHOD;
	String getCollection() default Container.COLLECTION_GET_METHOD;
}
