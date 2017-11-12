package com.jie.aoptest.aspect;

import android.util.Log;
import android.view.View;

import com.jie.aoptest.R;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Calendar;

/**
 * desc：防止连续点击
 * author：haojie
 * date：2017/10/30
 */
@Aspect
public class SingleClickAspect {
    static int TIME_TAG = R.id.click_time;
    public static final int MIN_CLICK_DELAY_TIME = 600;

    @Pointcut("execution(@com.jie.aoptest.aop.SingleClick * *(..))")
    public void methodAnnotated() {
    }

    @Around("methodAnnotated()")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        View view = null;
        for (Object arg : joinPoint.getArgs())
            if (arg instanceof View) view = (View) arg;
        if (view != null) {
            Object tag = view.getTag(TIME_TAG);
            long lastClickTime = ((tag != null) ? (long) tag : 0);
            Log.d("SingleClickAspect", "lastClickTime:" + lastClickTime);
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {//过滤掉600毫秒内的连续点击
                view.setTag(TIME_TAG, currentTime);
                Log.d("SingleClickAspect", "currentTime:" + currentTime);
                joinPoint.proceed();
            }
        }
    }
}
