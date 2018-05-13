package com.sunday.common.http.intercepts;

import com.sunday.common.http.exception.GatewayTimeoutException;

import java.io.IOException;
import java.net.UnknownHostException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Http 返回的错误码拦截器
 */
public class HttpCodeInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        checkCode(response.code());
        return response;
    }

    private void checkCode(int code) throws IOException {
        if (code == 404) {//找不到服务
            throw new UnknownHostException();
        } else if (code == 400) {

        } else if (code == 401) {

        } else if(code == 504){//缓存读取过期
            throw new GatewayTimeoutException();
        }
    }
}
