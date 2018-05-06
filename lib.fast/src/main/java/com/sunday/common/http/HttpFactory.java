package com.sunday.common.http;

import android.content.Context;
import android.support.annotation.NonNull;

import com.sunday.common.http.intercepts.CacheHeadInterceptor;
import com.sunday.common.http.intercepts.CharsetInterceptor;
import com.sunday.common.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by siwei on 2018/3/15.
 */

public class HttpFactory {

    private Retrofit mRetrofit;
    private static HttpFactory instance;
    private static final long CACHE_SIZE = 1024 * 1024 * 10;//缓存的文件大小
    private static final int CONNECT_TIME = 10;//连接时长为10s
    private static final int READ_TIME_OUT = 15;//读取超时时长为15s
    private static final int WRITE_TIME_OUT = 20;//写入超时时长为20s
    private List<Interceptor> mInterceptors;//okhttp的拦截器

    public static void initFactory(@NonNull Context context, @NonNull String baseUrl) {
        instance = new HttpFactory(context, baseUrl);
    }

    public static HttpFactory instance() {
        if (instance == null)
            throw new NullPointerException("not call HttpFactory.initFactory(context, baseUrl)");
        return instance;
    }

    private HttpFactory(Context context, String baseUrl, Interceptor... interceptors) {
        mInterceptors = new ArrayList<>();
        if(interceptors != null)mInterceptors.addAll(Arrays.asList(interceptors));
        mRetrofit = initRetrofit(context, baseUrl);
    }

    private Retrofit initRetrofit(Context context, String baseUrl) {
        return new Retrofit.Builder()
                .client(genericClient(context))
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**创建接口服务*/
    public <T> T createApiService(Class<T> service) {
        return mRetrofit.create(service);
    }

    private OkHttpClient genericClient(final Context context) {
        File cacheFile = FileUtils.getAppHttpCacheDir(context);
        Cache cache = new Cache(cacheFile, CACHE_SIZE);
        OkHttpClient.Builder build = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .cache(cache)
                .addNetworkInterceptor(new CacheHeadInterceptor(context))
                .addInterceptor(new CharsetInterceptor());

        for(Interceptor interceptor : mInterceptors){
            build.addInterceptor(interceptor);
        }
        OkHttpClient httpClient = build.build();
        return httpClient;
    }
}
