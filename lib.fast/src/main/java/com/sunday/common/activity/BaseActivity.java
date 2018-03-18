package com.sunday.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.EditText;

import com.sunday.common.R;
import com.sunday.common.activity.tool.InputMethodHelper;
import com.sunday.common.activity.tool.LoadingProgressHelper;
import com.sunday.common.activity.view.NavBar;
import com.sunday.common.cache.ACache;
import com.sunday.common.event.EventBus;
import com.sunday.common.utils.ToastUtils;


/**
 * Created by siwei on 2015/6/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    protected Context mContext;
    private InputMethodHelper mInputMethodHelper;

    protected NavBar mTitleBar;
    private ViewStub bodyStub;
    private View bodyView;


    //标识是否显示对话框
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = this;
        mInputMethodHelper = new InputMethodHelper(this);
        super.setContentView(R.layout.activity_base);
        mTitleBar = (NavBar) findViewById(R.id.title_nb);
        bodyStub = (ViewStub) findViewById(R.id.body_vs);
    }

    @Override
    public void setContentView(int layoutResID) {
        bodyStub.setLayoutResource(layoutResID);
        bodyView = bodyStub.inflate();
        init();
    }

    private void init() {
        initView();
        initListener();
        initData();
    }

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }

    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }


    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    public NavBar getTitleBar(){
        return mTitleBar;
    }

    /**
     * 退出Activity
     */
    public void back() {
        finish();
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
    protected void closeSoftInput() {
        mInputMethodHelper.closeSoftInput(this);
    }

    /**
     * 显示软键盘
     */
    protected void showSoftInput(EditText et) {
        mInputMethodHelper.showSoftInput(et);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadingHelper != null) {
            mLoadingHelper.destory();
        }
        dismissProgressDialog();
        mInputMethodHelper.fixInputMethodManagerLeak(this);
        mInputMethodHelper = null;
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    /**
     * 发送event
     */
    protected void send(Object event) {
        EventBus.getDefault().post(event);
    }

    /**
     * loading帮助类
     */
    private LoadingProgressHelper mLoadingHelper;


    /**
     * 显示loading
     */
    protected void showProgressDialog() {
        if (mLoadingHelper == null) {
            mLoadingHelper = new LoadingProgressHelper(this);
        }
        mLoadingHelper.showProgress();
    }

    /**
     * 消失loading
     */
    protected void dismissProgressDialog() {
        if (mLoadingHelper != null) {
            mLoadingHelper.dismissProgress();
        }
    }

    public void toast(String msg) {
        if (mContext != null) {
            ToastUtils.showToast(mContext.getApplicationContext(), msg);
        }
    }


}
