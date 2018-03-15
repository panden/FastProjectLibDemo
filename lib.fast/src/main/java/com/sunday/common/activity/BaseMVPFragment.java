package com.sunday.common.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sunday.common.mvp.IView;
import com.sunday.common.mvp.PresenterImpl;

/**
 * Created by siwei on 2018/3/13.
 * 给予MVP封装的Fragment
 */

public abstract class BaseMVPFragment<P extends PresenterImpl> extends BaseFragment implements IView {

    private P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        if(mPresenter != null){
            mPresenter.attachView(this);
        }
    }

    public abstract P initPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.detatchView();
        }
    }
}
