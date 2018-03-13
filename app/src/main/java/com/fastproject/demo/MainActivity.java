package com.fastproject.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fastproject.demo.ui.MVPTestActivity;
import com.sunday.common.activity.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    Button mMvpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        mMvpBtn=findViewById(R.id.mvp_btn);
    }

    @Override
    protected void initListener() {
        mMvpBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mvp_btn:
                openActivity(MVPTestActivity.class);
                break;
        }
    }
}
