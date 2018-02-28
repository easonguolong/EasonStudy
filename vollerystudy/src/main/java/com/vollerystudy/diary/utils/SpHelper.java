package com.vollerystudy.diary.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/6/30.
 */

public class SpHelper {

    private static final String SP_NAME = "sp_name";
    private static SpHelper mSpHelper;
    private Context mAppContext;
    private SharedPreferences mSharePreference;
    private String info;

    private SpHelper(Context context){
        mAppContext = context.getApplicationContext();
    }

    public static SpHelper getInstance(Context context){
        if (mSpHelper==null){
            synchronized (SpHelper.class){
                if (mSpHelper==null){
                    mSpHelper = new SpHelper(context);
                }
            }
        }
        return mSpHelper;
    }

    private SharedPreferences getSharePreference(){
        if(mSharePreference==null){
            mSharePreference = mAppContext.getSharedPreferences(SP_NAME,Context.MODE_APPEND);
        }
        return mSharePreference;
    }

    public void setInfo(String info){
        this.info = info;
        getSharePreference().edit().putString("info",info).apply();
    }

    public String getInfo(){
        if (info.equals("") || info.length() == 0){
            info = getSharePreference().getString("info","");
        }
        return info;
    }

}
