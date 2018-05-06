package com.sunday.common.http.exception;

import java.io.IOException;

/**
 * Created by siwei on 2018/5/6.
 * 无缓存异常，当未读取到该缓存的时候（http返回504）会抛出
 */
public class NoneCacheException extends IOException {

    public NoneCacheException(){
        super("Unread cached content, http code 504");
    }
}
