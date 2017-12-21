package com.puyixiaowo.fblog.controller;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;


/**
 * 
 * @author Moses
 * @date 2017-12-19 17:59
 * 
 */
public class TestController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    public static Object test(Request request,
                       Response response) {
        logger.info("test===========>" + JSON.toJSONString(request.queryMap().toMap()));
        return "success";
    }
}
