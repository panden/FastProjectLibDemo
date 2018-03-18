package com.libwidget.widget.image;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Checkable;

/**
 * Created by siwei on 2018/3/18.
 * 支持checked属性配置，让imageview根据check状态去展示不同的图片
 */

public class CheckImageView extends android.support.v7.widget.AppCompatImageView implements Checkable {

    private boolean mChecked;
    int[] CHECKED_STATE_SET = { android.R.attr.state_checked };

    public CheckImageView(Context context) {
        super(context);
    }

    public CheckImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        int[] drawableStatus = super.onCreateDrawableState(extraSpace+1);
        if(isChecked()){
            mergeDrawableStates(drawableStatus, CHECKED_STATE_SET);
        }
        return drawableStatus;
    }

    @Override
    public void setChecked(boolean checked) {
        if(mChecked != checked){
            mChecked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
}
