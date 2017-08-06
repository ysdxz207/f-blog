package com.puyixiaowo.fblog;

import com.puyixiaowo.fblog.Controller.admin.MainController;
import com.puyixiaowo.fblog.Controller.front.IndexController;
import com.puyixiaowo.fblog.filters.AdminAuthFilter;

import static spark.Spark.get;

public class Routes {
    public static void init(){

        //前台
        get("/", ((request, response) -> IndexController.index(request, response)));


        AdminAuthFilter.init();
        //后台管理
        get("/admin", ((request, response) -> MainController.index(request, response)));
        get("/admin/login", ((request, response) ->
                MainController.login(request, response)));

    }
}
