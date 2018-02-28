package com.vollerystudy.common;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;


/**
 * Created by Administrator on 2017/6/29.
 */

public class GlideHelper {

    public static String getImagePathCache(String imageurl, Context context){
        FutureTarget<File> futureTarget = Glide.with(context).load(imageurl).downloadOnly(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL);
        File cacheFile ;
        try{
            cacheFile = futureTarget.get();
            return cacheFile.getAbsolutePath();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
