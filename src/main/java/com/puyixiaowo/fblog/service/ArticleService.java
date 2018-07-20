package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.ArticleBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;

import java.util.List;

/**
 * @author Moses
 * @date 2017-09-03
 */
public class ArticleService {

    public static String getSelectSql(ArticleBean articleBean,
                                      PageBean pageBean) {
        StringBuilder sbSql = new StringBuilder("select a.*,c.name as category,group_concat(t.name) as tags " +
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

        if (articleBean.getId() != null) {
            sbSql.append("and a.id = :id ");
        }

        if (articleBean.getTitle() != null) {
            sbSql.append("and title like :title ");
            articleBean.setTitle("%" + articleBean.getTitle() + "%");
        }

        if (articleBean.getStatus() != null) {
            sbSql.append("and status = :status ");
        }

        if (articleBean.getCategory() != null) {
            sbSql.append("and c.name = :category ");
        }

        if (articleBean.getCategoryId() != null) {
            sbSql.append("and a.category_id = :categoryId ");
        }
        if (articleBean.getType() != null) {
            sbSql.append("and a.type = :type ");
        }
        if (articleBean.getTags() != null) {
            sbSql.append("and t.name = :tags ");
        }
    }

}
