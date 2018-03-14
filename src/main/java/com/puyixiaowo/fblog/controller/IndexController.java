package com.puyixiaowo.fblog.controller;

import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * 
 * @author Moses
 * @date 2017-09-21
 * 
 */
public class IndexController extends BaseController {

    public static Object index(Request request, Response response) {
        String str = null;
        System.out.println(str.equals(""));
        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(null, "index.html"));
    }
}
