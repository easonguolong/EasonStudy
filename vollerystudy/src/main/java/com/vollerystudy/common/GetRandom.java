package com.vollerystudy.common;

/**
 * Created by Administrator on 2017/6/29.
 */

public class GetRandom {


    /**
     * 获取一个 0 到 48 之间的随机整数
     *
     * @return 一个 随机整数
     */
    public static int getRandom(){
        double random = Math.random();
        int result = (int)(random * 50 - 20);
        return Math.abs(result);
    }
}
