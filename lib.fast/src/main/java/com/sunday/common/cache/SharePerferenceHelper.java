package com.sunday.common.cache;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.sunday.common.activity.BaseApplication;


/**
 *

 */
public class SharePerferenceHelper {
    private SharedPreferences mSharedPreferences;

    private SharePerferenceHelper(Context context, String name) {
        mSharedPreferences = context.getSharedPreferences(name,
                Activity.MODE_PRIVATE);
    }

    public static SharePerferenceHelper createSharePerference(String name) {
        return new SharePerferenceHelper(BaseApplication.getInstance(), name);
    }

    public SharedPreferences getSp(){
        return mSharedPreferences;
    }

    public void putBoolean(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public void putInt(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public void putString(String key, String value) {
        mSharedPreferences.edit().putString(key, value).commit();
    }

    public String getString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public void putLong(String key, long value) {
        mSharedPreferences.edit().putLong(key, value).commit();
    }

    public long getLong(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    public void removeKey(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }



}
