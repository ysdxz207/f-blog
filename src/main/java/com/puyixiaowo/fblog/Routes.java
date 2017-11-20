package com.puyixiaowo.fblog;

import com.puyixiaowo.fblog.controller.IndexController;
import com.puyixiaowo.fblog.controller.admin.*;
import com.puyixiaowo.fblog.controller.admin.afu.AfuController;
import com.puyixiaowo.fblog.controller.admin.afu.AfuTypeController;
import com.puyixiaowo.fblog.controller.afu.AfuApiController;
import com.puyixiaowo.fblog.controller.fblog.FblogController;
import com.puyixiaowo.fblog.filters.AdminAuthFilter;
import com.puyixiaowo.fblog.filters.AdminPermissionsFilter;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import spark.Spark;

import static spark.Spark.*;

public class Routes {
    public static void init() {
        Spark.staticFileLocation("static_resources");

        get("/", (request, response) -> IndexController.index(request, response));

        //fblog前台
        path("/yiyi", () -> {
            get("/", ((request, response) -> FblogController.articleList(request, response)));
            get("/category/list", (request, response) -> FblogController.categoryList(request, response));
            get("/tag/top", (request, response) -> FblogController.tagTop(request, response));
            get("/article/detail", (request, response) -> FblogController.articleDetail(request, response));
            get("/search", (request, response) -> FblogController.search(request, response));
            get("/article/tags", (request, response) -> FblogController.articleTags(request, response));

        });

        path("/pupu", () -> {
            get("/", (request, response) -> {
                return "开发中...";
            });
        });

        //后台管理
        AdminAuthFilter.init();
        //权限控制
        AdminPermissionsFilter.init();

        //管理后台
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
                get("/array/:parent", ((request, response) ->
                        MenuController.array(request, response)));

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
                        RoleController.allArray(request));
            });

            //博客组
            path("/article", () -> {

                get("/:data", ((request, response) ->
                        ArticleController.articles(request, response)));

                post("/edit/:data", ((request, response) ->
                        ArticleController.edit(request, response)));

                post("/delete", ((request, response) ->
                        ArticleController.delete(request, response)));

                get("/lucene/reindex", ((request, response) ->
                        ArticleController.luceneReindex(request, response)));
            });

            //博客分类组
            path("/category", () -> {

                get("/:data", ((request, response) ->
                        CategoryController.categorys(request, response)));

                post("/edit/:data", ((request, response) ->
                        CategoryController.edit(request, response)));

                post("/delete", ((request, response) ->
                        CategoryController.delete(request, response)));

                get("/all/array", (request, response) ->
                        CategoryController.allArray(request));
            });

            //博客标签组
            path("/tag", () -> {

                get("/:data", ((request, response) ->
                        TagController.tags(request, response)));

                post("/edit/:data", ((request, response) ->
                        TagController.edit(request, response)));

                post("/delete", ((request, response) ->
                        TagController.delete(request, response)));

                get("/top/array", (request, response) ->
                        TagController.topArray(request));
            });

            //afu组
            path("/afu", () -> {
                get("/:data", ((request, response) ->
                        AfuController.afus(request, response)));
                get("/detail/:id", ((request, response) ->
                        AfuController.detail(request, response)));
                get("/delete", ((request, response) ->
                        AfuController.delete(request, response)));

                path("/type", () -> {

                    get("/:data", ((request, response) ->
                            AfuTypeController.afuTypes(request, response)));
                    post("/edit/:data", ((request, response) ->
                            AfuTypeController.edit(request, response)));
                    post("/delete", ((request, response) ->
                            AfuTypeController.delete(request, response)));

                    get("/all/array", ((request, response) ->
                            AfuTypeController.allArray(request)));


                });
            });
        });

        //afu
        path("/api", () -> {
            path("/afu", () -> {
                post("/list", ((request, response) ->
                        AfuApiController.apiAfus(request, response)));
                post("/edit", ((request, response) ->
                        AfuApiController.apiAfusEdit(request, response)));
                post("/delete", ((request, response) ->
                        AfuApiController.apiAfusDelete(request, response)));
            });
        });
    }
}
