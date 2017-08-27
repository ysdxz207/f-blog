package com.puyixiaowo.fblog.filters;

import static spark.Spark.before;

/**
 * @author Moses
 * @date 2017-08-27 16:17
 */
public class AdminPermissionsFilter {
    public static void init(){
        before("/*", (request, response) -> {

        });
    }
}
