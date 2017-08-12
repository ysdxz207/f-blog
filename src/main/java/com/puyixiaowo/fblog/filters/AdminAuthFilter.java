package com.puyixiaowo.fblog.filters;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.Constants.Constants;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.utils.JedisUtils;
import com.puyixiaowo.fblog.utils.StringUtils;

import java.util.List;

import static spark.Spark.before;
import static spark.Spark.halt;

/**
 *
 * @author feihong
 * @date 2017-08-06 18:04:00
 * 管理后台用户权限控制过滤器
 */
public class AdminAuthFilter {



    public static void init() {
        //后台管理
        before("/admin/*", (request, response) -> {
            String uri = request.uri();
            if (!isIgnorePath(uri)
                    && (request.session().attribute(Constants.SESSION_USER_KEY) == null)) {

                response.redirect("/admin/loginPage");
                halt();
            }
        });
    }

    private static boolean isIgnorePath(String uri) {

        String str = JedisUtils.get(EnumsRedisKey.REDIS_KEY_IGNORE_CONF_KEY.key);

        if (StringUtils.isBlank(str)) {
            throw new RuntimeException("缓存中无忽略路径配置");
        }

        List<String> ignores = JSON.parseArray(str, String.class);

        for (String path : ignores) {
            if (removeFirstSeparator(path).equals(removeFirstSeparator(uri))) {
                return true;
            }
        }
        return false;
    }

    private static String removeFirstSeparator(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path;
    }

}
