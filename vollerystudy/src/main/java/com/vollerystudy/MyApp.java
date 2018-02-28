package com.vollerystudy;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2017/6/30.
 */

public class MyApp extends Application {
    private static MyApp mContext;
    private static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        requestQueue = Volley.newRequestQueue(mContext);
    }

    public static Context getContext(){
        return mContext;
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

}
