package com.sunday.common.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/6/23.
 */
public class ResultDO<T> implements Serializable {

    /**
     * success : true
     * type : sms_sent_success
     * message : 短信验证码发送成功，请注意查收
     */
    private boolean success;
    private String type;
    private boolean status;
    private String message;
    private String code;

    private T data;



    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
