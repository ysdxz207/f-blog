package com.puyixiaowo.fblog.error;

import com.puyixiaowo.fblog.bean.sys.ErrorPagesBean;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.exception.NoPermissionsException;
import com.puyixiaowo.fblog.utils.RedisUtils;
import com.puyixiaowo.fblog.utils.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static spark.Spark.exception;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;

public class ErrorHandler {
    private static ErrorPagesBean[] ERROR_PAGES = {
            new ErrorPagesBean("404", EnumsRedisKey.REDIS_KEY_404_PAGE.key, "error/404.html", "阿狸找不到页面了"),
            new ErrorPagesBean("500", EnumsRedisKey.REDIS_KEY_500_PAGE.key, "error/500.html", "服务器报错了！已经报告给了管理员!")
    };

    public static void init() {

        for (ErrorPagesBean errorPagesBean : ERROR_PAGES) {

            try {
                InputStream inputStream = ResourceUtils.readFile(errorPagesBean.getErrorPage());

                Scanner sc = new Scanner(inputStream, "UTF-8");
                StringBuilder sb = new StringBuilder();
                while (sc.hasNextLine()) {
                    sb.append(sc.nextLine());
                }
                // note that Scanner suppresses exceptions
                if (sc.ioException() != null) {
                    throw sc.ioException();
                }

                RedisUtils.set(errorPagesBean.getRedisKey(), sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
                RedisUtils.set(errorPagesBean.getRedisKey(), errorPagesBean.getCode());
            }
        }
    }

    /**
     * 处理错误信息
     */
    public static void handleErrors() {

        handle404();
        handle500();
        handleNoPermissions();
    }

    private static void handle404(){

        notFound((request, response) -> {
            String html = RedisUtils.get(EnumsRedisKey.REDIS_KEY_404_PAGE.key);
            return html;
        });
    }

    private static void handle500(){

        internalServerError((request, response) -> {
            String html = RedisUtils.get(EnumsRedisKey.REDIS_KEY_500_PAGE.key);
            return html;
        });
    }

    private static void handleNoPermissions(){
        exception(NoPermissionsException.class, (e, request, response) -> {
            response.body("您没有访问权限！");
        });
    }


}
