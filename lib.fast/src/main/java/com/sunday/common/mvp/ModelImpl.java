package com.sunday.common.mvp;

import android.database.sqlite.SQLiteDatabase;

import com.sunday.common.activity.BaseApplication;
import com.sunday.common.cache.ACache;
import com.sunday.common.http.HttpFactory;

import org.litepal.LitePal;

/**
 * Created by siwei on 2018/3/13.
 * Model层封装，后续会封装一些数据访问的工具类在其中，方便数据层去访问数据
 */

public class ModelImpl implements IModel {


    protected <T> T createRetorfitService(final Class<T> service) {
        return HttpFactory.instance().createApiService(service);
    }

    /**
     * 获取缓存
     */
    protected ACache getCache() {
        return BaseApplication.getInstance().getCache();
    }

    /**
     * 获取数据库操作(LitePal:<a>https://www.jianshu.com/p/bc68e763c7a2</a>)
     * 数据库映射关系在asset/litepal.xml中
     */
    protected SQLiteDatabase getDB() {
        return LitePal.getDatabase();
    }

    protected void readFile() {
        //读取文件
    }

    @Override
    public void onRelease() {

    }

    //其余一些数据操作的封装....

}
