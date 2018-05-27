package com.sunday.common.http;

import com.sunday.common.error.IErrorStatus;

/**
 * Created by siwei on 2018/3/19.
 */

public enum HttpError implements IErrorStatus {

    /**未知异常*/
    UNKnow(1001, "未知异常"),
    /**Http访问异常*/
    HttpError(1002, "Http访问出错"),
    /**解析异常*/
    ParseError(1004, "解析异常"),
    /**网络异常*/
    NetWorkError(1005, "网络异常"),

    HttpsError(1006, "Https认证异常");

    HttpError(int code, String msg){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.caseMessage = caseMessage;
    }

    /**错误码*/
    protected int errorCode;
    /**错误信息*/
    protected String errorMessage;
    /**导致错误的原因*/
    protected String caseMessage;
    /**导致错误的时间*/
    private long caseTime = System.currentTimeMillis();

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getCaseMessage() {
        return caseMessage;
    }

    @Override
    public void setCaseMessage(String caseMessage) {
        this.caseMessage = caseMessage;
    }

    @Override
    public long getCaseTime() {
        return caseTime;
    }

    @Override
    public void setCaseTime(long caseTime) {
        this.caseTime = caseTime;
    }

    @Override
    public void setException(Exception e){
        this.errorMessage = e.getMessage();
        this.caseMessage = e.getCause().getMessage();
    }

}
