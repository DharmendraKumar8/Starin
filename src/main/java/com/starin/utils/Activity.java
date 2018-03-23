package com.starin.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation Used on Controller methods to 
 * Identify  it is a Activity used for Role  
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Activity {
    
	public String activityName() default "";
	public String activityDescription() default "";
	public String url() default "";
	public String methodType() default "";
	public boolean active() default true;

	/*
	 * Its Internal name for mapping with @Activity annotated method.
	 * value formate = "ClassName_methodName"
	 *  example : "com.oodles.controller.UserHandler_verify" 
	 */
	public String handlerMethodName() default "";

}
