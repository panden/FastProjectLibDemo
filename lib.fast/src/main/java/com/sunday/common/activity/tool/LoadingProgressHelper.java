package com.sunday.common.activity.tool;

import android.content.Context;

import com.sunday.common.activity.view.LoadingProgress;
import com.sunday.common.widgets.loadingdialog.LoadingDialog;

/**
 * Created by siwei on 2018/3/13.
 * LoadingProgress的帮助类，想实现自定义的loading，继承AbsLoadingProgress即可
 */

public class LoadingProgressHelper {

    private LoadingProgress mLoadingProgress;

    public LoadingProgressHelper(Context context) {
        mLoadingProgress = new LoadingDialog(context);
        mLoadingProgress.setCanceledOnTouchOutside(false);
    }

    public LoadingProgress getLoadingProgress() {
        return mLoadingProgress;
    }

    public boolean isLoadingShowed() {
        if (mLoadingProgress != null) {
            return mLoadingProgress.isShowing();
        }
        return false;
    }

    /**
     * 显示loading
     */
    public void showProgress() {
        if (mLoadingProgress != null && !mLoadingProgress.isShowing()) {
            mLoadingProgress.show();
        }
    }

    /**
     * 隐藏loading
     */
    public void dismissProgress() {
        if (mLoadingProgress != null && mLoadingProgress.isShowing()) {
            mLoadingProgress.dismiss();
        }
    }

    public void destory() {
        dismissProgress();
        mLoadingProgress = null;
    }
}
