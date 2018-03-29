package com.fastproject.demo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fastproject.demo.R;

public class NumTextSwitcherActivity extends AppCompatActivity {


    private NumTextSwitcher mNumText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_text_switcher);
        initView();
    }

    private void initView(){
        mNumText = findViewById(R.id.num_text);
        mNumText.setText(String.valueOf(4));
    }


}
