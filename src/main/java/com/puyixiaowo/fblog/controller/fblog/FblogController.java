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
import com.puyixiaowo.fblog.utils.LuceneIndexUtils;
import org.apache.commons.text.StringEscapeUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

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
    public static Object articleList(Request request, Response response){

        Map<String, Object> model = new HashMap<>();
        //查询文章列表,标签和分类通过ajax获取
        PageBean pageBean = getPageBean(request);

        ArticleBean articleBean = getParamsEntity(request, ArticleBean.class, false);

        articleBean.setStatus(1);//发布状态
        List<ArticleBean> list = ArticleService.selectArticleList(articleBean,
                pageBean);
        pageBean.setList(list);
        pageBean.setTotalCount(ArticleService.selectCount(articleBean));

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


    public static Object articleDetail(Request request, Response response) {
        Map<String, Object> model = new HashMap<>();

        ArticleBean articleBean = getParamsEntity(request, ArticleBean.class, false);
        articleBean.setStatus(1);//发布状态
        articleBean = DBUtils.selectOne(ArticleBean.class, "select a.*,group_concat(t.name) as tags " +
                "from article a " +
                "left join article_tag at " +
                "on a.id = at.article_id " +
                "left join tag t " +
                "on at.tag_id = t.id where a.id = :id " +
                "group by a.id", articleBean);

        if (articleBean == null) {
            Spark.halt("文章不存在");
        }
        articleBean.setContext(StringEscapeUtils.unescapeHtml4(articleBean.getContext()));
        model.put("model", JSON.parseObject(JSON.toJSONString(articleBean)));
        return new FreeMarkerTemplateEngine().render(
                new ModelAndView(model, "index.html")
        );
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

    public static Object search(Request request, Response response){
        Map<String, Object> model = new HashMap<>();

        String words = request.queryParamOrDefault("search", "");
        PageBean pageBean = getPageBean(request);

        try {
            pageBean = LuceneIndexUtils.search(pageBean, words);
        } catch (Exception e) {
            pageBean.error(e);
        }

        model.put("search", words);
        model.put("pageBean", pageBean);
        return new FreeMarkerTemplateEngine().render(
                new ModelAndView(model, "index.html")
        );
    }
}
