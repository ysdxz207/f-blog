package com.puyixiaowo.fblog.controller.fblog;

import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class IndexController {

    /**
     * 首页
     * @param request
     * @param response
     * @return
     */
    public static Object index(Request request, Response response){

        return new FreeMarkerTemplateEngine().render(
                new ModelAndView(null, "index.html")
        );
    }
}
