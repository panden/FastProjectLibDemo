package com.fastproject.demo.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fastproject.demo.R;
import com.sunday.common.activity.BaseMVPActivity;
import com.sunday.common.activity.view.NavBar;

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
    protected void initData() {
        super.initData();
        getTitleBar().setTitle("MVP测试");
        getTitleBar().showRightIcon();
        getTitleBar().setRightImageResource(R.drawable.ic_close_normal);
    }

    @Override
    protected void initListener() {
        super.initListener();
        getDataBtn.setOnClickListener(this);
        getTitleBar().setClickListener(mNavBarOnClickListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_data:
                getPresenter().showMsg();
                break;
        }
    }

    NavBar.NavBarOnClickListener mNavBarOnClickListener = new NavBar.NavBarOnClickListener() {
        @Override
        public void onLeftIconClick(View view) {
            back();
        }

        @Override
        public void onLeftSenIconClick(View view) {

        }

        @Override
        public void onRightIconClick(View view) {
            back();
        }

        @Override
        public void onRightTxtClick(View view) {

        }
    };

    public void showMsgText(String msg){
        mMsgTv.setText(msg);
    }
}
