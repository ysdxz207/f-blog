package com.puyixiaowo.fblog.controller.fblog;

import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.TagService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class FblogController {

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

    public static String tagTop(Request request, Response response) {
        String tagName = request.queryParams("tagName");
        return TagService.tagTop(tagName, true);
    }
}
