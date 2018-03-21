package com.sunday.common.error;

/**
 * Created by siwei on 2018/3/21.
 * 自定义异常接口定义
 */

public interface ApiError {

    int getCode();

    void setCode(int code);

    String getMsg();

    void setMsg(String msg);
}
