package com.fastproject.demo;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fastproject.demo.ui.MVPTestActivity;
import com.fastproject.demo.ui.NumTextSwitcherActivity;
import com.fastproject.demo.ui.TestGeneratectivity;
import com.fastproject.demo.ui.TestScanActivity;
import com.sunday.common.activity.BaseActivity;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks{

    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    Button mMvpBtn, mQrcodebtn, mGQrcodebtn, mNumBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        mMvpBtn=findViewById(R.id.mvp_btn);
        mQrcodebtn=findViewById(R.id.qrcode_btn);
        mGQrcodebtn=findViewById(R.id.gqrcode_btn);
        mNumBtn=findViewById(R.id.num_btn);
    }

    @Override
    protected void initListener() {
        mMvpBtn.setOnClickListener(this);
        mQrcodebtn.setOnClickListener(this);
        mGQrcodebtn.setOnClickListener(this);
        mNumBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        getTitleBar().setTitle("Fast Project");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mvp_btn:
                openActivity(MVPTestActivity.class);
                break;
            case R.id.qrcode_btn:
                openActivity(TestScanActivity.class);
                break;
            case R.id.gqrcode_btn:
                openActivity(TestGeneratectivity.class);
                break;
            case R.id.num_btn:
                openActivity(NumTextSwitcherActivity.class);
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }
}
