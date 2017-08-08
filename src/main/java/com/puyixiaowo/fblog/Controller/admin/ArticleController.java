package com.puyixiaowo.fblog.Controller.admin;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.Controller.BaseController;
import com.puyixiaowo.fblog.domain.Article;
import com.puyixiaowo.fblog.utils.DBUtils;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Moses
 * @date 2017-08-08 13:48:12
 * 文章
 */
public class ArticleController extends BaseController {

    /**
     * 文章列表
     * @param request
     * @param response
     * @return
     */
    public static Object selectArticleList(Request request, Response response) {

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> model = new HashMap<>();
        params.put("offset", 0);
        params.put("pageSize", 10);

        List<Article> articleList = DBUtils.selectList(Article.class,
                "select * from article limit :offset, :pageSize",
                params);

        return JSON.toJSONString(articleList);
    }
}
