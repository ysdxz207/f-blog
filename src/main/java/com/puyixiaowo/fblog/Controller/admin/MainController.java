package com.puyixiaowo.fblog.Controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.fblog.Controller.BaseController;
import com.puyixiaowo.fblog.domain.User;
import com.puyixiaowo.fblog.utils.DBUtils;
import org.sql2o.Connection;
import spark.Request;
import spark.Response;

import java.util.List;

public class MainController extends BaseController {

    public static Object index(Request request, Response response) {


        return "main index";
    }

    public static String login(Request request,
                               Response response) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection conn = DBUtils.getSql2o().open()) {
            List<User> posts = conn.createQuery("select * from user;")
                    .executeAndFetch(User.class);
            return JSONObject.toJSONString(posts);
        }
    }
}
