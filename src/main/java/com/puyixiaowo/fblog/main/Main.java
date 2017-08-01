package com.puyixiaowo.fblog.main;

import static spark.Spark.get;
import static spark.Spark.port;

/**
 * @author Moses
 * @date 2017-08-01 18:21
 */
public class Main {
    public static void main(String[] args) {
        port(2333);
        get("/", (request, response) -> "Hello World!");
    }
}
