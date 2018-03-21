package com.sunday.common.http;

import com.sunday.common.error.ApiError;

/**
 * Created by siwei on 2018/3/19.
 */

public interface ResponseListener<T> {

    /**请求成功回调*/
    void onResponse(T data);

    /**请求异常或者接口返回的参数说当前异常回调(如接口isSuccess=false的时候回调该方法，并传入自定义的错误状态)*/
    void onFaild(ApiError error);

    /**请求执行完成*/
    void onComplate();
}
