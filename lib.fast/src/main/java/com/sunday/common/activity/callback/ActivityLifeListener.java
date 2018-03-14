package com.sunday.common.activity.callback;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * Created by siwei on 2018/3/14.
 */

public class ActivityLifeListener implements Application.ActivityLifecycleCallbacks {


    private WeakReference<Activity> mCurrentActivity;

    public @Nullable Activity getCurrentActivity() {
        if(mCurrentActivity != null) {
            mCurrentActivity.get();
        }
        return null;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        mCurrentActivity = new WeakReference<Activity>(activity);
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
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    public void destory(){
        mCurrentActivity.clear();
        mCurrentActivity = null;
    }
}
