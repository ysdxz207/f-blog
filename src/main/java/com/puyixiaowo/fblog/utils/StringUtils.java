package com.puyixiaowo.fblog.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static Integer parseInteger(String str) {
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return Integer.valueOf(m.replaceAll("").trim());
    }

    public static void main(String[] args) {
        String str = "int(12)";
        System.out.println(parseInteger(str));
    }
}
