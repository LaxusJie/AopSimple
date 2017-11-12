package com.jie.aoptest;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.Stack;

/**
 * desc：全局类
 * author：haojie
 * date：2017/10/30
 */
public class App extends Application {
    private static App mApp;
    public Stack<Activity> store;

    public void onCreate() {
        super.onCreate();
        mApp = this;
        store = new Stack<>();
        registerActivityLifecycleCallbacks(new SwitchBackgroundCallbacks());
    }

    public static App getAppContext() {
        return mApp;
    }


    private class SwitchBackgroundCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            store.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            store.remove(activity);
        }
    }

    /**
     * 获取当前的Activity
     *
     * @return
     */
    public Activity getCurActivity() {
        return store.lastElement();
    }
}
