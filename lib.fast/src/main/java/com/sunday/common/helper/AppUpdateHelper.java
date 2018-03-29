package com.sunday.common.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.sunday.common.utils.FileProvider7;

import java.io.File;

/**
 * Created by siwei on 2018/3/25.
 * 安装app helper
 */

public class AppUpdateHelper {

    public static final int REQ_PERMISSION_CODE_SDCARD = 0X111;

    private Activity mActivity;
    private String apkPath;

    private ImgSetHelper.SetListener mListener;

    public AppUpdateHelper(Activity activity) {
        mActivity = activity;
    }

    public void setListener(ImgSetHelper.SetListener listener) {
        this.mListener = listener;
    }

    public void installApk(String apkPath) {
        this.apkPath = apkPath;
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQ_PERMISSION_CODE_SDCARD);

        } else {
            installApk2();
        }
    }

    private void installApk2() {
        // 需要自己修改安装包路径
        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        FileProvider7.setIntentDataAndType(mActivity, intent, "application/vnd.android.package-archive", file, true);
        mActivity.startActivity(intent);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_PERMISSION_CODE_SDCARD) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                installApk2();
            } else {
                //没有权限
            }
        }
    }

}
