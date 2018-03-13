package com.sunday.common.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.sunday.common.activity.view.tool.InputMethodHelper;
import com.sunday.common.activity.view.tool.LoadingProgressHelper;
import com.sunday.common.cache.ACache;
import com.sunday.common.event.EventBus;
import com.sunday.common.utils.ToastUtils;

/**
 * Created by siwei on 2015/6/9.
 */
public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    private InputMethodHelper mInputMethodHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view){
        initView(view);
        initData();
        initListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoadingProgressHelper != null) {
            mLoadingProgressHelper.destory();
        }
        mInputMethodHelper.fixInputMethodManagerLeak(getActivity());
        mInputMethodHelper = null;
        EventBus.getDefault().unregister(this);
    }

    protected abstract void initView(View view);

    protected abstract void initListener();

    protected abstract void initData();

    private LoadingProgressHelper mLoadingProgressHelper;

    protected boolean isLoadingShowed() {
        if (mLoadingProgressHelper != null) {
            return mLoadingProgressHelper.isLoadingShowed();
        }
        return false;
    }

    protected void showLoadingDialog() {
        if (mLoadingProgressHelper == null) {
            mLoadingProgressHelper = new LoadingProgressHelper(getActivity());
        }
        mLoadingProgressHelper.showProgress();
    }

    protected void dismissLoadingDialog() {
        if (mLoadingProgressHelper != null) {
            mLoadingProgressHelper.dismissProgress();
        }
    }

    public void toast(String msg) {
        if(mContext != null){
            ToastUtils.showToast(mContext.getApplicationContext(), msg);
        }
    }

    /**
     * 获取缓存
     */
    public ACache getCache() {
        return BaseApplication.getInstance().getCache();
    }

    /**
     * 关闭软键盘
     */
    protected void closeSoftInput(){
        mInputMethodHelper.closeSoftInput(getActivity());
    }

    /**
     * 显示软键盘
     */
    protected void showSoftInput(EditText et){
        mInputMethodHelper.showSoftInput(et);
    }


}
