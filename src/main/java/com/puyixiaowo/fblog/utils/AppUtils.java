package com.puyixiaowo.fblog.utils;

import com.puyixiaowo.fblog.bean.sys.AppConfigBean;
import com.puyixiaowo.fblog.enums.EnumAppConfig;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Moses
 * @date 2017-09-01 13:33
 */
public class AppUtils {

    public static AppConfigBean getAppConfigBean(String[] args) {

        AppConfigBean appConfigBean = new AppConfigBean();
        Map<String, String> argMap = new LinkedHashMap<>();
        Iterator<String> it = Arrays.asList(args).iterator();
        while (it.hasNext()) {
            String arg = it.next();
            if (EnumAppConfig.ARG_PORT.arg.equals(arg)) {
                String argValue = it.next();
                argMap.put(arg, argValue);
            }
        }

        return appConfigBean;
    }
}
