package com.puyixiaowo.fblog.error;

import com.puyixiaowo.fblog.exception.NoPermissionsException;
import com.puyixiaowo.fblog.utils.ExceptionEmailUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import static spark.Spark.exception;
import static spark.Spark.notFound;

public class ErrorHandler {

    public static void init() {


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
            response.redirect("/error/error404");
            return null;
        });
    }

    private static void handle500(){

        exception(Exception.class, (exception, request, response) -> {
            ExecutorService exec = Executors.newFixedThreadPool(5);

            FutureTask futureTask = new FutureTask(() -> {

                //发送邮件
                try {
                    ExceptionEmailUtils.sendException("飞鸿博客异常", exception);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            });

            exec.submit(futureTask);
            exec.shutdown();
            response.redirect("/error/error500");
        });
    }

    private static void handleNoPermissions(){
        exception(NoPermissionsException.class, (e, request, response) -> {
            response.body("您没有访问权限！");
        });
    }


}
