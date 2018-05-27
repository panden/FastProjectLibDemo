package com.sunday.common.mvp;

import com.sunday.common.activity.BaseApplication;
import com.sunday.common.cache.ACache;
import com.sunday.common.http.HttpFactory;

/**
 * Created by siwei on 2018/3/13.
 * Model层封装，后续会封装一些数据访问的工具类在其中，方便数据层去访问数据
 */

public class ModelImpl implements IModel {

    /**创建接口服务*/
    protected <T> T createRetorfitService(final Class<T> service) {
        return HttpFactory.instance().createApiService(service);
    }

    /**
     * 获取缓存
     */
    protected ACache getCache() {
        return BaseApplication.getInstance().getCache();
    }

    @Override
    public void onRelease() {
        //释放数据的时候会调用
    }

    //其余一些数据操作的封装....

}
