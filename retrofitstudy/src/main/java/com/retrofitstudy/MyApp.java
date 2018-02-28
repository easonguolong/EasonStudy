package com.retrofitstudy;

import android.app.Application;

import com.retrofitstudy.utils.ToastUtils;

/**
 * Created by Administrator on 2017/7/5.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
        RetrofitManager.setBaseUrl("http://www.thingtill.com/index.php/home/android/");
        RetrofitManager.setUserToken("456465gdsg");
    }
}