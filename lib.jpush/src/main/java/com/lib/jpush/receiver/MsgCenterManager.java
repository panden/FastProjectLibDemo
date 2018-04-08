package com.lib.jpush.receiver;

import android.content.Context;
import android.content.Intent;

import com.lib.jpush.receiver.listener.JPushMsgListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siwei on 2018/4/8.
 */

public class MsgCenterManager {

    private static MsgCenterManager INSTANCE;
    private List<JPushMsgListener> mMsgListeners;

    private MsgCenterManager() {
        mMsgListeners = new ArrayList<>();
    }

    public static MsgCenterManager instance() {
        if (INSTANCE == null) {
            synchronized (MsgCenterManager.class) {
                INSTANCE = new MsgCenterManager();
            }
        }
        return INSTANCE;
    }


    public void addReceiver(JPushMsgListener listener) {
        if (listener != null && !mMsgListeners.contains(listener)) {
            mMsgListeners.add(listener);
        }
    }

    public void removeReceiver(JPushMsgListener listener) {
        if (mMsgListeners.contains(listener)) {
            mMsgListeners.remove(listener);
        }
    }


    protected void dispatchOnRegistrationID(Context context, Intent intent) {
        for (JPushMsgListener listener : mMsgListeners) {
            try {
                listener.onRegistrationID(context, intent);
            } catch (Exception e) {

            }
        }
    }

    protected void dispatchOnMsgReceived(Context context, Intent intent) {
        for (JPushMsgListener listener : mMsgListeners) {
            try {
                listener.onMsgReceived(context, intent);
            } catch (Exception e) {

            }
        }
    }

    protected void dispatchOnNotificationReceived(Context context, Intent intent) {
        for (JPushMsgListener listener : mMsgListeners) {
            try {
                listener.onNotificationReceived(context, intent);
            } catch (Exception e) {
            }
        }
    }

    protected void dispatchOnNotificationOpened(Context context, Intent intent) {
        for (JPushMsgListener listener : mMsgListeners) {
            try {
                listener.onNotificationOpened(context, intent);
            } catch (Exception e) {

            }
        }
    }

    protected void dispatchOnRitchpushCallBack(Context context, Intent intent) {
        for (JPushMsgListener listener : mMsgListeners) {
            try {
                listener.onRitchpushCallBack(context, intent);
            } catch (Exception e) {

            }
        }
    }

    protected void dispatchOnConnectionChange(Context context, Intent intent) {
        for (JPushMsgListener listener : mMsgListeners) {
            try {
                listener.onConnectionChange(context, intent);
            } catch (Exception e) {

            }
        }
    }

    protected void dispatchOnOther(Context context, Intent intent) {
        for (JPushMsgListener listener : mMsgListeners) {
            try {
                listener.onOther(context, intent);
            } catch (Exception e) {
            }
        }
    }

    /**释放*/
    public void release(){
        mMsgListeners.clear();
        mMsgListeners = null;
        INSTANCE = null;
    }

}
