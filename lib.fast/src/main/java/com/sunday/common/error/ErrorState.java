package com.sunday.common.error;

/**
 * Created by siwei on 2018/3/21.
 * 自定义错误状态接口定义
 */

public class ErrorState implements IErrorStatus {

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

    public ErrorState(int errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorState(int errorCode, Exception e) {
        this.errorCode = errorCode;
        this.errorMessage = e.getMessage();
        this.caseMessage = e.getCause().getMessage();
    }

    public ErrorState(int errorCode, String errorMessage, String caseMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.caseMessage = caseMessage;
    }

    @Override
    public String toString() {
        return "ErrorState{" +
                "errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", caseMessage='" + caseMessage + '\'' +
                ", caseTime=" + caseTime +
                '}';
    }
}
