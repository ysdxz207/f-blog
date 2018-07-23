package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.ArticleBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import win.hupubao.common.utils.StringUtils;

import java.util.List;

/**
 * @author Moses
 * @date 2017-09-03
 */
public class ArticleService {

    public static String getSelectSql(ArticleBean articleBean,
                                      PageBean pageBean) {
        StringBuilder sbSql = new StringBuilder("select a.*,c.name as category,t.* " +
                "from article a " +
                "left join category c " +
                "on a.category_id = c.id " +
                "left join article_tag at " +
                "on a.id = at.article_id " +
                "left join tag t " +
                "on at.tag_id = t.id where 1=1 ");

        buildSqlParams(sbSql, articleBean);

        sbSql.append("group by a.id order by a.create_date desc ");
        sbSql.append(" limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return sbSql.toString();
    }

    public static List<ArticleBean> selectArticleList(ArticleBean articleBean,
                                                      PageBean pageBean) {
        return articleBean.selectList(getSelectSql(articleBean, pageBean));
    }

    public static PageBean<ArticleBean> selectArticlePageBean(ArticleBean articleBean,
                                                      PageBean<ArticleBean> pageBean) {
        String sql = getSelectSql(articleBean, pageBean);
        List<ArticleBean> list = articleBean.selectList(sql);
        int count = articleBean.count(sql);
        pageBean.setList(list);
        pageBean.setTotalCount(count);
        return pageBean;
    }

    public static void buildSqlParams(StringBuilder sbSql,
                                      ArticleBean articleBean) {

        if (StringUtils.isNotBlank(articleBean.getId())) {
            sbSql.append("and a.id = :id ");
        }

        if (StringUtils.isNotBlank(articleBean.getTitle())) {
            sbSql.append("and title like :title ");
            articleBean.setTitle("%" + articleBean.getTitle() + "%");
        }

        if (StringUtils.isNotBlank(articleBean.getStatus())) {
            sbSql.append("and status = :status ");
        }

        if (StringUtils.isNotBlank(articleBean.getCategory())) {
            sbSql.append("and c.name = :category ");
        }

        if (StringUtils.isNotBlank(articleBean.getCategoryId())) {
            sbSql.append("and a.category_id = :categoryId ");
        }
        if (StringUtils.isNotBlank(articleBean.getType())) {
            sbSql.append("and a.type = :type ");
        }
        if (StringUtils.isNotBlank(articleBean.getTagId())) {
            sbSql.append("and t.id = :tagId ");
        }

    }

}
