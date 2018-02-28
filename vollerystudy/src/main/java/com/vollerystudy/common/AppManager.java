package com.vollerystudy.common;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.Stack;

/**
 * Created by Administrator on 2017/6/29.
 */

public class AppManager {
    private static Stack<AppCompatActivity> stackActivity;
    private static AppManager inStance;

    private AppManager(){};

    public static AppManager getAppManager(){
        if (inStance==null){
            synchronized (AppManager.class){
                if (inStance ==null){
                    inStance =new AppManager();
                }
            }
        }
        return inStance;
    }

    public void addActivity(AppCompatActivity activity){
        if (stackActivity == null){
            stackActivity = new Stack<>();
        }
        stackActivity.add(activity);
    }

    public AppCompatActivity getCurrentActivity(){
        if (stackActivity==null || stackActivity.isEmpty()){
            return null;
        }
        AppCompatActivity activity = stackActivity.lastElement();
        return activity;
    }

    public AppCompatActivity findActivity(Class<?> cls){
        AppCompatActivity activity = null;
        if (stackActivity==null || stackActivity.isEmpty()){
            return null;
        }
        for (AppCompatActivity appCompatActivity: stackActivity) {
            if (appCompatActivity.getClass().equals(cls)){
                activity = appCompatActivity;
                break;
            }
        }
        return activity;
    }

    public void finishActivity(){
        AppCompatActivity activity = stackActivity.lastElement();
        activity.finish();
    }

    public void finishActivity(AppCompatActivity activity){
        if (activity !=null){
            stackActivity.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void finishActivity(Class<?> cls){
        for (AppCompatActivity appCompatActivity:stackActivity) {
            if (appCompatActivity.getClass().equals(cls)){
                finishActivity(appCompatActivity);
            }
        }
    }

    public void finishAllActivity(){
        for (int i = 0; i <stackActivity.size() ; i++) {
            if (null!= stackActivity.get(i)){
                stackActivity.get(i).finish();
            }
        }
        stackActivity.clear();
    }

    public void AppExit(Context context){
        try{
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
