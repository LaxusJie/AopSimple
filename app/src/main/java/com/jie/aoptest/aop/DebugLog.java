package com.jie.aoptest.aop;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * desc：debug
 * author：haojie
 * date：2017/10/29
 */
@Target({METHOD, CONSTRUCTOR})
@Retention(CLASS)
public @interface DebugLog {
}
