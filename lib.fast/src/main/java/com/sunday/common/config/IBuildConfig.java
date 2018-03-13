package com.sunday.common.config;

import android.content.Context;

/**
 * Created by siwei on 2018/3/12.
 * 基础的项目配置项
 */

public interface IBuildConfig {

    /**
     * 当前是否为debug状态
     */
    boolean isDebug();

    /**
     * 请求的baseUrl
     */
    String getBaseUrl();

    /**
     * 获取当前的版本号
     */
    int getVersionCode(Context context);

    /**
     * 获取当前的版本名
     */
    String getVersionName(Context context);

    /**获取App名称*/
    String getAppName();
}
