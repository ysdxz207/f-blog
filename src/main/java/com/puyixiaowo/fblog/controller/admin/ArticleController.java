package com.puyixiaowo.fblog.controller.admin;

import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.bean.AccessRecordBean;
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
import com.puyixiaowo.fblog.utils.DateUtils;
import com.puyixiaowo.fblog.utils.LuceneIndexUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Moses
 * @date 2017-08-08
 * 文章
 */
public class ArticleController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

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
            pageBean = ArticleService.selectArticlePageBean(params, pageBean);
            for (Object obj : pageBean.getList()) {
                String sqlAcccessCount = "select * from access_record where article_id=:articleId ";
                ArticleBean articleBean = (ArticleBean) obj;
                AccessRecordBean accessCountParams = new AccessRecordBean();
                accessCountParams.setArticleId(articleBean.getId());

                //总访问量
                Integer accessCountAll = accessCountParams.count(sqlAcccessCount);

                sqlAcccessCount += "and access_date=:accessDate";
                accessCountParams.setAccessDate(DateUtils.getTodayZeroMiliseconds());
                //今天访问量
                Integer accessCountToday = accessCountParams.count(sqlAcccessCount);
                articleBean.setAccessCountAll(accessCountAll);
                articleBean.setAccessCountToday(accessCountToday);
            }
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
            ArticleBean articleBean = null;
            try {
                articleBean = getParamsEntity(request, ArticleBean.class, false);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
            if (articleBean.getId() != null) {
                //编辑
                articleBean.selectOne("select a.*,group_concat(t.name) as tags " +
                        "from article a " +
                        "left join article_tag at " +
                        "on a.id = at.article_id " +
                        "left join tag t " +
                        "on at.tag_id = t.id where a.id = :id " +
                        "group by a.id");
                String html = StringEscapeUtils.escapeHtml4(articleBean.getContext());
                articleBean.setContext(html);
                model.put("model", articleBean);
            }

            //分类列表
            model.put("categoryList", new CategoryBean().selectList("select * from category"));

            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(model,
                            "admin/article/article_edit.html"));
        }

        ResponseBean responseBean = new ResponseBean();
        try {
            ArticleBean articleBean = getParamsEntity(request, ArticleBean.class, true);
            UserBean currentUser = request.session().attribute(Constants.SESSION_USER_KEY);

            if (StringUtils.isNotBlank(articleBean.getId())) {
                ArticleBean bean = articleBean.selectOne("select * from article where id=:id");
                if (bean == null) {
                    responseBean.errorMessage("文章不存在！");
                    return responseBean.serialize();
                }

                articleBean.setCreateDate(bean.getCreateDate());
                articleBean.setCreator(bean.getCreator());
            } else {
                articleBean.setCreator(currentUser.getLoginname());
                articleBean.setCreateDate(System.currentTimeMillis());
            }
            articleBean.setLastUpdateDate(System.currentTimeMillis());

            articleBean.insertOrUpdate(false);
            //标签
            TagService.insertArticleTags(articleBean);
            //lucene搜索引擎
            LuceneIndexUtils.dealLuceneIndex(articleBean);
        } catch (Exception e) {
            responseBean.errorMessage(e.getMessage());
            logger.error("编辑文章异常：", e);
        }
        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"article:delete"})
    public static String delete(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();


        try {
            String ids = request.queryParams("id");
            if (StringUtils.isNotBlank(ids)) {
                new ArticleBean().deleteByIds(ids.split(","));
                //删除lucene索引
                for (String id :
                        ids.split(",")) {
                    LuceneIndexUtils.deleteLuceneIndex(id);
                }
            }

        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"article:view"})
    public static String luceneReindex(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();
        ArticleBean params = new ArticleBean();
        params.setStatus(1);//发布状态
        List<ArticleBean> list = ArticleService.selectArticleList(params, new PageBean());

        try {
            //删除索引目录
            LuceneIndexUtils.deleteIndexDir();
            //添加索引
            for (ArticleBean articleBean :
                    list) {
                LuceneIndexUtils.dealLuceneIndex(articleBean);
            }

        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }
}
