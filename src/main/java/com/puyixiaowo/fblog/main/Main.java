package com.puyixiaowo.fblog.main;

import com.puyixiaowo.fblog.Routes;
import com.puyixiaowo.fblog.error.ErrorHandler;
import com.puyixiaowo.fblog.utils.ConfigUtils;
import com.puyixiaowo.fblog.utils.DBUtils;

import static spark.Spark.notFound;
import static spark.Spark.port;

/**
 * @author Moses
 * @date 2017-08-01 18:21
 */
public class Main {
    public static void main(String[] args) {
        port(8003);
        notFound((request, response) -> ErrorHandler.handle404());

        ConfigUtils.init();
        DBUtils.initDB();
        Routes.init();
    }
}
