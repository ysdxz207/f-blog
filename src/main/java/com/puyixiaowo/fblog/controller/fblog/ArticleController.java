package com.puyixiaowo.fblog.controller.fblog;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.controller.BaseController;
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

    /**
     * 添加文章
     * @param request
     * @param response
     * @return
     */
    public static Object addOrUpdateArticle(Request request, Response response) {

        String update_sql = "update article set creator=:creator,title=:title," +
                "";
        String sql = "insert into artile values " +
                "(:creator,:title,:context,:category,:tagIds," +
                ":createDate,:lastUpdateDate,:status,:isDel) " +
                "on duplicate key update b=values(b),c=values(c)";

        String sql2 = "insert or replace into `artile` values ('feihong','我的第一篇文章','我的第一篇文章内容...','11111','测试','2017-08-08 14:36',null,1) on duplicate key update title='我的第一篇文章修改',context='我的第一篇文章内容修改'";
        return null;
    }


}
