package com.puyixiaowo.fblog.controller;

import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 *
 * @author Moses.wei
 * @date 2018-03-13 23:03:10
 *
 */
public class ErrorController extends BaseController {

    public static Object error404(Request request, Response response) {
        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(null, "error/404.html"));
    }

    public static Object error500(Request request, Response response) {
        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(null, "error/500.html"));
    }
}
