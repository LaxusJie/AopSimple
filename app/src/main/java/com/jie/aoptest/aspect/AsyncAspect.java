package com.jie.aoptest.aspect;

import android.os.Looper;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * desc：异步切片
 * author：haojie
 * date：2017/11/2
 */
@Aspect
public class AsyncAspect {

    @Pointcut("@within(com.jie.aoptest.aop.Async)||@annotation(com.jie.aoptest.aop.Async)")
    public void methodAnnotated() {}


    @Around("execution(!synthetic * *(..)) && methodAnnotated()")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint) throws Throwable {
        Observable.create(new Observable.OnSubscribe<Object>() {

            @Override
            public void call(Subscriber<? super Object> subscriber) {
                Looper.prepare();
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                Looper.loop();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }
}
