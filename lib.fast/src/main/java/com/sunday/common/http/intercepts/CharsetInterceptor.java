package com.sunday.common.http.intercepts;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by siwei on 2018/5/6.
 * 添加编码请求头拦截器
 */
public class CharsetInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                //.addHeader("Content-Type", "multipart/form-data; charset=UTF-8")
                .build();
        return chain.proceed(request);
    }
}
