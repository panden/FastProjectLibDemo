package com.lib.jpush.receiver.listener;

import android.content.Context;
import android.content.Intent;

/**
 * 消息回调
 */

public interface JPushMsgListener {

    void onRegistrationID(Context context, Intent intent);

    void onMsgReceived(Context context, Intent intent);

    void onNotificationReceived(Context context, Intent intent);

    void onNotificationOpened(Context context, Intent intent);

    void onRitchpushCallBack(Context context, Intent intent);

    void onConnectionChange(Context context, Intent intent);

    void onOther(Context context, Intent intent);
}
