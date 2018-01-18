package com.puyixiaowo.fblog.controller.fblog;

import com.puyixiaowo.fblog.bean.ArticleBean;
import com.puyixiaowo.fblog.bean.admin.CategoryBean;
import com.puyixiaowo.fblog.bean.admin.TagBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.ArticleService;
import com.puyixiaowo.fblog.service.CategoryService;
import com.puyixiaowo.fblog.service.TagService;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.LuceneIndexUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class FblogController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(FblogController.class);

    /**
     * 首页
     * @param request
     * @param response
     * @return
     */
    public static Object articleList(Request request, Response response){

        Map<String, Object> model = new HashMap<>();
        //查询文章列表,标签和分类通过ajax获取
        PageBean<ArticleBean> pageBean = getPageBean(request);

        try {
            ArticleBean articleBean = getParamsEntity(request, ArticleBean.class, false);

            articleBean.setStatus(1);//发布状态
            articleBean.setType("yiyi");
            pageBean = ArticleService.selectArticlePageBean(articleBean, pageBean);
            for (ArticleBean bean :
                    pageBean.getList()) {
                String html = StringEscapeUtils.unescapeHtml4(bean.getContext());
                bean.setContext(StringUtils.delHTMLTag(html));
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        model.put("pageBean", pageBean);

        saveAccessRecord(request, 0L);

        return new FreeMarkerTemplateEngine().render(
                new ModelAndView(model, "yiyi/index.html")
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

        ArticleBean articleBean = null;
        try {
            articleBean = getParamsEntity(request, ArticleBean.class, false);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        articleBean.setStatus(1);//发布状态
        articleBean = DBUtils.selectOne("select a.*,group_concat(t.name) as tags " +
                "from article a " +
                "left join article_tag at " +
                "on a.id = at.article_id " +
                "left join tag t " +
                "on at.tag_id = t.id where a.id = :id " +
                "group by a.id", articleBean);

        if (articleBean == null) {
            Spark.halt("文章不存在");
        }
        articleBean.setContext(articleBean.getContext());

        model.put("model", articleBean);
        saveAccessRecord(request, articleBean.getId());
        return new FreeMarkerTemplateEngine().render(
                new ModelAndView(model, "yiyi/index.html")
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
        try {
            return CategoryService.selectCategoryPageBean(
                    getParamsEntity(request, CategoryBean.class, false), pageBean).serialize();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return "";
    }

    public static Object search(Request request, Response response){
        Map<String, Object> model = new HashMap<>();

        String words = request.queryParamOrDefault("search", "");
        PageBean pageBean = getPageBean(request);

        try {
            pageBean = LuceneIndexUtils.search(pageBean, words, "yiyi");
        } catch (Exception e) {
            pageBean.error(e);
        }

        model.put("search", words);
        model.put("pageBean", pageBean);
        return new FreeMarkerTemplateEngine().render(
                new ModelAndView(model, "yiyi/index.html")
        );
    }

    public static String articleTags(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();

        PageBean pageBean = new PageBean();
        try {
            TagBean tagBean = getParamsEntity(request, TagBean.class, false);
            pageBean = TagService.selectTagPageBean(tagBean, pageBean);
            responseBean.setData(pageBean.getList());
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }
}
