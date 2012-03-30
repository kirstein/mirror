package org.kolmas.mirror.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.kolmas.mirror.Mirror;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Contain {
	/**
	 * Change key name while storing data.
	 * @return
	 */
	String name() default "";
	/**
	 * Only overrides targets data when received data is not null.
	 * Default value is true (default fields are nullable).
	 * @return
	 */
	boolean nullable() default true;
	/**
	 * Define custom method name. After defining methods mirror will search for those given methods in container.
	 * Methods go in pairs. When defining custom method name it is needed to generate both retreive and store methods.
	 * Created methods must have @ContainMethod annotation where it is needed to mark its name.
	 * 
	 * @return
	 */
	String method() default Mirror.NOT_DEFINED;
}
