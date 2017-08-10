package com.puyixiaowo.fblog.utils;

/**
 * @author feihong
 * @date 2017-08-10 23:21
 */
public class StringUtils {

    public static boolean isBlank(Object obj) {
        return obj == null || spark.utils.StringUtils.isBlank(obj.toString());
    }

    public static boolean isNotBlank(Object obj){
         return !isBlank(obj);
    }
}
