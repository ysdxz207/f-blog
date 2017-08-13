package com.puyixiaowo.fblog.error;

import spark.ModelAndView;

public class ErrorHandler {

    public static ModelAndView handle404(){
        return new ModelAndView(null, "error/404.html");
    }
}
