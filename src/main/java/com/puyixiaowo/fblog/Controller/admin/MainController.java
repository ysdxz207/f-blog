package com.puyixiaowo.fblog.Controller.admin;

import com.puyixiaowo.fblog.Controller.BaseController;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 *
 * @author Moses
 * @date 2017-08-08 13:40:41
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

        return new ModelAndView(null, "index.ftl");
    }
}
