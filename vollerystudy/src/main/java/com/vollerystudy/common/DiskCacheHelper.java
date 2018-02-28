package com.vollerystudy.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/6/29.
 */

public class DiskCacheHelper {


    /**
     * @param context
     * @param uniqueName
     * @return  缓存地址
     */
    public File getDiskCache(Context context,String uniqueName){
        String cachePath ;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())|| !Environment.isExternalStorageRemovable()){
            cachePath = context.getExternalCacheDir().getPath();
        }else{
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath+File.separator+uniqueName);   //File.separator跨系统文件分隔符  “/”或“\”
    }

    /**
     * @param context
     * @return app version
     */
    public int getAppVersion(Context context){
        try{
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return info.versionCode;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * @param key  图片的URL
     * @return  经过MD5编码的图片URL
     */
    public String hashKeyForDisk(String key){
        String cacheKey;
        try{
            final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(key.getBytes());
            cacheKey = bytesToHexString(messageDigest.digest());
        }catch (NoSuchAlgorithmException e){
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }


    private String bytesToHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <bytes.length ; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1){
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }


}
