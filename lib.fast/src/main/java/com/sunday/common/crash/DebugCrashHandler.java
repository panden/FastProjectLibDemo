package com.sunday.common.crash;

import android.content.Context;

/**
 * Created by siwei on 2018/5/13.
 */

public class DebugCrashHandler implements Thread.UncaughtExceptionHandler {

    private DebugGenerator mDebugGenerator;
    private Context mContext;

    public DebugCrashHandler(Context context, DebugGenerator debugGenerator){
        mDebugGenerator = debugGenerator;
        mContext = context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

    }


    public interface DebugGenerator{

        /**获取crash存储的目录*/
        String getCrashDir(Context context);

        /**获取crash存储的文件名*/
        String getCrashName(Context context);
    }
}
