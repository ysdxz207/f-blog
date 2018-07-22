package com.puyixiaowo.fblog.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * ID生成工具
 * @author W.feihong
 * @date 2017-08-10
 */
public class IdUtils {

    public static String generateId() {

        return DateFormatUtils.format(new Date(),
                "yyyyMMddHHmmssSSS")
                + System.nanoTime();
    }
}
