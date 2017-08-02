package com.puyixiaowo.fblog;

import com.puyixiaowo.fblog.Controller.admin.MainController;
import com.puyixiaowo.fblog.Controller.front.IndexController;

import static spark.Spark.get;

public class Routes {
    public static void init(){
        //前台
        get("/", ((request, response) -> IndexController.index(request, response)));


        //后台管理
        get("/admin", ((request, response) -> MainController.index(request, response)));

    }
}
