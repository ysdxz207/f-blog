package com.puyixiaowo.fblog.controller.admin;

import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.utils.DBUtils;
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
    public static Object index(Request request, Response response) {

        Map<String ,Object> model = new HashMap<>();
        UserBean user = request.session().attribute(Constants.SESSION_USER_KEY);
        model.put("user", user);
        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(model, "admin/index.html"));
    }

    public static Object main(Request request, Response response) {

        Map<String, Object> model = new HashMap<>();

        Map<String, String> params = new HashMap<>();

        Integer indexCount = DBUtils.count("select * from access_record " +
                "where article_id = 0", params);


        model.put("indexCount", indexCount);
        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(model, "admin/main.html"));
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
