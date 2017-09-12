package com.puyixiaowo.fblog.controller.fblog;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.bean.ArticleBean;
import com.puyixiaowo.fblog.bean.admin.CategoryBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.ArticleService;
import com.puyixiaowo.fblog.service.CategoryService;
import com.puyixiaowo.fblog.service.TagService;
import com.puyixiaowo.fblog.utils.DBUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FblogController extends BaseController{

    /**
     * 首页
     * @param request
     * @param response
     * @return
     */
    public static Object index(Request request, Response response){

        Map<String, Object> model = new HashMap<>();
        //查询文章列表,标签和分类通过ajax获取
        PageBean pageBean = getPageBean(request);

        ArticleBean articleBean = getParamsEntity(request, ArticleBean.class, false);

        List<ArticleBean> list = ArticleService.selectArticleList(articleBean,
                pageBean);
        pageBean.setList(list);
        pageBean.setTotalCount(ArticleService.selectCount(new ArticleBean()));

        model.put("pageBean", pageBean);
        return new FreeMarkerTemplateEngine().render(
                new ModelAndView(model, "index.html")
        );
    }

    /**
     * 标签
     * @param request
     * @param response
     * @return
     */
    public static String tagTop(Request request, Response response) {
        String tagName = request.queryParams("tagName");
        Integer num = Integer.parseInt(request.queryParamOrDefault("num", "10"));
        return TagService.tagTop(tagName, num, true);
    }

    /**
     * 文章列表
     * @param request
     * @param response
     * @return
     */
    public static String articleList(Request request, Response response) {
        PageBean pageBean = getPageBean(request);

        ArticleBean articleBean = getParamsEntity(request, ArticleBean.class, false);

        List<ArticleBean> list = ArticleService.selectArticleList(articleBean,
                pageBean);
        pageBean.setList(list);
        pageBean.setTotalCount(ArticleService.selectCount(new ArticleBean()));
        return pageBean.serialize();
    }

    public static String articleDetail(Request request, Response response) {
        ArticleBean articleBean = getParamsEntity(request, ArticleBean.class, false);

        articleBean = DBUtils.selectOne(ArticleBean.class, "select a.*,group_concat(t.name) as tags " +
                "from article a " +
                "left join article_tag at " +
                "on a.id = at.article_id " +
                "left join tag t " +
                "on at.tag_id = t.id where a.id = :id " +
                "group by a.id", articleBean);

        return JSON.toJSONString(articleBean);
    }
    /**
     * 分类
     * @param request
     * @param response
     * @return
     */
    public static String categoryList(Request request, Response response) {

        PageBean pageBean = getPageBean(request);
        return CategoryService.selectCategoryListPage(
                getParamsEntity(request, CategoryBean.class, false), pageBean);
    }
}
