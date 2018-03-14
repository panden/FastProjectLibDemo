package com.fastproject.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fastproject.demo.ui.MVPTestActivity;
import com.sunday.common.activity.BaseActivity;
import com.sunday.common.qrcode.CaptureActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    Button mMvpBtn, mQrcodebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        mMvpBtn=findViewById(R.id.mvp_btn);
        mQrcodebtn=findViewById(R.id.qrcode_btn);
    }

    @Override
    protected void initListener() {
        mMvpBtn.setOnClickListener(this);
        mQrcodebtn.setOnClickListener(this);
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
                openActivity(CaptureActivity.class);
                break;

        }
    }
}
