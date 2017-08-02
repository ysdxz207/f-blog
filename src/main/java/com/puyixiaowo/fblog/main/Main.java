package com.puyixiaowo.fblog.main;

import com.puyixiaowo.fblog.Routes;
import com.puyixiaowo.fblog.error.ErrorHandler;

import static spark.Spark.notFound;
import static spark.Spark.port;

/**
 * @author Moses
 * @date 2017-08-01 18:21
 */
public class Main {
    public static void main(String[] args) {
        port(2333);
        notFound((request, response) -> ErrorHandler.init());
        Routes.init();
    }
}
