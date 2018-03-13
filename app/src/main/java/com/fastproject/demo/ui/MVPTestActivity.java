package com.fastproject.demo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fastproject.demo.R;
import com.sunday.common.activity.BaseMVPActivity;

public class MVPTestActivity extends BaseMVPActivity<TestPresenter> implements View.OnClickListener{

    TextView mMsgTv;

    Button getDataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvptest);
    }

    @Override
    public TestPresenter initPresenter() {
        return new TestPresenter();
    }

    @Override
    protected void initView() {
        super.initView();
        mMsgTv = findViewById(R.id.msg_tv);
        getDataBtn = findViewById(R.id.get_data);
    }

    @Override
    protected void initListener() {
        super.initListener();
        getDataBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_data:
                getPresenter().showMsg();
                break;
        }
    }

    public void showMsgText(String msg){
        mMsgTv.setText(msg);
    }
}
