package com.puyixiaowo.fblog.filters;

import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.controller.admin.LoginController;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import win.hupubao.common.utils.RedisUtils;
import win.hupubao.common.utils.StringUtils;

import java.util.Arrays;
import java.util.List;

import static spark.Spark.before;

/**
 *
 * @author W.feihong
 * @date 2017-08-06
 * 管理后台用户权限控制过滤器
 */
public class AdminAuthFilter {



    public static void init() {
        //后台管理
        before("/admin/*", (request, response) -> {

            String origin = request.headers("Origin");
            if (StringUtils.isNotBlank(origin)
                    && Constants.ALLOWED_ORIGINS.length != 0) {
                String originAllowed = Arrays.asList(Constants.ALLOWED_ORIGINS).contains(origin) ? origin : "";
//                logger.info("[" + origin + "][" + originAllowed + "]");
                response.header("Access-Control-Allow-Origin", originAllowed);
                response.header("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
                response.header("Access-Control-Allow-Headers", "X-Requested-With,Content-Type");
                response.header("Access-Control-Allow-Credentials", "true");

            }

            String uri = request.uri();
            if (!isIgnorePath(uri)
                    && (request.session().attribute(Constants.SESSION_USER_KEY) == null)) {

                LoginController.cookieLogin(request, response);
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
