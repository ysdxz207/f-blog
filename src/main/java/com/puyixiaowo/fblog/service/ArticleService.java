package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.ArticleBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.utils.DBUtils;

import java.util.List;

/**
 *
 * @author Moses
 * @date 2017-09-03 21:48:40
 * 
 */
public class ArticleService {

    public static List<ArticleBean> selectArticleList(ArticleBean articleBean,
                                                      PageBean pageBean){
        StringBuilder sbSql = new StringBuilder("select a.*,c.name as category,group_concat(t.name) as tags " +
                "from article a " +
                "left join category c " +
                "on a.category_id = c.id " +
                "left join article_tag at " +
                "on a.id = at.article_id " +
                "left join tag t " +
                "on at.tag_id = t.id where 1=1 " +
                "group by a.id");

        buildSqlParams(sbSql, articleBean);
        sbSql.append(" order by id asc");
        sbSql.append(" limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return DBUtils.selectList(ArticleBean.class, sbSql.toString(), articleBean);
    }

    public static int selectCount(ArticleBean articleBean) {
        StringBuilder sbSql = new StringBuilder("select count(id) " +
                "from article where 1=1 ");

        buildSqlParams(sbSql, articleBean);
        return DBUtils.count(sbSql.toString(), articleBean);
    }


    public static void buildSqlParams(StringBuilder sbSql,
                                      ArticleBean articleBean) {
        if (articleBean.getTitle() != null) {
            sbSql.append("and title like :title ");
            articleBean.setTitle("%" + articleBean.getTitle() + "%");
        }

        if (articleBean.getStatus() != null) {
            sbSql.append("and status = :status ");
        }
        if (articleBean.getIsDel() != null) {
            sbSql.append("and is_del = :isDel ");
        }
    }
}
