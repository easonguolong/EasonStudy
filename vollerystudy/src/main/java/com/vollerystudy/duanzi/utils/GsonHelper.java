package com.vollerystudy.duanzi.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vollerystudy.duanzi.bean.DuanziBean;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/1.
 */

public class GsonHelper  {

    public static List<DuanziBean> getDuanziBeanList(String response){
        List<DuanziBean> mDuanziBeanList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(response);
            String dataArraryStr = jsonObject.getJSONObject("data").getString("data");
            Type type = new TypeToken<List<DuanziBean>>(){}.getType();
            Gson gson = new Gson();
            mDuanziBeanList = gson.fromJson(dataArraryStr,type);
            return mDuanziBeanList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return mDuanziBeanList;
    }


}
