package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.admin.afu.AfuBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;

import java.util.List;

/**
 * 
 * @author Moses
 * @date 2017-09-05
 * 
 */
public class AfuService {

    public static String getSelectSql(AfuBean afuBean,
                                                      PageBean pageBean) {

        StringBuilder sbSql = new StringBuilder("select t.* from afu t " +
                "left join afu_type at " +
                "on t.type = at.id where 1 = 1 ");

        buildSqlParams(sbSql, afuBean);
        sbSql.append("group by t.id ");
        sbSql.append("order by t.id desc ");
        sbSql.append("limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return sbSql.toString();
    }
    public static PageBean<AfuBean> selectAfuPageBean(AfuBean afuBean, PageBean<AfuBean> pageBean){
        String sql = getSelectSql(afuBean, pageBean);
        List<AfuBean> list = afuBean.selectList(sql);
        int count = afuBean.count(sql);
        pageBean.setList(list);
        pageBean.setTotalCount(count);
        return pageBean;
    }
    public static void buildSqlParams(StringBuilder sbSql,
                                               AfuBean afuBean) {
        if (afuBean.getName() != null) {
            sbSql.append("and t.name like :name ");
            afuBean.setName("%" + afuBean.getName() + "%");
        }

        if (afuBean.getType() != null) {
            sbSql.append("and t.type = :type ");
        }
    }
}
