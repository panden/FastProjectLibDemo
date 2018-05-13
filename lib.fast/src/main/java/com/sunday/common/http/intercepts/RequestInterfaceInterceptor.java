package com.sunday.common.http.intercepts;

import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求接口拦截器
 */
public class RequestInterfaceInterceptor implements Interceptor {

    private List<String> requestUrlMatchs;
    private boolean isReverseIntercept;//是否符反向匹配进行拦截

    /**
     * @param requestMatchs 请求接口过滤的正则
     */
    public RequestInterfaceInterceptor(List<String> requestMatchs) {
        requestUrlMatchs = new ArrayList<>(requestMatchs);
    }

    /**
     * @param requestMatchs 请求接口过滤的正则
     */
    public RequestInterfaceInterceptor(String... requestMatchs) {
        requestUrlMatchs = Arrays.asList(requestMatchs);
    }

    /**
     * 是否进行反向拦截
     *
     * @param isIntercept true:正则匹配失败进行拦截;false:正则匹配成功进行拦截
     */
    public void isReverseIntercept(boolean isIntercept) {
        this.isReverseIntercept = isIntercept;
    }

    /**
     * @param requestMatch 请求接口过滤的正则
     */
    public void addRequestMatchs(String requestMatch) {
        requestUrlMatchs.add(requestMatch);
    }

    //是否进行拦截
    private boolean isIntercept(Chain chain) {
        if (chain == null) return false;
        Request request = chain.request();
        boolean intercept = false;
        String requestUrl = request.url().toString();
        for (String matchsStr : requestUrlMatchs) {
            if (TextUtils.isEmpty(matchsStr)) continue;
            intercept = requestUrl.matches(matchsStr) || !isReverseIntercept;
            if (intercept) break;
        }
        return intercept;
    }

    @Override
    public final Response intercept(Chain chain) throws IOException {
        if (isIntercept(chain)) {
            //进行拦截
            return onIntercept(chain);
        } else {
            //不进行拦截
            Request request = chain.request();
            return chain.proceed(request);
        }
    }

    /**
     * 接口被拦截,覆盖实现
     */
    public Response onIntercept(Chain chain) throws IOException {
        Request request = chain.request();
        return chain.proceed(request);
    }

    ;

    public static class Builder {

        private List<String> requestMatchs;

        public Builder() {
            requestMatchs = new ArrayList<>();
        }

        public Builder addUrlMatchs(String matchs) {
            requestMatchs.add(matchs);
            return this;
        }

        public <T extends RequestInterfaceInterceptor> T build() {
            return (T) new RequestInterfaceInterceptor(requestMatchs);
        }

    }
}
