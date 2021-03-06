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
        logger.info("[test]：" + JSON.toJSONString(getParamsMap(request)));
        return "success";
    }

    public static Object testJson(Request request,
                                  Response response) {

        return "{\"awesome\":true,\"bogus\":false,\"chinese\":\"这是中文。\",\"notLink\":\"http://www.htmleaf.com/ is great\",\"multiline\":[\"Much like me, you make your way forward,\",\"Walking with downturned eyes.\",\"Well, I too kept mine lowered.\",\"Passer-by, stop here, please.\"],\"link\":\"http://img.1391.com/api/v1/bookcenter/cover/1/1127281/_1127281_685974.jpg/\",\"anobject\":{\"anarray\":[1,2,\"three\"],\"whoa\":\"nuts\",\"more\":\"stuff\"},\"hey\":\"guy\",\"anumber\":243}";
    }

    public static void main(String[] args) {

        String url = "jdbc:sqlite:fblog.db/dfdf";
//        String url = "jdbc:sqlite://fblog.db";

        int indexStart = url.indexOf("sqlite:");
        if (indexStart != -1) {
            indexStart += 3;
            int indexEnd = url.indexOf("/", indexStart);
            indexEnd = indexEnd == -1 ? url.length() : indexEnd;
            System.out.println(url.substring(indexStart, indexEnd));
        }
    }

}
