package com.puyixiaowo.fblog.error;

import com.puyixiaowo.fblog.exception.NoPermissionsException;
import com.puyixiaowo.fblog.utils.ExceptionEmailUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static spark.Spark.exception;
/**
 *
 * @author Moses
 * @date 2018-03-14 14:54:58
 */

public class ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);


    public static void init() {


    }

    /**
     * 处理错误信息
     */
    public static void handleSystemErrors() {

        handle404();
        handle500();
        handleNoPermissions();
    }

    private static void handle404(){

//        notFound((request, response) -> {
//            return "404";
//        });
    }

    private static void handle500(){

        exception(Exception.class, (exception, request, response) -> {

            ScheduledExecutorService exec = new ScheduledThreadPoolExecutor(1,
                    new BasicThreadFactory.Builder().namingPattern("handle500-schedule-pool-%d")
            .daemon(true).build());

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
