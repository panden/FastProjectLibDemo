package com.sunday.common.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/6/23.
 */
public class BaseResponse<T> implements Serializable {

    private boolean success;
    private String message;

    private T data;



    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
