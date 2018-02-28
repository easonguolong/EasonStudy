package com.vollerystudy.meizi.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vollerystudy.meizi.bean.MeiziBean;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/1.
 */

public class GsonHelper {

    public static List<MeiziBean> getMeiziBean(String response){
        List<MeiziBean> meiziBeanList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(response);
            String meiziArrary = jsonObject.getString("results");
            Type meiziBeanType = new TypeToken<List<MeiziBean>>(){}.getType();
            Gson gson = new Gson();
            meiziBeanList = gson.fromJson(meiziArrary,meiziBeanType);
        }catch (Exception e){
            e.printStackTrace();
        }
        return meiziBeanList;
    }


}
