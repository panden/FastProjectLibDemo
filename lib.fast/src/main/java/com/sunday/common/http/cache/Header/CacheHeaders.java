package com.sunday.common.http.cache.Header;


/**
 * 所有请求的缓存头
 * Created by xiaolei on 2017/12/9.
 */
public class CacheHeaders {
    // 自己设置的一个标签
    public static final String NORMAL = "cache:true";
    // 客户端可以缓存
    public static final String PRIVATE = "Cache-Control:private";
    // 客户端和代理服务器都可缓存（前端的同学，可以认为public和private是一样的）
    private static final String MAX_AGE = "Cache-Control:max-age=%s";
    // 缓存的内容将在 xxx 秒后失效
    public static final String NO_CACHE = "Cache-Control:no-cache";
    // 需要使用对比缓存来验证缓存数据（后面介绍）
    public static final String PUBLIC = "Cache-Control:public";
    // 所有内容都不会缓存，强制缓存，对比缓存都不会触发（对于前端开发来说，缓存越多越好，so...基本上和它说886）
    public static final String NO_STORE = "Cache-Control:no-store";

    public static String getMaxAge(long time){
        return String.format(MAX_AGE, time);
    }
}
