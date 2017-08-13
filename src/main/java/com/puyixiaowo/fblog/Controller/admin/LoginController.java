package com.puyixiaowo.fblog.Controller.admin;

import com.puyixiaowo.fblog.Constants.Constants;
import com.puyixiaowo.fblog.Controller.BaseController;
import com.puyixiaowo.fblog.domain.Role;
import com.puyixiaowo.fblog.domain.User;
import com.puyixiaowo.fblog.service.LoginService;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.Md5Utils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Moses
 * @date 2017-08-08 13:40:25
 * 登录
 */
public class LoginController extends BaseController {


    /**
     * 登录页面
     *
     * @param request
     * @param response
     * @return
     */
    public static ModelAndView loginPage(Request request, Response response) {
        return new ModelAndView(null, "login.ftl");
    }

    /**
     * 登录
     *
     * @param request
     * @param response
     * @return
     */
    public static ModelAndView doLogin(Request request,
                                       Response response) {

        Map<String, Object> model = new HashMap<>();
        Map<String, Object> params = new HashMap<>();

        params.put("loginname", request.queryParams("uname"));
        params.put("password", Md5Utils.md5Password(request.queryParams("upass")));

        User user = null;

        try {
            user = LoginService.login(params);
            if (user == null) {
                model.put("message", "用户名或密码不正确");
            } else {
                params.clear();
                params.put("userId", user.getId());
                Role role = DBUtils.selectOne(Role.class, "select * from role r " +
                        "left join user_role ur on r.id=ur.role_id where " +
                        "ur.user_id = :userId",
                        params);

                user.setRoleId(role.getId());
                user.setRoleName(role.getRoleName());
                request.session().attribute(Constants.SESSION_USER_KEY, user);
                response.redirect("/admin/");
                Spark.halt();
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.put("message", e.getMessage());
        }


        return new ModelAndView(model, "login.ftl");
    }

    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @return
     */
    public static ModelAndView logout(Request request, Response response) {
        request.session().removeAttribute(Constants.SESSION_USER_KEY);
        return new ModelAndView(null, "login.ftl");
    }

}
