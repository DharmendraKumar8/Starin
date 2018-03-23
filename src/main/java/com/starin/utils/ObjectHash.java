package com.starin.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used for
 * add user defined dataType
 * In a HashMap
 *
 */
@Target({ ElementType.FIELD ,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ObjectHash{
	
	public String keys() default "";

}
