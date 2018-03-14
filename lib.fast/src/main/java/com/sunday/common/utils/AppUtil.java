package com.sunday.common.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.util.List;


public class AppUtil {

    /**
     * 获取当前程序的版本号
     */
    public static int getVersionCode(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager;
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        int code = 0;
        try {
            packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            code = packInfo.versionCode;
        } catch (Exception e) {
        }
        return code;
    }

    /**
     * 获取当前程序的版本名
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager;
        String name = "";
        try {
            packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            name = packInfo.versionName;
        } catch (Exception e) {
        }
        return name;
    }

    /**
     * 获取当前程序的版本信息
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return packInfo;
    }

    /**
     * app是否在后台运行
     */
    public static boolean isRunInBackground(Context context) {
        boolean result = !isScreenOn(context) || isTaskBackground(context);
        return result;
    }

    /**
     * 是否锁屏
     */
    public static boolean isScreenOn(Context c) {
        PowerManager pm = (PowerManager) c.getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }

    /**
     * 是否在栈顶
     */
    private static boolean isTaskBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        for (RunningTaskInfo task : tasks) {
            if (task.numRunning == 0 || !context.getPackageName().equals(task.baseActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通知栏的高度获取
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
        }
        return statusBarHeight;
    }

    /**
     * 获取资源文件中的字符
     */
    public static String getStringFromResource(Context context, int id) {
        if (null == context || null == context.getResources().getString(id))
            return "";
        else
            return context.getResources().getString(id);
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }


    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 检测Intent 是否有效
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        if (context == null) {
            return false;
        }
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolves = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolves != null && !resolves.isEmpty();
    }

    /**
     * 跳转到权限设置界面
     */
    public static void getAppDetailSettingIntent(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }
}

