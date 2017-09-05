package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.admin.TagBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.utils.DBUtils;

import java.util.List;

/**
 * 
 * @author Moses
 * @date 2017-09-05 22:29:28
 * 
 */
public class TagService {


    public static List<TagBean> selectTagList(TagBean tagBean,
                                                      PageBean pageBean) {

        StringBuilder sbSql = new StringBuilder("select * from tag where 1 = 1 ");

        buildSqlParams(sbSql, tagBean);
        sbSql.append(" order by id asc");
        sbSql.append(" limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return DBUtils.selectList(TagBean.class, sbSql.toString(), tagBean);
    }

    public static int selectCount(TagBean tagBean) {
        StringBuilder sbSql = new StringBuilder("select count(*) from tag where 1 = 1 ");

        buildSqlParams(sbSql, tagBean);
        return DBUtils.count(sbSql.toString(), tagBean);
    }

    public static void buildSqlParams(StringBuilder sbSql,
                                               TagBean tagBean) {
        if (tagBean.getName() != null) {
            sbSql.append("and tag like :tag ");
            tagBean.setName("%" + tagBean.getName() + "%");
        }
    }
}
