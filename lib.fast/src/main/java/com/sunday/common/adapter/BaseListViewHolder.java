package com.sunday.common.adapter;

import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by siwei on 2018/3/15.
 */

public class BaseListViewHolder {

    public BaseListViewHolder(View view){
        ButterKnife.bind(this, view);
    }
}
