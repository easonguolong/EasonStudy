package com.vollerystudy.common;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/29.
 */

public class CheckEmpty {
    public static boolean isNull(Object o){
        return o == null;
    }

    public static boolean isEmpty(CharSequence str){
        return isNull(str) || str.length() ==0;
    }

    public static boolean isEmpty(Object[] objects){
        return isNull(objects) || objects.length == 0;
    }

    public static boolean isEmpty(Collection<?> collection){
        return isNull(collection) || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map){
        return isNull(map) || map.isEmpty();
    }
}
