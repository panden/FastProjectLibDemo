package com.sunday.common.http.intercepts;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sunday.common.http.cache.CacheType;
import com.sunday.common.http.cache.Catch.CacheManager;
import com.sunday.common.logger.Logger;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpDate;

import static okhttp3.internal.Util.UTC;

/**
 * 字符串的缓存类
 * Created by xiaolei on 2017/12/9.
 */
public class PostCacheInterceptor implements Interceptor {

    private static final String TAG = "OkHttp_PostCacheInterceptor ";
    private static final int MOBILE_ENABLE_MAX_AGE = 6 * 60 * 60;//有网络时 设置缓存超时时间6个小时
    private static final int MOBILE_UNENABLE_MAX_STALE = 30 * 60 * 60 * 24;//无网络时，设置超时为30天

    /**
     * Most websites serve cookies in the blessed format. Eagerly create the parser to ensure such
     * cookies are on the fast path.
     */
    private static final ThreadLocal<DateFormat> STANDARD_DATE_FORMAT =
            new ThreadLocal<DateFormat>() {
                @Override
                protected DateFormat initialValue() {
                    // RFC 2616 specified: RFC 822, updated by RFC 1123 format with fixed GMT.
                    DateFormat rfc1123 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
                    rfc1123.setLenient(false);
                    rfc1123.setTimeZone(UTC);
                    return rfc1123;
                }
            };

    private long oldTime;

    private Context context;

    public PostCacheInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        //只缓存post请求
        if ("POST".equalsIgnoreCase(request.method())) {
            //设置过缓存头信息,就去缓存
            if (requestHasCache(request) || requestHasCacheContral(request)) {
                oldTime = System.currentTimeMillis();
                try {
                    response = chain.proceed(request);
                    // 只有在网络请求返回成功之后，才进行缓存处理，否则，404存进缓存，岂不笑话
                    if (response.isSuccessful()) {
                        response = addOnlineCacheHead(response);
                        response = cacheAndNewResponse(request, response);
                        return response;
                    } else {
                        return chain.proceed(request);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.d(TAG + "PostCacheInterceptor exception:%s", e.getMessage());
                    response = tryGetCacheResponse(request, response);
                    if (response == null) {
                        return chain.proceed(request);
                    } else {
                        return response;
                    }
                }
            } else {
                return chain.proceed(request);
            }
        } else {
            return chain.proceed(request);
        }
    }

    //缓存并且new一个新的response返回
    // response一旦调用string()之后,流信息被读取出来了,后续的就读不到返回的信息且会抛出异常,因此需要new一个新的response去继续传递流信息
    private Response cacheAndNewResponse(Request request, Response response) throws IOException {
        ResponseBody responseBody = response.body();
        String responseBodyStr = responseBody.string();
        responseBodyStr = responseBodyStr == null ? "" : responseBodyStr;
        String responseHeadsStr = getResponseHeads(response);
        cacheResponse(request, responseBodyStr);
        cacheResponseHead(request, responseHeadsStr);
        return getNewResponse(response, responseBodyStr);
    }

