package com.jie.aoptest.aspect;

import android.util.Log;
import android.widget.Toast;

import com.jie.aoptest.App;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


/**
 * desc：检测登录切片
 * author：haojie
 * date：2017/10/29
 */
@Aspect
public class CheckLoginAspect {

    @Pointcut("execution(@com.jie.aoptest.aop.CheckLogin * *(..))")
    public void methodAnnotated() {
    }


    @Around("methodAnnotated()")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.d("login", "请您登录");
        Toast.makeText(App.getAppContext().getCurActivity(), "请您登录", Toast.LENGTH_SHORT).show();
        joinPoint.proceed();
    }
}

