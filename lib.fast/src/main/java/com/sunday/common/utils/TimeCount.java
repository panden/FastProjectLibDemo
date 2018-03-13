package com.sunday.common.utils;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * Created by Wangran on 2015/7/14.
 */
public class TimeCount extends CountDownTimer {
    private Button getCodeBtn;
    public TimeCount(long millisInFuture, long countDownInterval, Button getCodeBtn) {
        super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        this.getCodeBtn = getCodeBtn;
    }

    @Override
    public void onFinish() {//计时完毕时触发
        getCodeBtn.setText("重新发送");
        getCodeBtn.setClickable(true);
        getCodeBtn.setTag(1);
    }

    @Override
    public void onTick(long millisUntilFinished) {//计时过程显示
        getCodeBtn.setClickable(false);
        getCodeBtn.setText(millisUntilFinished / 1000 + "秒");
    }
}
