package com.fastproject.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.fastproject.demo.R;

/**
 * Created by siwei on 2018/4/8.
 */

public class ScrollLinearLayout extends ScrollView {

    private View mHeadView, mBodyView;
    private int headId, bodyId;
    private GestureDetector mGestureDetector;

    public ScrollLinearLayout(Context context) {
        this(context, null);
    }

    public ScrollLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs){
        if(attrs == null)return;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ScrollLinearLayout);
        headId = array.getResourceId(R.styleable.ScrollLinearLayout_head_id, -1);
        bodyId = array.getResourceId(R.styleable.ScrollLinearLayout_body_id, -1);
        System.out.println("ScrollLinearLayout headId="+headId);
        array.recycle();
        mGestureDetector = new GestureDetector(context, mOnGestureListener);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(headId > 0){
            mHeadView = findViewById(headId);
        }
        if(bodyId > 0){
            mBodyView = findViewById(bodyId);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    final GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            System.out.println("ScrollLinearLayout onDown");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            System.out.println("ScrollLinearLayout onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            System.out.println("ScrollLinearLayout onSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            System.out.println("ScrollLinearLayout onScroll");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            System.out.println("ScrollLinearLayout onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            System.out.println("ScrollLinearLayout onFling");
            return false;
        }
    };
}
