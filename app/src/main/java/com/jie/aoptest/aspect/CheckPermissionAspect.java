package com.jie.aoptest.aspect;

import android.util.Log;

import com.jie.aoptest.aop.CheckPermission;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * desc：异步切片
 * author：haojie
 * date：2017/11/2
 */
@Aspect
public class CheckPermissionAspect {

    @Pointcut("execution(@com.jie.aoptest.aop.CheckPermission * *(..)) && @annotation(checkPermission)")
    public void checkPermission(CheckPermission checkPermission){};

    @Before("checkPermission(checkPermission)")
    public void check(JoinPoint joinPoint, CheckPermission checkPermission){
        //从注解信息中获取声明的权限。
        String neededPermission = checkPermission.declaredPermission();
        Log.d("CheckPermissionAspect", joinPoint.toShortString());
        Log.d("CheckPermissionAspect", "\tneeded permission is " + neededPermission);
    }
}
