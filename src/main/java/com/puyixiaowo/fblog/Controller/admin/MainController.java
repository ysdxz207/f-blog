package com.puyixiaowo.fblog.Controller.admin;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.Controller.BaseController;
import com.puyixiaowo.fblog.domain.Article;
import com.puyixiaowo.fblog.utils.DBUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainController extends BaseController {

    /**
     * 管理后台首页页面
     * @param request
     * @param response
     * @return
     */
    public static ModelAndView index(Request request, Response response) {

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> model = new HashMap<>();
        params.put("offset", 0);
        params.put("pageSize", 10);

        List<Article> articleList = DBUtils.selectList(Article.class,
                "select * from article limit :offset, :pageSize",
                params);

        model.put("articleList", JSON.toJSONString(articleList));

        return new ModelAndView(model, "index.ftl");
    }

}
