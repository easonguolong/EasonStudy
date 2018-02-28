package com.vollerystudy.meizi.api;

import android.content.Context;
import android.view.ContextThemeWrapper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vollerystudy.MyApp;
import com.vollerystudy.common.GetRandom;

/**
 * Created by Administrator on 2017/7/1.
 */

public class MeiziApi {

    private static String meiziData = "";

    public static String getMeiziApi(){
        StringBuilder meiziApi = new StringBuilder();
        meiziApi.append("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/").append("15").append("/" + GetRandom.getRandom());
        return String.valueOf(meiziApi);
    }

    public static String getMeiziData(Context context){
        String url = getMeiziApi();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                meiziData = s;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApp app = new MyApp();
        requestQueue.add(stringRequest);
        return meiziData;
    }

}
