package com.sunday.common.error;

/**
 * Created by siwei on 2018/3/19.
 */

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.sunday.common.http.HttpError;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLKeyException;

import retrofit2.HttpException;

public class ErrorEngine {

    /**把Http异常转换为自定义的错误*/
    public static IErrorStatus handleHttpException(Throwable e) {
        IErrorStatus apiError;
        if (e instanceof HttpException || e instanceof org.apache.http.HttpException) {
            //HTTP访问错误
            apiError = HttpError.HttpError;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //数据解析异常
            apiError = HttpError.ParseError;
        } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
            //连接或者找不到服务导致的异常
            apiError = HttpError.NetWorkError;
        } else if(e instanceof SSLException || e instanceof SSLKeyException){
            //Https ssl认证的异常
            apiError = HttpError.HttpsError;
        }else {
            //其余位置的异常
            apiError = HttpError.UNKnow;
        }
        return apiError;
    }
}