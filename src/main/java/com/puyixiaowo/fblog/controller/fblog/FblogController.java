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
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.List;

public class FblogController extends BaseController{

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

    public static String categoryList(Request request, Response response) {
        List<CategoryBean> list = CategoryService.selectCategoryList(new CategoryBean(), new PageBean());
        return JSON.toJSONString(list);
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

        List<ArticleBean> list = ArticleService.selectArticleList(new ArticleBean(),
                pageBean);
        pageBean.setList(list);
        pageBean.setTotalCount(ArticleService.selectCount(new ArticleBean()));
        return pageBean.serialize();
    }
}
