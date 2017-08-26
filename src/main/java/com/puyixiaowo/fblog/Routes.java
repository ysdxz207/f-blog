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

            get("/logout", ((request, response) ->
                            LoginController.logout(request, response)));

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
                get("/all/array/:parent", ((request, response) ->
                        MenuController.allArray(request, response)));

                post("/edit", ((request, response) ->
                        MenuController.edit(request, response)));

                post("/delete", ((request, response) ->
                        MenuController.delete(request, response)));
            });
            /*
             * 用户组
             */
            path("/user", () -> {

                get("/:data", ((request, response) ->
                        UserController.users(request, response)));

                post("/edit", ((request, response) ->
                        UserController.edit(request, response)));

                post("/delete", ((request, response) ->
                        UserController.delete(request, response)));
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

            /*
             * 角色组
             */
            path("/role", () -> {
                get("/:data", ((request, response) ->
                        RoleController.roles(request, response)));

                post("/edit", ((request, response) ->
                        RoleController.edit(request, response)));

                post("/delete", ((request, response) ->
                        RoleController.delete(request, response)));

                get("/setPermission/:data", ((request, response) ->
                        RoleController.setPermission(request, response)));

                get("/all/array", (request, response) ->
                RoleController.allArray());
            });
        });
    }
}
