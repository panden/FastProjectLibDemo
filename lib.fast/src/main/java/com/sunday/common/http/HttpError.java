package com.sunday.common.http;

import com.sunday.common.error.ApiError;

/**
 * Created by siwei on 2018/3/19.
 */

public enum HttpError implements ApiError {

    /**未知异常*/
    UNKnow(1001, "未知异常"),
    /**Http访问异常*/
    HttpError(1002, "Http访问出错"),
    /**解析异常*/
    ParseError(1004, "解析异常"),
    /**网络异常*/
    NetWorkError(1005, "网络异常"),

    HttpsError(1006, "Https认证异常");;

    HttpError(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    public static final int UNKNOWN = -1;

    public static final int HTTP_ERROR = 1;

    public static final int PARSE_ERROR = 2;

    public static final int NETWORD_ERROR = 3;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
