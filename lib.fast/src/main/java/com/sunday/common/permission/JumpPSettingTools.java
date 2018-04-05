package com.sunday.common.permission;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by siwei on 2018/1/4.
 * 权限设置兼容跳转
 */

public class JumpPSettingTools {

    private static String mManu = Build.MANUFACTURER.toUpperCase();
    private static JumpInfo mJumpInfo;


    /**
     * 跳转到权限设置页面
     */
    public static void jumpPermisionSetting(Context context) {
        Intent intent;
        if (mJumpInfo == null) {
            mJumpInfo = getJumpInfo(context);
        }
        intent = createIntent(mJumpInfo);
        try {
            if (!isIntentAvailable(context, intent)) {
                //当Intent无法跳转
                mJumpInfo = getDefaultJumpInfo(context);
                intent = createIntent(mJumpInfo);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            //当适配信息不准确则跳转到Android系统的默认页面
            mJumpInfo = getDefaultJumpInfo(context);
            intent = createIntent(mJumpInfo);
            context.startActivity(intent);
        }
    }

    /**
     * 检测Intent 是否有效
     */
    private static boolean isIntentAvailable(Context context, Intent intent) {
        if (context == null) {
            return false;
        }
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolves = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolves != null && !resolves.isEmpty();
    }

    //根据配置创建intent
    private static Intent createIntent(JumpInfo jumpInfo) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!TextUtils.isEmpty(jumpInfo.action)) {
            intent.setAction(jumpInfo.action);
        }
        if (!TextUtils.isEmpty(jumpInfo.packageName) && !TextUtils.isEmpty(jumpInfo.packageClazz)) {
            ComponentName cName = new ComponentName(jumpInfo.packageName, jumpInfo.packageClazz);
            intent.setComponent(cName);
        }
        if (!TextUtils.isEmpty(jumpInfo.paramsKey)) {
            intent.putExtra(jumpInfo.paramsKey, jumpInfo.paramsValue);
        }
        return intent;
    }

    //初始化各个机型的配置信息
    private static JumpInfo getJumpInfo(Context context) {
        JumpInfo jumpInfo;
        if (mManu.equals(ManuConstans.HUAWEI)) {
            jumpInfo = new JumpInfo(ManuConstans.HUAWEI, null, "com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity", null, null);
        } else if (mManu.equals(ManuConstans.XIAOMI)) {
            jumpInfo = new JumpInfo(ManuConstans.XIAOMI, "miui.intent.action.APP_PERM_EDITOR", null, null, "extra_pkgname", context.getPackageName());
        } else if (mManu.equals(ManuConstans.OPPO)) {
            jumpInfo = new JumpInfo(ManuConstans.OPPO, null, "com.coloros.safecenter", "com.coloros.safecenter.permission.singlepage.PermissionSinglePageActivity", null, null);
        } else if (mManu.equals(ManuConstans.VIVO)) {
            jumpInfo = new VivoJumpInfo(ManuConstans.VIVO);
        } else if (mManu.equals(ManuConstans.MEIZU)) {
            jumpInfo = new JumpInfo(ManuConstans.MEIZU, null, "com.meizu.safe", "com.meizu.safe.security.AppSecActivity", "packageName", context.getPackageName());
        } else if (mManu.equals(ManuConstans.COOLPAD)) {
            jumpInfo = new JumpInfo(ManuConstans.COOLPAD, null, "com.yulong.android.security", "com.yulong.android.security.ui.activity.dataprotection.AppListActivity", "packageName", context.getPackageName());
        } else {
            jumpInfo = new JumpInfo(ManuConstans.DEFAULT, "android.settings.APPLICATION_DETAILS_SETTINGS", null, null, "package", context.getPackageName());
        }
        return jumpInfo;
    }

    private static JumpInfo getDefaultJumpInfo(Context context) {
        return new JumpInfo(ManuConstans.DEFAULT, "android.settings.APPLICATION_DETAILS_SETTINGS", null, null, "package", context.getPackageName());
    }


    //跳转信息
    public static class JumpInfo {

        protected String mark;
        protected String action;
        protected String packageName;
        protected String packageClazz;
        protected String paramsKey;
        protected String paramsValue;

        JumpInfo(String mark) {
            this.mark = mark;
        }

        public JumpInfo(String mark, String action, String packageName, String packagePath, String paramsKey, String paramsValue) {
            this.mark = mark;
            this.action = action;
            this.packageName = packageName;
            this.packageClazz = packagePath;
            this.paramsKey = paramsKey;
            this.paramsValue = paramsValue;
        }

        //读取系统配置信息
        public String readSystemProperties(String name) {
            String value = null;
            try {
                Method getMethod = Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class});
                value = (String) getMethod.invoke(null, new Object[]{name});
            } catch (Exception e) {
            }
            return value;
        }
    }

    //VIVO的跳转信息
    public static class VivoJumpInfo extends JumpInfo {

        VivoJumpInfo(String mark) {
            super(mark);
            initVivoInfo();
        }

        private void initVivoInfo() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                packageName = "com.iqoo.secure";
                packageClazz = "com.iqoo.secure.safeguard.SoftPermissionDetailActivity";
            } else {
                packageName = "com.vivo.permissionmanager";
                packageClazz = "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity";
            }
        }
    }

    //厂商常量
    public class ManuConstans {

        //小米
        public static final String XIAOMI = "XIAOMI";

        //华为
        public static final String HUAWEI = "HUAWEI";

        //魅族
        public static final String MEIZU = "MEIZU";

        //vivo
        public static final String VIVO = "VIVO";

        //oppo
        public static final String OPPO = "OPPO";

        //coolpad
        public static final String COOLPAD = "YULONG";

        //unsupport
        public static final String DEFAULT = "DEFAULT";

    }
}
