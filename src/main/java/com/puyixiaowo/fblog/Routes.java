package com.puyixiaowo.fblog;

import com.puyixiaowo.fblog.Controller.admin.UserController;
import com.puyixiaowo.fblog.Controller.admin.ArticleController;
import com.puyixiaowo.fblog.Controller.admin.LoginController;
import com.puyixiaowo.fblog.Controller.admin.MainController;
import com.puyixiaowo.fblog.Controller.front.IndexController;
import com.puyixiaowo.fblog.filters.AdminAuthFilter;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import spark.Spark;

import static spark.Spark.*;

public class Routes {
    public static void init() {
        Spark.staticFileLocation("static");

        //前台
        get("/", ((request, response) -> IndexController.index(request, response)));

        //后台管理
        AdminAuthFilter.init();
        path("/admin", () -> {
            get("/", ((request, response) ->
                    MainController.index(request, response)),
                    new FreeMarkerTemplateEngine());
            get("/loginPage", ((request, response) ->
                            LoginController.loginPage(request, response)),
                    new FreeMarkerTemplateEngine());
            post("/login", ((request, response) ->
                    LoginController.doLogin(request, response)),
                    new FreeMarkerTemplateEngine());
            get("/user/add", (request, response) ->
                    UserController.editUser(request, response));
            post("/article/list", ((request, response) ->
                    ArticleController.selectArticleList(request, response)));
        });


    }
}
