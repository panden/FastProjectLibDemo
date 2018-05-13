package com.sunday.common.http.intercepts;

import com.sunday.common.error.ErrorState;
import com.sunday.common.error.IErrorStatus;

/**
 * 错误状态
 */
public enum HttpErrorStatus implements IErrorStatus {


    ;

    private ErrorState mErrorState;

    @Override
    public int getErrorCode() {
        return mErrorState.getErrorCode();
    }

    @Override
    public void setErrorCode(int errorCode) {
        mErrorState.setErrorCode(errorCode);
    }

    @Override
    public String getErrorMessage() {
        return mErrorState.getErrorMessage();
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        mErrorState.setErrorMessage(errorMessage);
    }

    @Override
    public String getCaseMessage() {
        return mErrorState.getCaseMessage();
    }

    @Override
    public void setCaseMessage(String caseMessage) {
        mErrorState.setCaseMessage(caseMessage);
    }

    @Override
    public long getCaseTime() {
        return mErrorState.getCaseTime();
    }

    @Override
    public void setCaseTime(long caseTime) {
        mErrorState.setCaseTime(caseTime);
    }

    @Override
    public void setException(Exception e) {
        mErrorState.setException(e);
    }

    HttpErrorStatus(ErrorState errorState) {
        mErrorState = errorState;
    }

    HttpErrorStatus(int errorCode, String errorMessage, String caseMessage){
        mErrorState = new ErrorState(errorCode, errorMessage, caseMessage);
    }
}
