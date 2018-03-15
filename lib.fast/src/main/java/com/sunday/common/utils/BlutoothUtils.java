package com.sunday.common.utils;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;

/**
 * Created by siwei on 2018/3/15.
 * 蓝牙工具类
 */

public class BlutoothUtils {

    public static boolean isSupportBlutooth(){
        return BluetoothAdapter.getDefaultAdapter() != null;
    }

    @SuppressLint("MissingPermission")
    public static boolean isBlutoothEnable(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null){
            return bluetoothAdapter.isEnabled();
        }
        return false;
    }
}
