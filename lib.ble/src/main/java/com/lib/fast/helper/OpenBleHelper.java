package com.lib.fast.helper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.List;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class OpenBleHelper {

    private static final String TAG = "OpenBleHelper";
    private static final boolean DEBUG = true;

    private static final int REQUEST_CODE_ENABLE_BLE = 0X330;
    private static final int ON_REQUEST_OPEN_BLE_DELAY_CHECK = 6 * 1000;

    private FragmentActivity mActivity;
    private BleHelperFragment mBleHelperFragment;
    private BluetoothAdapter bluetoothAdapter;
    private BleStatusBroadCastReciver mReciver;
    private OnBleStatusChangedListener mListener;
    private Handler mMainHandler;

    public OpenBleHelper(FragmentActivity activity) {
        mActivity = activity;
        init();
    }

    private void init() {
        mBleHelperFragment = getBleFragment(mActivity);
        mBleHelperFragment.setBleHelper(this);
        BluetoothManager bluetoothManager = (BluetoothManager) mActivity.getApplicationContext()
                .getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        mReciver = new BleStatusBroadCastReciver(mActivity, this);
        mReciver.regiseter();
        mMainHandler = new Handler();
    }

    private BleHelperFragment getBleFragment(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        BleHelperFragment fragment = (BleHelperFragment) fragmentManager.findFragmentByTag(BleHelperFragment.TAG);
        if (fragment == null) {
            fragment = new BleHelperFragment();
            fragmentManager.beginTransaction().add(fragment, BleHelperFragment.TAG).commit();
        }
        return fragment;
    }

    public void setListener(OnBleStatusChangedListener listener) {
        mListener = listener;
    }

    /**
     * 开启蓝牙
     */
    public void openBle() {
        if (isSupportBle()) {
            if (isBleEnable()) {
                dispatchOnBleOpened();
            } else {
                requestOpenBle();
            }
        } else {
            dispatchOnBleClosed();
        }
    }

    /**
     * 判断当前是否支持蓝牙
     */
    public boolean isSupportBle() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
                && mActivity.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    /**
     * 判断蓝牙是否已开启
     */
    public boolean isBleEnable() {
        if (bluetoothAdapter != null) {
            return bluetoothAdapter.isEnabled();
        }
        return false;
    }

    //通过intent的方式去请求开启蓝牙
    private void requestOpenBle() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(mActivity, intent)) {
            mBleHelperFragment.startActivityForResult(intent, REQUEST_CODE_ENABLE_BLE);
        } else {
            dispatchOnBleClosed();
        }
    }

    //检测Intent 是否有效
    private boolean isIntentAvailable(Context context, Intent intent) {
        if (context == null) {
            return false;
        }
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolves = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolves != null && !resolves.isEmpty();
    }

    //主线程中传递蓝牙已开启回调
    private void dispatchOnBleOpened() {
        if (mListener != null && mMainHandler != null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onBleOpened();
                }
            });
        }
    }

    //主线程中传递蓝牙已关闭回调
    private void dispatchOnBleClosed() {
        if (mListener != null && mMainHandler != null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onBleClosed();
                }
            });
        }
    }

    /**
     * 系统弹窗申请开启蓝牙之后，延时一段时间检查蓝牙的开启状态，避免用户点击拒绝而无响应
     */
    private void onRequestResultDelayCheckBleStatus() {
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isBleEnable()) {
                        dispatchOnBleOpened();
                    } else {
                        dispatchOnBleClosed();
                    }
                }
            }, ON_REQUEST_OPEN_BLE_DELAY_CHECK);
        }
    }

    /**
     * 移除延迟检测
     */
    private void removeOnRequestResultDelayCheckBleStatus() {
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
        }
    }

    protected void release() {
        if (mBleHelperFragment != null) {
            mBleHelperFragment.release();
            mBleHelperFragment = null;
        }
        if (mReciver != null) {
            mReciver.release();
            mReciver = null;
        }
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
        mListener = null;
        bluetoothAdapter = null;
        mActivity = null;
    }

    @SuppressLint("ValidFragment")
    public static class BleHelperFragment extends Fragment {

        private static final String TAG = "com.lib.fast.helper.OpenBleHelper.BleHelperFragment";

        OpenBleHelper mBleHelper;

        private void setBleHelper(OpenBleHelper helper) {
            mBleHelper = helper;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CODE_ENABLE_BLE) {
                d("use request open ble, activity result");
                mBleHelper.onRequestResultDelayCheckBleStatus();
            }
        }

        private void release() {
            mBleHelper = null;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mBleHelper.release();
        }
    }

    //监听蓝牙开启状态广播
    private class BleStatusBroadCastReciver extends BroadcastReceiver {

        private Context mContext;
        private OpenBleHelper mOpenBleHelper;
        private boolean isRegisted;
        private String[] boradcasts = new String[]{BluetoothAdapter.ACTION_STATE_CHANGED};

        private BleStatusBroadCastReciver(Context context, OpenBleHelper openBleHelper) {
            mContext = context.getApplicationContext();
            mOpenBleHelper = openBleHelper;
        }

        public void regiseter() {
            if (!isRegisted) {
                IntentFilter intentFilter = new IntentFilter();
                for (String action : boradcasts) {
                    intentFilter.addAction(action);
                }
                mContext.registerReceiver(this, intentFilter);
                isRegisted = true;
            }
        }

        public void unRegister() {
            if (isRegisted) {
                mContext.unregisterReceiver(this);
                isRegisted = false;
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                mOpenBleHelper.removeOnRequestResultDelayCheckBleStatus();
                int bleStatus = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                if (bleStatus == BluetoothAdapter.STATE_ON) {
                    mOpenBleHelper.dispatchOnBleOpened();
                } else if (bleStatus == BluetoothAdapter.STATE_OFF) {
                    mOpenBleHelper.dispatchOnBleClosed();
                }
            }
        }

        public void release() {
            unRegister();
            mOpenBleHelper = null;
            mContext = null;
        }
    }

    private static void d(String msg, Object... args) {
        if (DEBUG) {
            Log.d(TAG, String.format(msg, args));
        }
    }

    public interface OnBleStatusChangedListener {

        /**
         * 蓝牙已开启
         */
        void onBleOpened();

        /**
         * 蓝牙已关闭或者开启失败
         */
        void onBleClosed();
    }

}
