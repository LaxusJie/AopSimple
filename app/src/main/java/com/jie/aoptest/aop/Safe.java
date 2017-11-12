package com.jie.aoptest.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * desc：安全方法
 * 防止异常崩溃
 * author：haojie
 * date：2017/10/29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface Safe {
}
