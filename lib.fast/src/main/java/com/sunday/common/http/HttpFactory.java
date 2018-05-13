package com.sunday.common.http;

import android.content.Context;
import android.support.annotation.NonNull;

import com.sunday.common.activity.BaseApplication;
import com.sunday.common.http.intercepts.CharsetInterceptor;
import com.sunday.common.http.intercepts.HttpCodeInterceptor;
import com.sunday.common.http.intercepts.OkHttpCacheHeadInterceptor;
import com.sunday.common.http.intercepts.PostCacheInterceptor;
import com.sunday.common.utils.FileUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by siwei on 2018/3/15.
 */

public class HttpFactory {

    private final String TAG = "OkHttp_Factory ";

    private Interceptor[] mInterceptors;
    private Retrofit mRetrofit;
    private static HttpFactory instance;
    private static final long CACHE_SIZE = 1024 * 1024 * 10;//缓存的文件大小
    private static final int CONNECT_TIMEOUT = 10;//连接超时10s
    private static final int READ_TIMEOUT = 15;//读取超时15s
    private static final int WRITE_TIMEOUT = 20;//写入超时20s

    public static void initFactory(@NonNull Context context, @NonNull String baseUrl) {
        instance = new HttpFactory(context, baseUrl);
    }

    public static void initFactory(@NonNull Context context, @NonNull String baseUrl, Interceptor... interceptors) {
        instance = new HttpFactory(context, baseUrl, interceptors);
    }

    public static HttpFactory instance() {
        if (instance == null)
            throw new NullPointerException("not call HttpFactory.initFactory(context, baseUrl)");
        return instance;
    }

    private HttpFactory(Context context, String baseUrl, Interceptor... interceptors) {
        this.mInterceptors = interceptors;
        if (baseUrl != null && !baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
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

    public <T> T createApiService(Class<T> service) {
        return mRetrofit.create(service);
    }

    private OkHttpClient genericClient(final Context context) {
        File cacheFile = FileUtils.getAppHttpCacheDir(context);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Cache cache = new Cache(cacheFile, CACHE_SIZE);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);

        if (mInterceptors != null) {
            for (Interceptor interceptor : mInterceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        builder.cache(cache)
                .addInterceptor(new HttpCodeInterceptor())//错误码拦截器
                .addInterceptor(new PostCacheInterceptor(context))//post缓存拦截器
                .addInterceptor(new OkHttpCacheHeadInterceptor(context))//get缓存拦截器
                .addInterceptor(new CharsetInterceptor());//编码拦截器
        if(BaseApplication.getInstance().getBuildConfig().isDebug()){
            builder.addInterceptor(loggingInterceptor);
        }
        OkHttpClient httpClient = builder.build();
        return httpClient;
    }
}
