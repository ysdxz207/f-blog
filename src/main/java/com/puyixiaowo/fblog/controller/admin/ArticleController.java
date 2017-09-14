package com.puyixiaowo.fblog.controller.admin;

import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.bean.ArticleBean;
import com.puyixiaowo.fblog.bean.admin.CategoryBean;
import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.ArticleService;
import com.puyixiaowo.fblog.service.TagService;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.LuceneIndexUtils;
import org.apache.commons.text.StringEscapeUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Moses
 * @date 2017-08-08 13:48:12
 * 文章
 */
public class ArticleController extends BaseController {

    /**
     * 文章列表
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions(value = {"article:view"})
    public static Object articles(Request request, Response response) {

        Boolean data = Boolean.valueOf(request.params(":data"));

        if (!data) {
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(null,
                            "admin/article/article_list.html"));
        }

        PageBean pageBean = getPageBean(request);
        try {
            ArticleBean params = getParamsEntity(request, ArticleBean.class, false);
            List<ArticleBean> list =
                    ArticleService.selectArticleList(params, pageBean);
            pageBean.setList(list);
            int count = ArticleService.selectCount(params);
            pageBean.setTotalCount(count);
        } catch (Exception e) {
            pageBean.error(e);
        }
        return pageBean.serialize();
    }

    /**
     * 添加或修改文章
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions(value = {"article:edit"})
    public static String edit(Request request, Response response) {
        Boolean data = Boolean.valueOf(request.params(":data"));

        if (!data) {
            Map<String, Object> model = new HashMap<>();
            ArticleBean articleBean = getParamsEntity(request, ArticleBean.class, false);
            if (articleBean.getId() > 0) {
                //编辑
                articleBean = DBUtils.selectOne(ArticleBean.class, "select a.*,group_concat(t.name) as tags " +
                        "from article a " +
                        "left join article_tag at " +
                        "on a.id = at.article_id " +
                        "left join tag t " +
                        "on at.tag_id = t.id where a.id = :id " +
                        "group by a.id", articleBean);
                model.put("model", articleBean);
            }

            //分类列表
            model.put("categoryList", DBUtils.selectList(CategoryBean.class,
                    "select * from category", null));

            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(model,
                            "admin/article/article_edit.html"));
        }

        ResponseBean responseBean = new ResponseBean();
        try {
            ArticleBean articleBean = getParamsEntity(request, ArticleBean.class, true);

            UserBean currentUser = request.session().attribute(Constants.SESSION_USER_KEY);
            articleBean.setCreator(currentUser.getLoginname());
            articleBean.setCreateDate(System.currentTimeMillis());
            if (articleBean.getId() != null) {
                articleBean.setLastUpdateDate(System.currentTimeMillis());
            }
            DBUtils.insertOrUpdate(articleBean);
            //标签
            TagService.insertArticleTags(articleBean);
            //lucene搜索引擎
            LuceneIndexUtils.addLuceneIndex(articleBean);
        } catch (Exception e) {
            responseBean.errorMessage(e.getMessage());
        }

        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"article:delete"})
    public static String delete(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();

        try {
            String ids = request.queryParams("id");
            DBUtils.deleteByIds(ArticleBean.class,
                    ids);
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"article:view"})
    public static String luceneReindex(Request request, Response response){
        ResponseBean responseBean = new ResponseBean();
        List<ArticleBean> list = ArticleService.selectArticleList(new ArticleBean(), new PageBean());

        try {
            //删除索引目录
            LuceneIndexUtils.deleteIndexDir();
            //添加索引
            for (ArticleBean articleBean :
                    list) {
                articleBean.setContext(StringEscapeUtils.unescapeHtml4(articleBean.getContext()));
                LuceneIndexUtils.addLuceneIndex(articleBean);
            }

        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }
}