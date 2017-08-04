package com.puyixiaowo.fblog.Controller.admin;

import com.puyixiaowo.fblog.Controller.BaseController;
import com.puyixiaowo.fblog.domain.User;
import spark.Request;
import spark.Response;

public class MainController extends BaseController{

    public static Object index(Request request, Response response) {


        return "main index";
    }

    public static void login(User user,
                             Request request,
                             Response response){

    }
}
