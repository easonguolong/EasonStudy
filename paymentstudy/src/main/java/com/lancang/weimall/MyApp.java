package com.lancang.weimall;

import android.app.Application;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2018/2/1.
 */

public class MyApp extends Application {
    public static final String APP_Secret = "9729d4b32301d6ab8e72c5c74ed2d5b6";// 微信APP_Secret
    public static final String APP_ID = "wx7379d82be3fcc3d1";// 微信APPID
    public static IWXAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);

    }
}
