package com.puyixiaowo.fblog.service;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.bean.ArticleBean;
import com.puyixiaowo.fblog.bean.admin.ArticleTagBean;
import com.puyixiaowo.fblog.bean.admin.TagBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Moses
 * @date 2017-09-05
 * 
 */
public class TagService {

    public static void insertArticleTags(ArticleBean articleBean) {
        if(articleBean == null ||
                StringUtils.isBlank(articleBean.getTags())) {
            return;
        }

        List<String> tagNameList = Arrays.asList(articleBean.getTags().split(","));

        //删除文章关联的已存在的标签
        if (articleBean.getId() != null) {
            DBUtils.executeSql("delete from article_tag " +
                    "where article_id = :id",
                    articleBean);
        }
        //创建并关联标签
        for (String tagName : tagNameList) {
            TagBean tagBean = new TagBean();
            tagBean.setName(tagName);
            TagBean tagDb = DBUtils.selectOne("select * from tag where name=:name", tagBean);
            if (tagDb == null) {
                DBUtils.insertOrUpdate(tagBean, false);
            } else {
                tagBean = tagDb;
            }

            ArticleTagBean articleTagBean = new ArticleTagBean();
            articleTagBean.setArticleId(articleBean.getId());
            articleTagBean.setTagId(tagBean.getId());
            DBUtils.insertOrUpdate(articleTagBean, false);
        }
    }


    public static String getSelectSql(TagBean tagBean,
                                                      PageBean pageBean) {

        StringBuilder sbSql = new StringBuilder("select t.* from tag t " +
                "left join article_tag at " +
                "on t.id = at.tag_id where 1 = 1 ");

        buildSqlParams(sbSql, tagBean);
        sbSql.append("group by t.id ");
        sbSql.append("order by t.id asc ");
        sbSql.append("limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return sbSql.toString();
    }
    public static PageBean selectTagPageBean(TagBean tagBean, PageBean pageBean){
        return DBUtils.selectPageBean(getSelectSql(tagBean, pageBean), tagBean, pageBean);
    }
    public static void buildSqlParams(StringBuilder sbSql,
                                               TagBean tagBean) {
        if (tagBean.getName() != null) {
            sbSql.append("and tag like :tag ");
            tagBean.setName("%" + tagBean.getName() + "%");
        }

        if (tagBean.getArticleId() != null) {
            sbSql.append("and article_id = :articleId ");
        }
    }

    public static String tagTop(String tagName,
                                Integer num,
                                boolean isBean){
        Map<String, Object> params = new HashMap<>();
        String sql = "select " +
                (isBean ? "t.*":"t.name") + " from tag t\n" +
                "  left join\n" +
                "(select at.*,count(at.id) as pop\n" +
                "from article_tag at\n" +
                "group by at.tag_id\n" +
                "order by pop desc) mt\n" +
                "on t.id = mt.tag_id where 1 = 1 ";

        if (StringUtils.isNotBlank(tagName)) {
            sql += " and name like :tagName ";
            params.put("tagName", "%" + tagName + "%");

        }

        sql += "limit " + (num == null ? 10 : num);
        if (isBean) {
            List<TagBean> list = DBUtils.selectList(TagBean.class, sql, params);
            return JSON.toJSONString(list);
        }
        List<String> list = DBUtils.selectList(String.class,
                sql, params);
        return JSON.toJSONString(list);
    }
}
