package com.puyixiaowo.fblog.Controller.admin;

import com.puyixiaowo.fblog.Controller.BaseController;
import spark.Request;
import spark.Response;

/**
 * @author feihong
 * @date 2017-08-13 18:58
 */
public class MenuController extends BaseController {

    public static Object menus(Request request, Response response) {
        int type = Integer.parseInt(request.queryParams(":type"));

    return null;
    }
}
