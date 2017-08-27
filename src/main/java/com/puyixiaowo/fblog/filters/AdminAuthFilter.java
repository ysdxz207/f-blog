package com.puyixiaowo.fblog.filters;

import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.utils.RedisUtils;

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

        List<String> ignores = RedisUtils.get(EnumsRedisKey.REDIS_KEY_IGNORE_CONF.key,
                List.class);

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
