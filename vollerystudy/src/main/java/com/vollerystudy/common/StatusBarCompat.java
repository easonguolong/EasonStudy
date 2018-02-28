package com.vollerystudy.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

/**
 * * 修改状态栏的工具类
 *
 * Created by Administrator on 2017/6/29.
 */

public class StatusBarCompat {
    private static final int INVALID_VAL = -1;
    private static final int COLOR_DEFAULT = Color.parseColor("#20000000");

    public static void compat(Activity activity,int statusColor){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if (statusColor != INVALID_VAL){
                activity.getWindow().setStatusBarColor(statusColor);
            }
            return;
        }
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT<=Build.VERSION_CODES.LOLLIPOP){
            int color = COLOR_DEFAULT;
            ViewGroup viewGroup = (ViewGroup)activity.findViewById(android.R.id.content);
            if (statusColor !=INVALID_VAL){
                color = statusColor;
            }
            View statusBarView = viewGroup.getChildAt(0);
            if (statusBarView != null && statusBarView.getMeasuredHeight()== getStatusBarHeight(activity)){
                statusBarView.setBackgroundResource(color);
                return;
            }
            statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            viewGroup.addView(statusBarView,lp);
        }
    }

    public static void compat(Activity activity){
        compat(activity,INVALID_VAL);
    }


    private static int getStatusBarHeight(Context context){
        int result = 0;
        int resourcId = context.getResources().getIdentifier("status_bar_height","dimen","android");
        if (resourcId > 0){
            result = context.getResources().getDimensionPixelSize(resourcId);
        }
        return  result;
    }

}
