package com.sunday.common.utils;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;


/**
 * UI工具类
 */
public class UiUtil {

    public static double density = 1d;

    public static void setVisibility(View view, int visibility) {
        if (view != null && view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    public static void setVisibility(View parent, int id, int visibility) {
        if (parent != null) {
            View view = parent.findViewById(id);
            if (view != null) {
                setVisibility(view, visibility);
            }
        }
    }

    public static void requestFocus(View view) {
        if (view != null) {
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.requestFocus();
        }
    }


    public static int dip2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, Resources.getSystem().getDisplayMetrics());
    }

    public static int px2dip(float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);  
    }
    
    public static int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, Resources.getSystem()
                .getDisplayMetrics());
    }

}