    /**
     * 添加离线缓存请求头,如果响应头已带有only-if-cached就不会添加任何响应头
     */
    private Response addDelineCacheHead(Response response) {
        if (response == null) return response;
        response = cleanPragmaHead(response);
        Response.Builder builder = response.newBuilder();
        if (!responseOnlyCache(response)) {
            Logger.d(TAG + " add deline cache head");
            builder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + MOBILE_UNENABLE_MAX_STALE).build();
        }
        return builder.build();
    }

    /**
     * 添加在线缓存响应头
     */
    private Response addOnlineCacheHead(Response response) {
        if (response == null) return response;
        response = cleanPragmaHead(response);
        Response.Builder builder = response.newBuilder();
        if (!responseHasCacheContral(response)) {
            Logger.d(TAG + "add online cache head");
            builder.addHeader("Cache-Control", "public, max-age=" + MOBILE_ENABLE_MAX_AGE);
        }
        if (!responseHasDate(response)) {//如果未添加响应时间
            builder.addHeader("Date", STANDARD_DATE_FORMAT.get().format(new Date()));
        }
        return builder.build();
    }

    /**
     * 尝试去获取缓存数据,要是没有缓存数据,则返回传入的响应
     */
    private synchronized Response tryGetCacheResponse(Request request, Response chainResponse) throws IOException {
        Response cacheResponse = getCacheResponse(request);
        Headers headers = getCacheHeaders(request);
        if (cacheResponse == null) {//未读取到缓存信息
            return chainResponse;
        } else {//已读取到缓存信息
            //添加离线缓存头信息
            cacheResponse = cacheResponse.newBuilder().headers(headers).build();
            //检查响应数据是否超时
            cacheResponse = checkResponseTimeout(request, cacheResponse);
            if (cacheResponse.code() != 504) {//数据未超时
                if (!responseOnlyCache(cacheResponse)) {
                    //缓存响应中已带有only-if-cached缓存头信息,则之前已经缓存过一次了,就不在缓存,否则进行缓存
                    Logger.d(TAG + "not find only-if-cached head and cache response");
                    cacheResponse = addDelineCacheHead(cacheResponse);
                    cacheResponse = cacheAndNewResponse(request, cacheResponse);
                }
            }
            return cacheResponse;
        }
    }

    /**
     * 检查响应数据是否超时
     */
    private Response checkResponseTimeout(Request request, Response response) {
        String dateHead = response.header("Date");
        if (TextUtils.isEmpty(dateHead)) {
            //未配置从服务器相应数据的时间
            return response;
        } else {
            Date date = HttpDate.parse(dateHead);
            long timeout = responseOnlyCache(response) ? MOBILE_UNENABLE_MAX_STALE : MOBILE_ENABLE_MAX_AGE;
            if (System.currentTimeMillis() > date.getTime() + timeout) {//数据超时,返回504
                Logger.d(TAG + "response gateway timeout, remove body and head cache.");
                CacheManager.getInstance(context).removeCache(getCacheResponseKey(request));
                CacheManager.getInstance(context).removeCache(getCacheHeadKey(request));
                return new Response.Builder()
                        .request(request)
                        .protocol(Protocol.HTTP_1_1)
                        .code(504)
                        .message("Unsatisfiable Request (only-if-cached)")
                        .body(Util.EMPTY_RESPONSE)
                        .sentRequestAtMillis(-1L)
                        .receivedResponseAtMillis(System.currentTimeMillis())
                        .build();
            } else {//数据未超时,正常返回
                return response;
            }
        }
    }

    /**
     * 判断响应是否添加了Cache-Control请求头
     */
    private boolean responseHasCacheContral(Response response) {
        if (response == null) return false;
        String cacheContralHead = response.header("Cache-Control");
        return !TextUtils.isEmpty(cacheContralHead);
    }

    /**
     * 判断响应是否添加了Date请求头
     */
    private boolean responseHasDate(Response response) {
        if (response == null) return false;
        String cacheContralHead = response.header("Date");
        return !TextUtils.isEmpty(cacheContralHead);
    }

    /**
     * 判断响应是否添加了only-if-cached请求头
     */
    private boolean responseOnlyCache(Response response) {
        if (response == null) return false;
        String cacheContralHead = response.header("Cache-Control", "");
        boolean onlyCache = cacheContralHead.indexOf("only-if-cached") >= 0;
        return onlyCache;
    }

    /**
     * 清除Pragma头信息
     */
    private Response cleanPragmaHead(Response response) {
        if (response == null) return response;
        String pragmaHead = response.header("Pragma");
        if (!TextUtils.isEmpty(pragmaHead)) {
            response = response.newBuilder().removeHeader("Pragma").build();
        }
        return response;
    }

    /**
     * 判断响应是否添加了cache请求头
     */
    private boolean responseHasCache(Response response) {
        if (response == null) return false;
        String cacheHead = response.header("cache");
        return !TextUtils.isEmpty(cacheHead);
    }

    /**
     * 判断请求是否添加了cache请求头
     */
    private boolean requestHasCache(Request request) {
        if (request == null) return false;
        String cacheHead = request.header("cache");
        return "true".equalsIgnoreCase(cacheHead);
    }

    /**
     * 判断请求是否添加了Cache-Control请求头
     */
    private boolean requestHasCacheContral(Request request) {
        if (request == null) return false;
        String cache_control = request.header("Cache-Control");
        return !TextUtils.isEmpty(cache_control);
    }

    /**
     * 缓存响应
     */
    private void cacheResponse(Request request, String responseBody) throws IOException {
        if (request == null || responseBody == null) return;
        String url = request.url().url().toString();
        String responBodyStr = TextUtils.isEmpty(responseBody) ? "" : responseBody;
        //存缓存，以链接+参数进行MD5编码为KEY存
        CacheManager.getInstance(context).setCache(getCacheResponseKey(request), responBodyStr);
        Logger.i(TAG + "--> Push Body Cache: %s :Success", url);
    }


    /**
     * 读取缓存响应
     */
    private Response getCacheResponse(Request request) {
        Logger.i(TAG + "--> Try to Get Body Cache --------");
        String url = request.url().url().toString();
        String params = getPostParams(request);
        String cacheStr = CacheManager.getInstance(context).getCache(getCacheResponseKey(request));//取缓存，以链接+参数进行MD5编码为KEY取
        if (cacheStr == null) {
            Logger.i(TAG + "<-- Get Body Cache Failure --------");
            return null;
        }
        Response response = new Response.Builder()
                .code(200)
                .body(ResponseBody.create(null, cacheStr))
                .request(request)
                .message(CacheType.DISK_CACHE)
                .protocol(Protocol.HTTP_1_0)
                .build();
        long useTime = System.currentTimeMillis() - oldTime;
        Logger.i(TAG + "<-- Get Body Cache: code:%s message:%s url:%s (%sms)", response.code(), response.message(), url, useTime);
        Logger.i(TAG + cacheStr);
        return response;
    }

    /**
     * 获取缓存的请求体的key
     */
    private String getCacheResponseKey(Request request) {
        String url = request.url().url().toString();
        String params = getPostParams(request);
        String responseKey = CacheManager.encryptMD5(url + params);
        return CacheManager.encryptMD5(responseKey);
    }

    /**
     * 缓存请求头
     */
    private void cacheResponseHead(Request request, String responseHead) throws IOException {
        if (request == null || responseHead == null) return;
        String url = request.url().url().toString();
        CacheManager.getInstance(context).setCache(getCacheHeadKey(request), responseHead);
        Logger.i(TAG + "--> Push Head Cache: %s :Success", url);
    }

    /**
     * 读取缓存的请求头
     */
    private Headers getCacheHeaders(Request request) {
        Logger.i(TAG + "--> Try to Get Cache Headers   --------");
        if (request == null) return new Headers.Builder().build();
        String headersJson = CacheManager.getInstance(context).getCache(getCacheHeadKey(request));
        String url = request.url().url().toString();
        if (headersJson == null) {
            Logger.i(TAG + "<-- Get Headers Cache Failure --------");
        } else {
            Logger.i(TAG + "<-- Get Headers Cache url:%s", url);
            Logger.i(TAG + new String(headersJson));
        }
        return getResponseHeadsByJson(headersJson);
    }

    /**
     * 拼接处一个缓存的key,并计算md5
     */
    private String getCacheHeadKey(Request request) {
        String url = request.url().url().toString();
        String requestParamsStr = getPostParams(request);
        String headKey = url + requestParamsStr + "_response_heads";
        return CacheManager.encryptMD5(headKey);
    }

    /**
     * 构建一个新的响应
     */
    private Response getNewResponse(Response response, String body) {
        ResponseBody responseBody = response.body();
        return response.newBuilder()
                .code(response.code())
                .body(ResponseBody.create(responseBody == null ? null : responseBody.contentType(), body))
                .build();
    }

    /**
     * 获取在Post方式下。向服务器发送的参数
     *
     * @param request
     * @return
     */
    private String getPostParams(Request request) {
        String reqBodyStr = "";
        String method = request.method();
        if ("POST".equalsIgnoreCase(method)) // 如果是Post，则尽可能解析每个参数
        {
            StringBuilder sb = new StringBuilder();
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                if (body != null) {
                    for (int i = 0; i < body.size(); i++) {
                        sb.append(body.encodedName(i)).append("=").append(body.encodedValue(i)).append(",");
                    }
                    sb.delete(sb.length() - 1, sb.length());
                }
                reqBodyStr = sb.toString();
                sb.delete(0, sb.length());
            }
        }
        return reqBodyStr;
    }

    /**
     * 获取相应的头字符串
     */
    private String getResponseHeads(Response response) {
        String responseHeadStr = "";
        if (response == null) return responseHeadStr;
        Headers headers = response.headers();
        try {
            if (headers != null) {
                Field field = headers.getClass().getDeclaredField("namesAndValues");
                field.setAccessible(true);
                responseHeadStr = new Gson().toJson(field.get(headers));
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return responseHeadStr;
    }

    /**
     * 获取相应的头信息
     */
    private Headers getResponseHeadsByJson(String headsJson) {
        Headers headers = new Headers.Builder().build();
        if (!TextUtils.isEmpty(headsJson)) {
            try {
                String[] namesAndValues = new Gson().fromJson(headsJson, String[].class);
                Constructor<Headers> headersConstructor = Headers.class.getDeclaredConstructor(String[].class);
                headersConstructor.setAccessible(true);
                headers = headersConstructor.newInstance(new Object[]{namesAndValues});
                Logger.i(TAG + "<-- newInstance Headers Cache-Control:%s", headers.get("Cache-Control"));
            } catch (Exception e) {
                e.printStackTrace();
                Logger.i(TAG + "<-- newInstance Headers Exception:%s", e.getMessage());
            }
        }
        return headers;
    }

}
