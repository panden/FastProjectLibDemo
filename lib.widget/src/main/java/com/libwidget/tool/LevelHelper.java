package com.libwidget.tool;

/**
 * Created by siwei on 2018/3/18.
 * ImageView.setImageLevel(level)的工具类
 */

public class LevelHelper {

    private int[] mLevels;
    private boolean mTakeNext;//在指定范围内,true取范围内的下一个值，false取范围内的上一个值

    public LevelHelper(int... levels){
        mLevels = levels;
    }

    public LevelHelper(boolean takeNext, int... levels){
        mTakeNext = takeNext;
        mLevels = levels;
    }

    public int formatLevel(int level){
        int formatLevel = -1;
        for(int i = 0; i< mLevels.length; i++){
            if(i ==  mLevels.length-1){
                //超出了给出的最大范围
            	if(level >= mLevels[i]){
                    formatLevel = mLevels[i];
                    break;
            	}
            }else if(i == 0 && level <= mLevels[i]){
            	//低于范围
            	formatLevel = mLevels[i];
            	break;
            }else if(level >= mLevels[i] && level < mLevels[i+1]){
                //在该范围内
                formatLevel = mTakeNext ? mLevels[i+1] : mLevels[i];
                break;
            }
        }
        return formatLevel;
    }
}
