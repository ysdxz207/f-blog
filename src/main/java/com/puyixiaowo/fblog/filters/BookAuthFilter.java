package com.puyixiaowo.fblog.filters;

import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.controller.admin.LoginController;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.utils.RedisUtils;
import spark.Request;
import spark.Response;

import java.util.List;

import static spark.Spark.before;
import static spark.Spark.halt;

/**
 *
 * @author Moses
 * @date 2017-12-19
 * 书用户权限控制过滤器
 */
public class BookAuthFilter {



    public static void init() {
        //书
        before("/book/*", (request, response) -> {
            String uri = request.uri();
            if (!isIgnorePath(uri)
                    && (request.session().attribute(Constants.SESSION_USER_KEY) == null)) {

                LoginController.login(request, response);
                halt();
            }
        });
    }


    private static boolean isIgnorePath(String uri) {

        List<String> ignores = RedisUtils.get(EnumsRedisKey.REDIS_KEY_IGNORE_CONF_BOOK.key,
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
