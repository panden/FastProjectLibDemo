package com.sunday.common.error;

/**
 * Created by siwei on 2018/5/13.
 */

public interface IErrorStatus {

    public int getErrorCode();

    public void setErrorCode(int errorCode);

    public String getErrorMessage();

    public void setErrorMessage(String errorMessage);

    public String getCaseMessage();

    public void setCaseMessage(String caseMessage);

    public long getCaseTime();

    public void setCaseTime(long caseTime);

    public void setException(Exception e);
}
