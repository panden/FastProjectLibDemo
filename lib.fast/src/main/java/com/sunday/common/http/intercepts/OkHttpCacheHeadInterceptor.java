package com.sunday.common.http.intercepts;

import android.content.Context;

import com.sunday.common.logger.Logger;
import com.sunday.common.utils.NetworkUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * okhttp get请求 缓存头拦截器, okhttp的默认缓存只支持get的请求
 */
public class OkHttpCacheHeadInterceptor implements Interceptor {

    private static final int HTTP_CODE_504 = 504;//未找到缓存信息

    private static final int MOBILE_ENABLE_MAX_AGE = 60 * 60;//有网络时 设置缓存超时时间1个小时
    private static final int MOBILE_UNENABLE_MAX_STALE = 60 * 60 * 24;//无网络时，设置超时为1天
    private static final CacheControl FORCE_NETWORK = new CacheControl.Builder()
            .maxAge(MOBILE_ENABLE_MAX_AGE, TimeUnit.SECONDS)
            .build();
    private static final CacheControl FORCE_CACHE = new CacheControl.Builder()
            .onlyIfCached().maxStale(MOBILE_UNENABLE_MAX_STALE, TimeUnit.SECONDS)
            .build();

    private Context context;

    public OkHttpCacheHeadInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response;
        if ("GET".equalsIgnoreCase(request.method())) {
            //有网络缓存时间为1小时，无网络缓存时间为1天
            if (NetworkUtils.isConnectInternet(context)) {
                request = request.newBuilder().cacheControl(FORCE_NETWORK).build();
                response = chain.proceed(request);
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + MOBILE_ENABLE_MAX_AGE)
                        .build();
            } else {
                //无网络时只从缓存中读取
                request = request.newBuilder().cacheControl(FORCE_CACHE).build();
                response = chain.proceed(request);
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + MOBILE_UNENABLE_MAX_STALE)
                        .build();
            }
            Logger.d("okhttp response.code=" + response.code());
            //缓存信息未读取到
        } else {
            response = chain.proceed(request);
        }
        return response;
    }
}
