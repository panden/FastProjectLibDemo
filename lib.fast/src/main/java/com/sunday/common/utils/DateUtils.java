package com.sunday.common.utils;

import java.text.SimpleDateFormat;

/**
 * Created by siwei on 2018/5/8.
 * 时间工具类
 */

public class DateUtils {

    /**获取年月日时分秒的格式化
     * @param ym 年月的分隔符
     * @param md 月日的分隔符
     * @param dh 日时的分隔符
     * @param hm 时分的分隔符
     * @param ms 分秒的分隔符
     * */
    public static SimpleDateFormat getYMDHMSFormatter(String ym, String md, String dh, String hm, String ms){
        String formatStr = String.format("yyyy%sMM%sdd%sHH%smm%sSS", ym, md, dh, hm, ms);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);
        return simpleDateFormat;
    }

    /**获取年月日的格式化
     * @param ym 年月的分隔符
     * @param md 月日的分隔符
     * */
    public static SimpleDateFormat getYMDFormatter(String ym, String md){
        String formatStr = String.format("yyyy%sMM%sdd", ym, md);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);
        return simpleDateFormat;
    }

    /**获取时分秒的格式化
     * @param hm 时分的分隔符
     * @param ms 分秒的分隔符
     * */
    public static SimpleDateFormat getHMSFormatter(String hm, String ms){
        String formatStr = String.format("HH%smm%sSS", hm, ms);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);
        return simpleDateFormat;
    }
}
