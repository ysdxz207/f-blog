package com.puyixiaowo.fblog;

import com.puyixiaowo.fblog.Controller.admin.LoginController;
import com.puyixiaowo.fblog.Controller.front.IndexController;
import com.puyixiaowo.fblog.filters.AdminAuthFilter;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;

public class Routes {
    public static void init() {

        //前台
        get("/", ((request, response) -> IndexController.index(request, response)));

        //后台管理
        AdminAuthFilter.init();
        path("/admin", () -> {
            get("/", ((request, response) -> LoginController.index(request, response)));
            get("/loginPage", ((request, response) ->
                            LoginController.loginPage(request, response)),
                    new FreeMarkerTemplateEngine());
            post("/login", ((request, response) ->
                    LoginController.doLogin(request, response)));
        });


    }
}
