package com.puyixiaowo.fblog;

import com.puyixiaowo.fblog.controller.IndexController;
import com.puyixiaowo.fblog.controller.TestController;
import com.puyixiaowo.fblog.controller.admin.*;
import com.puyixiaowo.fblog.controller.admin.afu.AfuController;
import com.puyixiaowo.fblog.controller.admin.afu.AfuTypeController;
import com.puyixiaowo.fblog.controller.afu.AfuApiController;
import com.puyixiaowo.fblog.controller.fblog.FblogController;
import com.puyixiaowo.fblog.controller.tools.qrcode.QrcodeController;
import com.puyixiaowo.fblog.filters.AdminAuthFilter;
import com.puyixiaowo.fblog.filters.AdminPermissionsFilter;
import spark.Spark;

import static spark.Spark.*;

public class Routes {
    public static void init() {
        Spark.staticFileLocation("static_resources");

        get("/", (request, response) -> IndexController.index(request, response));

        path("/test", () -> {
            get("", ((request, response) -> TestController.test(request, response)));
            post("", ((request, response) -> TestController.test(request, response)));

            get("/json", ((request, response) -> TestController.testJson(request, response)));
            post("/json", ((request, response) -> TestController.testJson(request, response)));


        });

        path("/accessrecord", () -> {
            get("/:data", ((request, response) -> AccessRecordController.accessRecords(request, response)));
            post("/:data", ((request, response) -> AccessRecordController.accessRecords(request, response)));

        });

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
                            MainController.index(request, response)));
            post("/login", ((request, response) ->
                            LoginController.login(request, response, request.raw())));

            get("/logout", ((request, response) ->
                    LoginController.logout(request, response)));

            get("/captcha", ((request, response) ->
                            LoginController.captcha(request, response)));
            get("/mainPage", ((request, response) ->
                            MainController.mainPage(request, response)));

            /*
             * 菜单组
             */
            path("/menu", () -> {
                get("", (MenuController::navMenus));
                post("", (MenuController::navMenus));

                post("/edit", (MenuController::edit));

                post("/delete", (MenuController::delete));

                get("/types", (MenuController::typeList));
                post("/types", (MenuController::typeList));
            });
            /*
             * 用户组
             */
            path("/user", () -> {

                get("", (UserController::users));

                post("/edit", (UserController::edit));

                post("/delete", (UserController::delete));
                get("/current/edit/:data", (UserController::currentEdit));
                post("/current/edit/:data", (UserController::currentEdit));
            });
            /*
             * 权限组
             */
            path("/permission", () -> {

                get("", (PermissionController::permissions));

                post("/edit", (PermissionController::edit));

                post("/delete", (PermissionController::delete));
            });

            /*
             * 角色组
             */
            path("/role", () -> {
                get("", (RoleController::roles));

                post("/edit", (RoleController::edit));

                post("/delete", (RoleController::delete));

                get("/permissions", (RoleController::rolePermissionList));

                post("/setPermission", (RoleController::setPermission));

            });

            //博客组
            path("/article", () -> {

                get("", (ArticleController::articles));

                post("/detail", (ArticleController::detail));

                post("/edit", (ArticleController::edit));

                post("/delete", (ArticleController::delete));

                get("/lucene/reindex", (ArticleController::luceneReindex));
            });

            //博客分类组
            path("/category", () -> {

                get("", (CategoryController::categorys));

                post("/edit", (CategoryController::edit));

                post("/delete", (CategoryController::delete));
            });

            //博客标签组
            path("/tag", () -> {

                get("", (TagController::tags));

                post("/edit", (TagController::edit));

                post("/delete", (TagController::delete));
            });

            //afu组
            path("/afu", () -> {
                get("", (AfuController::afus));
                post("/edit", (AfuController::edit));
                post("/detail", (AfuController::detail));
                post("/delete", (AfuController::delete));

                post("/text", (AfuController::text));

                path("/type", () -> {

                    get("", (AfuTypeController::afuTypes));
                    post("/detail", (AfuTypeController::detail));
                    post("/edit", (AfuTypeController::edit));
                    post("/delete", (AfuTypeController::delete));
                });
            });
        });

        //afu
        path("/api", () -> {
            path("/afu", () -> {
                post("/list", (AfuApiController::apiAfus));
                post("/edit", (AfuApiController::apiAfusEdit));
                post("/delete", (AfuApiController::apiAfusDelete));
            });
        });

        //qrcode
        path("/qrcode", () -> {
            get("", ((request, response) ->
                    QrcodeController.qrcodeIndex(request, response)));
            get("/create", ((request, response) ->
                    QrcodeController.makeQrcode(request, response)));
            post("/create", ((request, response) ->
                    QrcodeController.makeQrcode(request, response)));
            get("/:id", ((request, response) ->
                    QrcodeController.showQrcodeLink(request, response)));
        });

    }
}
