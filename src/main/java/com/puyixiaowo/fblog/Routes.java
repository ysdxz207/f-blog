package com.puyixiaowo.fblog;

import com.puyixiaowo.fblog.Controller.admin.*;
import com.puyixiaowo.fblog.Controller.fblog.ArticleController;
import com.puyixiaowo.fblog.Controller.fblog.IndexController;
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

            get("/main", ((request, response) ->
                            MainController.main(request, response)),
                    new FreeMarkerTemplateEngine());

            /*
             * 菜单组
             */
            path("/menu", () -> {
                get("/menus/:type", ((request, response) ->
                        MenuController.navMenus(request, response)));
                post("/menus/:type", ((request, response) ->
                        MenuController.navMenus(request, response)));

                get("/:data", ((request, response) ->
                        MenuController.menus(request, response)));
                get("/all/array", ((request, response) ->
                        MenuController.allArray()));
            });
            /*
             * 用户组
             */
            path("/user", () -> {

                get("/:data", ((request, response) ->
                        UserController.users(request, response)));
            });
            /*
             * 权限组
             */
            path("/permission", () -> {

                get("/:data", ((request, response) ->
                        PermissionController.permissions(request, response)));

                post("/edit", ((request, response) ->
                        PermissionController.edit(request, response)));

                post("/delete", ((request, response) ->
                        PermissionController.delete(request, response)));
            });
        });
    }
}
