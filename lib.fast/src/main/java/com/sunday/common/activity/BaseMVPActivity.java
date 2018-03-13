package com.sunday.common.activity;

import android.os.Bundle;

import com.sunday.common.mvp.PresenterImpl;
import com.sunday.common.mvp.IView;

/**
 * Created by siwei on 2018/3/13.
 */

public abstract class BaseMVPActivity<P extends PresenterImpl> extends BaseActivity implements IView {

    private P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        if(mPresenter != null){
            mPresenter.attachView(this);
        }
    }

    /**初始化Presenter*/
    public abstract P initPresenter();

    protected P getPresenter(){
        return mPresenter;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.detatchView();
            mPresenter = null;
        }
    }
}
