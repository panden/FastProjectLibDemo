package com.sunday.common.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.sunday.common.cache.ACache;
import com.sunday.common.config.IBuildConfig;
import com.sunday.common.logger.Logger;
import com.sunday.common.volley.RequestQueue;
import com.sunday.common.volley.toolbox.Volley;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.utils.Log;

import org.litepal.LitePalApplication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by siwei on 2015/11/2.
 */
public abstract class BaseApplication extends LitePalApplication implements Thread.UncaughtExceptionHandler {

    private RequestQueue mRequestQueue;

    private static BaseApplication instance;

    public synchronized static BaseApplication getInstance() {
        return instance;
    }

    private static final String TAG = "BaseApplication";

    /**
     * 返回app配置项
     */
    public abstract @NonNull
    IBuildConfig getBuildConfig();

    @Override
    public void onCreate() {
        super.onCreate();
        onAppInit();
    }

    //App进来进行的一些初始化操作
    private void onAppInit() {
        instance = this;
        Logger.init(getBuildConfig().getAppName()).hideThreadInfo().setMethodCount(3).setMethodOffset(2);
        Thread.setDefaultUncaughtExceptionHandler(this);
        android.util.Log.d(TAG, "BaseApplication onCreate");
    }

    /**
     * 友盟初始化
     *
     * @param umWeixinId     友盟初始化微信的id
     * @param umWeixinSecret 友盟初始化微信的secret
     */
    protected void initUM(String umWeixinId, String umWeixinSecret) {
        // 友盟
        UMShareAPI.get(this);
        Config.isJumptoAppStore = true;
        Log.LOG = false;
        PlatformConfig.setWeixin(umWeixinId, umWeixinSecret);
    }

    @Override
    public void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
    }

    public ACache getCache() {
        return ACache.get(this);
    }

    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .client(genericClient())
                .baseUrl(getInstance().getBuildConfig().getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", "multipart/form-data; charset=UTF-8")
                                .build();
                        return chain.proceed(request);
                    }
                }).build();

        return httpClient;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        if (!getBuildConfig().isDebug()) {
            System.exit(1);
        }
    }

}
