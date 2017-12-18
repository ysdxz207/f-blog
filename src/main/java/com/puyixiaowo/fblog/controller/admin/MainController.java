package com.puyixiaowo.fblog.controller.admin;

import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.bean.admin.UserBean;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Moses
 * @date 2017-08-08
 * 首页数据处理
 */
public class MainController extends BaseController {

    /**
     * 管理后台首页页面
     * @param request
     * @param response
     * @return
     */
    public static ModelAndView index(Request request, Response response) {

        Map<String ,Object> model = new HashMap<>();
        UserBean user = request.session().attribute(Constants.SESSION_USER_KEY);
        model.put("user", user);
        return new ModelAndView(model, "admin/index.html");
    }

    public static ModelAndView main(Request request, Response response) {

        return new ModelAndView(null, "admin/main.html");
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public static ModelAndView editUserCurrent(Request request, Response response) {
        Map<String ,Object> model = new HashMap<>();
        UserBean user = request.session().attribute(Constants.SESSION_USER_KEY);
        model.put("user", user);
        return new ModelAndView(model, "admin/user/user_edit_current.html");
    }


}
