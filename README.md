关于AOP详解可参考
[Android中AOP实践之一概念篇](http://blog.csdn.net/anthony_3/article/details/78470499)

[Android中AOP实践之二场景篇](http://blog.csdn.net/anthony_3/article/details/78492497)

[Android中AOP实践之三AspectJ解析篇](http://blog.csdn.net/anthony_3/article/details/78509812)

在项目应用中可以分为通用场景和业务场景两种。其中通用场景我用到了**日志输出、方法计时、异步操作、异常拦截、动态权限**等，业务场景用到了**登录验证和单次点击**。整个AOP编程是基于aspect实现的，至于实现原理我想放到下一篇再讲。
 ## 通用场景

 ### 日志输出和方法计时
 日志这里参考了[hugo](https://github.com/JakeWharton/hugo)，JakeWharton大神通过这个项目提供了一个很好的AOP框架。
#### 使用方法
@DebugLog，可作用于类、构造方法及方法
```
@DebugLog
public class AopTestActivity extends BaseActivity {
    @DebugLog
    public void initView() {
        ...
    }
}
```
#### 核心代码
```
    @Around("method() || constructor()")
    public Object logAndExecute(ProceedingJoinPoint joinPoint) throws Throwable {
    	//进入方法打印日志
        enterMethod(joinPoint);
		//过程计时
        long startNanos = System.nanoTime();
        Object result = joinPoint.proceed();
        long stopNanos = System.nanoTime();
        long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);
		//离开方法打印日志
        exitMethod(joinPoint, result, lengthMillis);

        return result;
    }
```
### 异步操作
异步操作用AOP的好处就是不用每个方法都很复杂的包裹一层异步操作了~
#### 使用方法
```
    @Async
    private void async() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
```
#### 核心代码
异步这里用到了rxjava，如果项目中没有引用rxjava框架可以用异步线程代替~

```
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
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
```
###异常拦截
这里指的是可以用注解来暂时性解决方法中异常导致项目崩溃的问题，当然不建议大量滥用。
#### 使用方法
```
    @Safe
    private void ex() {
        int a = 10 / 0;
    }
```
#### 核心代码
```
    @Around("execution(!synthetic * *(..)) && methodAnnotated()")
    public Object aroundJoinPoint(final ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        try {
            result = joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable e) {
            LogUtils.e(getStringFromException(e));
        }
        return result;
    }

    private static String getStringFromException(Throwable ex) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
```
### 动态权限
#### 使用方法
#### 核心代码
## 业务场景
### 登录验证
这是一种最常用的场景了，点击某个按钮自动检测是否登录，如果没有登录则跳转到登录页面。
#### 使用方法
这里编写的AOP注解是支持butterknife的。
```
    @CheckLogin
    @OnClick(R.id.btn_login)
    public void onViewClicked() {
    	...
    }
```
这个业务比较简单，就不贴实现代码了。
### 单次点击
这个场景也比较常见，经常用户点提交性按钮时可能存在连续点击的情况，用这个注解可以及时避免。
#### 使用方法
```
    @SingleClick
    @OnClick(R.id.btn_test)
    public void onViewClicked(View view) {
        btnTest.setText("clickNum " + clickNum++);
    }
```
#### 核心代码
```
    //viewtag
    private static int TIME_TAG = R.id.click_time;
    //过滤掉600毫秒内的连续点击
    private static final int MIN_CLICK_DELAY_TIME = 600;

    @Pointcut("execution(@com.app.base.aop.SingleClick * *(..))")
    public void methodAnnotated() {}

    @Around("methodAnnotated()")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        View view = null;
        for (Object arg : joinPoint.getArgs())
            if (arg instanceof View) view = (View) arg;
        if (view != null) {
            Object tag = view.getTag(TIME_TAG);
            long lastClickTime = ((tag != null) ? (long) tag : 0);
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                view.setTag(TIME_TAG, currentTime);
                joinPoint.proceed();
            }
        }
    }
```
以上这些场景基本够项目的日常操作了，通过这些切片也可以节省部分重复劳动，毕竟程序员都是懒惰的嘛。
