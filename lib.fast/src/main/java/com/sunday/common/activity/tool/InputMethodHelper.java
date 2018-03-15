package com.sunday.common.activity.tool;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Field;

/**
 * Created by siwei on 2018/3/13.
 */

public class InputMethodHelper {

    protected InputMethodManager imm;// 键盘管理

    public InputMethodHelper(Context context) {
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * 关闭软键盘
     */
    public void closeSoftInput(Activity activity) {
        if (imm != null && imm.isActive() && activity != null) {
            View focusView = activity.getCurrentFocus();
            if (focusView != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }
    }

    /**
     * 显示软键盘
     */
    public void showSoftInput(EditText et) {
        if (et != null) {
            imm.showSoftInput(et, 0);
        }
    }

    /**
     * 把InputMethodManager的view置null避免内存泄露
     */
    public static void fixInputMethodManagerLeak(Context context) {
        if (context == null) {
            return;
        }

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field field;
        Object obj_get;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                field = inputMethodManager.getClass().getDeclaredField(param);
                if (field.isAccessible() == false) {
                    field.setAccessible(true);
                }
                obj_get = field.get(inputMethodManager);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == context) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        field.set(inputMethodManager, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        break;
                    }
                }
            } catch (Exception e) {
            }
        }
    }
}
