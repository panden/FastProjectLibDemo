package com.sunday.common.utils;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

/**
 * Created by siwei on 2018/3/15.
 * 定位工具类
 */

public class LocationUtils {

    /**判断定位服务是否开启*/
    public static boolean isLocationEnable(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsProviderEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetProviderEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return isGpsProviderEnable || isNetProviderEnable;
    }

    /**跳转到定位服务设置页*/
    public static void goLocationSetting(Context context){
        if(context == null)return;
        Intent intent =  new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

}
