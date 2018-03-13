package com.fastproject.demo.ui;

import com.sunday.common.mvp.PresenterImpl;

/**
 * Created by siwei on 2018/3/13.
 */

public class TestPresenter extends PresenterImpl<MVPTestActivity, TestModel> {
    @Override
    public TestModel initModel() {
        return new TestModel();
    }

    public void showMsg(){
        getView().showMsgText(getModel().getMsg());
    }
}
