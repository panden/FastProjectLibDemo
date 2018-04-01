package com.sunday.common.widgets.wheel.helper;

import android.app.Activity;

import com.sunday.common.widgets.wheel.pickview.CharacterPickerView;
import com.sunday.common.widgets.wheel.pickview.CharacterPickerWindow;
import com.sunday.common.widgets.wheel.pickview.OnOptionChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by siwei on 2018/4/1.
 */

public class TimeOptionHelper {

    private List<String> options1Items = null;
    private List<List<String>> options2Items = null;
    private List<List<List<String>>> options3Items = null;

    private Map<String, List<String>> mMapDatas = new HashMap<>();

    private int yearLength = 20;//年的长度
    private int currentYear, currentMonth, currentDay;

    public TimeOptionHelper() {
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public interface OnDataSelectedListener {

        void onDataSelected(int year, int month, int day);
    }

    public CharacterPickerWindow builderYM(Activity activity, final OnDataSelectedListener listener) {
        //选项选择器
        CharacterPickerWindow mOptions = new CharacterPickerWindow(activity);
        //初始化选项数据
        setYMDataPicture(mOptions.getPickerView());
        //设置默认选中的三级项目
        mOptions.setCurrentPositions(0, 0, 0);
        //监听确定选择按钮
        mOptions.setOnoptionsSelectListener(new OnOptionChangedListener() {
            @Override
            public void onOptionChanged(int option1, int option2, int option3) {
                if (listener != null) {
                    int year = currentYear + option1;
                    int month = (option1 == 0 ? currentMonth + option2 : option2) + 1;
                    listener.onDataSelected(year, month, 0);
                }
            }
        });
        return mOptions;
    }

    public void setYMDataPicture(CharacterPickerView view) {
        if (options1Items == null) {
            options1Items = new ArrayList<>();
            options2Items = new ArrayList<>();
        }
        Map<String, List<String>> mapYMDatas = getYMData();
        for (Map.Entry<String, List<String>> entry1 : mapYMDatas.entrySet()) {
            String key1 = entry1.getKey();
            List<String> value1 = entry1.getValue();
            options1Items.add(key1);
            options2Items.add(value1);
        }
        view.setPicker(options1Items, options2Items);
    }

    /**
     * 获取年月的数据
     */
    public Map<String, List<String>> getYMData() {
        Map<String, List<String>> ymData = new LinkedHashMap<>();
        for (int year = currentYear; year < (currentYear + yearLength); year++) {
            List<String> monthList = new ArrayList<>();
            System.out.println("month for " + (year == currentYear ? 12 - currentMonth : 12));
            for (int month = (year == currentYear ? currentMonth : 0); month < 12; month++) {
                monthList.add(getMonthStr(month));
            }
            ymData.put(getYearStr(year), monthList);
        }
        return ymData;
    }

    public String getYearStr(int year) {
        return year + "年   ";
    }

    public String getMonthStr(int month) {
        return (month < 9 ? "0" : "") + (month + 1) + " 月  ";
    }

}
