package com.puyixiaowo.fblog.Controller.admin;

import com.puyixiaowo.fblog.Controller.BaseController;
import com.puyixiaowo.fblog.bean.ResponseBean;
import com.puyixiaowo.fblog.domain.User;
import com.puyixiaowo.fblog.service.LoginService;
import com.puyixiaowo.fblog.utils.Md5Utils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;


public class LoginController extends BaseController {

    public static Object index(Request request, Response response) {

        return "main index";
    }


    public static ModelAndView loginPage(Request request, Response response) {
        return new ModelAndView(null, "login.ftl");
    }

    public static String doLogin(Request request,
                               Response response) {
        ResponseBean responseBean = new ResponseBean();

        Map<String, Object> params = new HashMap<>();

        params.put("username", request.queryParams("uname"));
        params.put("password", Md5Utils.md5Password(request.queryParams("upass")));


        try {
            User user = LoginService.login(params);
            responseBean.success("登录成功");

        } catch (Exception e) {
            e.printStackTrace();
            responseBean.error("系统错误！");
        }

        return responseBean.toString();

    }

}
