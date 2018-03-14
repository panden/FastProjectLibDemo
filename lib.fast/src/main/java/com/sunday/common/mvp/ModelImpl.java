package com.sunday.common.mvp;

import android.database.sqlite.SQLiteDatabase;

import com.sunday.common.activity.BaseApplication;
import com.sunday.common.cache.ACache;
import com.sunday.common.volley.AuthFailureError;
import com.sunday.common.volley.Request;
import com.sunday.common.volley.RequestQueue;
import com.sunday.common.volley.Response;
import com.sunday.common.volley.toolbox.StringRequest;

import org.litepal.LitePal;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;

/**
 * Created by siwei on 2018/3/13.
 * Model层封装，后续会封装一些数据访问的工具类在其中，方便数据层去访问数据
 */

public class ModelImpl implements IModel {

    protected void post(String url, final HashMap<String, String> maps,
                        Response.ErrorListener errorListener,
                        Response.Listener response) {
        StringRequest request = new StringRequest(Request.Method.POST, url, response, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return maps;
            }
        };
        BaseApplication.getInstance().getRequestQueue().add(request);
    }

    protected RequestQueue getRequestQueue(){
        return BaseApplication.getInstance().getRequestQueue();
    }

    protected Retrofit getRetorfit(){
        return BaseApplication.getRetrofit();
    }

    /**获取缓存*/
    protected ACache getCache(){
        return BaseApplication.getInstance().getCache();
    }

    /**获取数据库操作(LitePal:<a>https://www.jianshu.com/p/bc68e763c7a2</a>)
     * 数据库映射关系在asset/litepal.xml中
     */
    protected SQLiteDatabase getDB(){
        return LitePal.getDatabase();
    }

    protected void readFile(){
        //读取文件
    }

    //其余一些数据操作的封装....

}
