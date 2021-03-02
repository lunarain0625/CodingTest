package com.example.weatherboi;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private Activity app_activity = null;


    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Fresco.initialize(this);
        initGlobeActivity();
    }

    /**
     * 初始化Activity
     **/
    private void initGlobeActivity() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                app_activity = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {
                app_activity = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                app_activity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                app_activity = activity;
            }

            @Override
            public void onActivityStopped(Activity activity) {
                app_activity = activity;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                app_activity = activity;
            }
        });
    }

    /**
     * 获取当前Activity
     **/
    public Activity getCurrentActivity() {
        return app_activity;
    }

}
