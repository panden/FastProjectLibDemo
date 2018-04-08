package com.lib.jpush;

import android.app.Application;
import android.content.Context;

import com.lib.jpush.receiver.MsgCenterManager;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by siwei on 2018/4/7.
 */

public class JPushSDK {

    private Context mContext;

    private static JPushSDK INSTANCE;
    private static boolean hasInited;
    private boolean isDebug;

    private JPushSDK(Context context, boolean isDebug){
        mContext = context;
        isDebug = isDebug;
    };

    /**初始化SDK*/
    public static void initSdk(Application application, boolean isDebug){
        if(!hasInited){
            JPushInterface.setDebugMode(isDebug);
            JPushInterface.init(application);
            INSTANCE = new JPushSDK(application.getApplicationContext(), isDebug);
            Logger.isDebug(isDebug);
            hasInited = true;
        }
    }

    public static JPushSDK instance(){
        if(INSTANCE == null)throw new NullPointerException("JPushSDK has not been initialized, please call JPushSDK.initSdk(Application application, boolean isDebug) initialize");
        return INSTANCE;
    }

    /**判断当前是否为调试模式*/
    public boolean isDebug() {
        return isDebug;
    }

    /**停止推送*/
    public void stopJPush(){
        if(!JPushInterface.isPushStopped(mContext)){
            JPushInterface.stopPush(mContext);
        }
    }

    /**继续推送*/
    public void resumeJPush(){
        if(JPushInterface.isPushStopped(mContext)){
            JPushInterface.resumePush(mContext);
        }
    }

    /**释放SDK*/
    public static void releaseSDK(){
        MsgCenterManager.instance().release();
    }
}
