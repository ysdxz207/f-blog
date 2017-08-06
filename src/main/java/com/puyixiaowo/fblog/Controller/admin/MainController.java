package com.puyixiaowo.fblog.Controller.admin;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.Controller.BaseController;
import com.puyixiaowo.fblog.domain.User;
import com.puyixiaowo.fblog.utils.DBUtils;
import spark.Request;
import spark.Response;


public class MainController extends BaseController {

    public static Object index(Request request, Response response) {

        return "main index";
    }

    public static String login(Request request,
                               Response response) {

        return JSON.toJSONString(DBUtils.selectList("select * from user;", User.class));
    }
}
