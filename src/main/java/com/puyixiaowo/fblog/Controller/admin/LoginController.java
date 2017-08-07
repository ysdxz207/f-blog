package com.puyixiaowo.fblog.Controller.admin;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.Constants.Constants;
import com.puyixiaowo.fblog.Controller.BaseController;
import com.puyixiaowo.fblog.bean.ResponseBean;
import com.puyixiaowo.fblog.domain.Article;
import com.puyixiaowo.fblog.domain.User;
import com.puyixiaowo.fblog.service.LoginService;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.Md5Utils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginController extends BaseController {

    /**
     * 管理后台首页页面
     * @param request
     * @param response
     * @return
     */
    public static ModelAndView index(Request request, Response response) {

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> model = new HashMap<>();
        params.put("offset", 0);
        params.put("pageSize", 10);

        List<Article> articleList = DBUtils.selectList(Article.class,
                "select * from article limit :offset, :pageSize",
                params);

        model.put("articleList", JSON.toJSONString(articleList));

        return new ModelAndView(model, "index.ftl");
    }


    /**
     * 登录页面
     * @param request
     * @param response
     * @return
     */
    public static ModelAndView loginPage(Request request, Response response) {
        return new ModelAndView(null, "login.ftl");
    }

    /**
     * 登录
     * @param request
     * @param response
     * @return
     */
    public static String doLogin(Request request,
                               Response response) {
        ResponseBean responseBean = new ResponseBean();

        Map<String, Object> params = new HashMap<>();

        params.put("username", request.queryParams("uname"));
        params.put("password", Md5Utils.md5Password(request.queryParams("upass")));


        try {
            User user = LoginService.login(params);
            if (user != null) {
                responseBean.success("登录成功");
                request.session().attribute(Constants.SESSION_USER_KEY, user);
            } else {
                responseBean.error("用户名或密码不正确");
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseBean.error("系统错误！");
        }

        return responseBean.toString();

    }

    /**
     * 退出登录
     * @param request
     * @param response
     * @return
     */
    public static ModelAndView logout(Request request, Response response){
        request.session().removeAttribute(Constants.SESSION_USER_KEY);
        return new ModelAndView(null, "login.ftl");
    }

}
