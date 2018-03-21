package com.sunday.common.mvp;

/**
 * Created by siwei on 2018/3/13.
 */

public abstract class PresenterImpl<V extends IView, M extends IModel> implements IPresenter<V> {

    private V mIView;
    private M mIModel;

    public PresenterImpl() {
        mIModel = initModel();
    }

    public abstract M initModel();

    @Override
    public void attachView(V view) {
        mIView = view;
    }

    @Override
    public void detatchView() {
        mIView = null;
        if(mIModel != null){
            mIModel.onRelease();
            mIModel = null;
        }
        onRelease();
    }

    public V getView() {
        return mIView;
    }

    public M getModel() {
        return mIModel;
    }

    /**释放数据的时候会被调用*/
    protected void onRelease(){}

}
