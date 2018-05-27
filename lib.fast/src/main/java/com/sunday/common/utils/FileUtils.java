package com.sunday.common.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by siwei on 2018/3/15.
 */

public class FileUtils {

    public static final String HTTP_CACHE_NAME="httpcache";

    public static final String IMG_CACHE_NAME="imgcache";

    /**获取app的缓存目录*/
    public static File getAppCacheDir(Context context, String fileName){
        if(context == null) return null;
        File file = new File(hasSdCard(context) ? context.getExternalCacheDir() : context.getCacheDir(), fileName);
        if(!file.exists())file.mkdirs();
        return file;
    }

    /**获取Http缓存目录*/
    public static File getAppHttpCacheDir(Context context){
        return getAppCacheDir(context, HTTP_CACHE_NAME);
    }

    /**获取图片缓存目录*/
    public static File getAppImgCacheDir(Context context){
        return getAppCacheDir(context, IMG_CACHE_NAME);
    }

    /**获取文件存放目录*/
    public static File getAppFileDir(Context context){
        return getAppFileDir(context, null);
    }

    /**获取文件存放目录*/
    public static File getAppFileDir(Context context, String fileName){
        if(context == null)return null;
        if(TextUtils.isEmpty(fileName))fileName="";
        File file = new File(hasSdCard(context) ? context.getExternalFilesDir(null) : context.getExternalFilesDir(null), fileName);
        if(!file.exists())file.mkdirs();
        return file;
    }


    /**判断是否有sd卡*/
    public static boolean hasSdCard(Context context){
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable();
    }

}
