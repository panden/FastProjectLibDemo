package com.sunday.common.volley.toolbox;

import android.util.Log;

import com.sunday.common.volley.AuthFailureError;
import com.sunday.common.volley.NetworkResponse;
import com.sunday.common.volley.Request;
import com.sunday.common.volley.Response;
import com.sunday.common.widgets.phoenix.util.Logger;

import org.apache.http.HttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/6/26.
 */
public class MultipartRequest extends Request<String> {

    private Response.Listener<String>  listener = null;
    private MultipartParams params = null;
    private HttpEntity httpEntity = null;

    public MultipartRequest(String url, MultipartParams params,Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        this(Method.POST,url, params,listener, errorListener);
    }

    public MultipartRequest(int method, String url, MultipartParams params,Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        super(method,  url,errorListener);
        this.params = params;
        this.listener = listener;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (params != null) {
            httpEntity = params.getEntity();
            try {
                httpEntity.writeTo(baos);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Logger.d("IOException writing to ByteArrayOutputStream");
            }
            String str = new String(baos.toByteArray());
            Logger.d("bodyString is :" + str);
        }
        return baos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        // TODO Auto-generated method stub
        Map<String, String> headers = super.getHeaders();
        if (null == headers || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }
        // MainApplication.getInstance().addSessionCookie(headers);
        return headers;
    }

    @Override
    public String getBodyContentType() {
        // TODO Auto-generated method stub
        String str = httpEntity.getContentType().getValue();
        return httpEntity.getContentType().getValue();
    }

    @Override
    protected void deliverResponse(String response) {
        // TODO Auto-generated method stub
        Log.i("HTTP", "请求接口：" + getKey() + "    返回数据:" + response);
        listener.onResponse(getKey(),response);
    }


}
