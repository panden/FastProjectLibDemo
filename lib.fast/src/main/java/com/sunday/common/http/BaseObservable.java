package com.sunday.common.http;

import com.sunday.common.error.ApiError;
import com.sunday.common.error.ErrorEngine;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by siwei on 2018/3/19.
 * Http请求Observable封装类
 */

public abstract class BaseObservable<T> implements Observer<T> {

    private ResponseListener<T> mResponseListener;
    private Disposable mDisposable;
    private CompositeDisposable mCompositeDisposable;

    public BaseObservable(CompositeDisposable compositeDisposable, ResponseListener<T> listener){
        this.mCompositeDisposable = compositeDisposable;
        this.mResponseListener = listener;
    }

    public BaseObservable(ResponseListener<T> listener){
        this.mResponseListener = listener;
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
        if(mCompositeDisposable != null){
            mCompositeDisposable.add(d);
        }
    }

    public Disposable getDisposable(){
        return mDisposable;
    }

    @Override
    public void onNext(T t) {
        onListenerResponse(t);
        onComplete();
    }

    @Override
    public void onError(Throwable e) {
        onFaild(ErrorEngine.handleHttpException(e));
        unSubscrib();
    }

    @Override
    public void onComplete() {
        unSubscrib();
    }

    /**请求成功会回调该接口*/
    public abstract void onResponse(T data);

    /**请求异常会回调该接口*/
    public abstract void onFaild(ApiError error);

    //执行完之后就解除订阅
    private void unSubscrib(){
        if(mDisposable != null && !mDisposable.isDisposed()){
            mDisposable.dispose();
            if(mCompositeDisposable != null){
                mCompositeDisposable.remove(mDisposable);
            }
            mDisposable = null;
        }
    }

    /**回调listener.onResponse(data)*/
    protected void onListenerResponse(T data){
        if(mResponseListener != null){
            mResponseListener.onResponse(data);
        }
    }

    /**回调listener.onFaild(ApiError)*/
    protected void onListenerFaild(ApiError error){
        if(mResponseListener != null){
            mResponseListener.onFaild(error);
        }
    }
}
