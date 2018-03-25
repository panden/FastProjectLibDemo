package com.sunday.common.utils;

import android.content.Context;

import java.util.Locale;

/**
 * Created by siwei on 2018/3/26.
 * https://blog.csdn.net/sinat_35721133/article/details/78327111
 * https://www.cnblogs.com/travellife/p/Android-ying-yong-nei-duo-yu-yan-qie-huan.html
 */

public class LanguageUtils {

    /**判断当前语言是否为中文*/
    public static boolean isChinese(Context context){
        Locale local=context.getResources().getConfiguration().locale;
        if(local.getLanguage().toLowerCase().endsWith("zh")){//中文
            return true;
        }
        return false;
    }
}
