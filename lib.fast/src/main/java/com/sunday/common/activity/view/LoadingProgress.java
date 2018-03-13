package com.sunday.common.activity.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by siwei on 2018/3/12.<br/>
 * 定义Loading弹窗的抽象类，会在BaseActivity中的showProgressDialog和dismissProgressDialog使用
 */

public class LoadingProgress extends Dialog{

    public LoadingProgress(@NonNull Context context) {
        super(context);
    }

    public LoadingProgress(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingProgress(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

}
