package com.sunday.common.http.exception;

import java.io.IOException;

/**
 * Created by siwei on 2018/5/6.
 * 无缓存异常，当未读取到该缓存的时候（http返回504）会抛出
 */
public class GatewayTimeoutException extends IOException {

    public GatewayTimeoutException(){
        super("response code:504, the cache not find");
    }
}
