package com.puyixiaowo.fblog.generator.utils;

import com.puyixiaowo.fblog.utils.StringUtils;

/**
 * @author Moses
 * @date 2017-08-16 22:55
 */
public class NameUtils {
    /**
     * 首字母转大写
     * @param name
     * @return
     */
    public static String firstToUpperCase(String name) {
        return StringUtils.isBlank(name) ? "" : name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
