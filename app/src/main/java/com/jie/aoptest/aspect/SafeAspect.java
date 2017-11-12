package com.jie.aoptest.aspect;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * desc：安全切片
 * author：haojie
 * date：2017/11/2
 */
@Aspect
public class SafeAspect {

    @Pointcut("@within(com.jie.aoptest.aop.Safe)||@annotation(com.jie.aoptest.aop.Safe)")
    public void methodAnnotated() {
    }

    @Around("execution(!synthetic * *(..)) && methodAnnotated()")
    public Object aroundJoinPoint(final ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        try {
            result = joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable e) {
            Log.e("aspect", getStringFromException(e));
        }
        return result;
    }

    private static String getStringFromException(Throwable ex) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
}
