package com.sunday.common.helper;

import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * 使用ViewPager结合Fragment使用的时候，当Fragment被切走了会执行对应的回调<br/>
 * 主要是防止Fragment切走了，需要界面或者后台一些操作暂停，类似生命周期的onPause的效果，方便Fragment优化其代码<br/><br/>
 * 注意：<br/>
 * 1.创建其对象的时候，推荐在Fragment.onViewCreated()方法中去创建<br/>
 * 2.在Fragment..onDestroy()中执行ViewPageSwitchFragmentHelper.onDestory()进行数据释放
 */
public class ViewPageSwitchFragmentHelper {

    private Object mFragmentObj;
    private ViewPager mViewPager;
    private OnSwitchFragmentListener mOnSwitchFragmentListener;

    public ViewPageSwitchFragmentHelper(android.support.v4.app.Fragment fragment) {
        mFragmentObj = fragment;
        initViewPager(fragment.getView());
    }

    public ViewPageSwitchFragmentHelper(Fragment fragment) {
        mFragmentObj = fragment;
        initViewPager(fragment.getView());
    }

    private void initViewPager(View view) {
        if (view != null) {
            ViewGroup vp = (ViewGroup) view.getParent();
            if (vp instanceof ViewPager) {
                mViewPager = (ViewPager) vp;
                mViewPager.addOnPageChangeListener(mOnPageChangeListener);
            }
        }
    }

    public void setOnSwitchFragmentListener(OnSwitchFragmentListener listener) {
        mOnSwitchFragmentListener = listener;
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (mViewPager != null) {
                Object itemObj = mViewPager.getAdapter().instantiateItem(mViewPager, position);
                if (mFragmentObj == itemObj) {
                    if (mOnSwitchFragmentListener != null) {
                        mOnSwitchFragmentListener.shouldOnResume();
                    } else {
                        mOnSwitchFragmentListener.shouldOnPause();
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    /**
     * 释放数据
     */
    public void onDestory() {
        mOnSwitchFragmentListener = null;
        mFragmentObj = null;
        if (mViewPager != null) {
            mViewPager.removeOnPageChangeListener(mOnPageChangeListener);
            mOnPageChangeListener = null;
            mViewPager = null;
        }
    }

    public interface OnSwitchFragmentListener {

        /**
         * 此时Fragment应该处于Pause的状态
         */
        void shouldOnPause();

        /**
         * 此时Fragment应该处于Resume的状态
         */
        void shouldOnResume();
    }
}
