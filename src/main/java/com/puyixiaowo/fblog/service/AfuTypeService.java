package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.admin.afu.AfuTypeBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.utils.DBUtils;

/**
 * 
 * @author Moses
 * @date 2017-09-05 22:29:28
 * 
 */
public class AfuTypeService {

    public static AfuTypeBean getAfuTypeByName(String typeName) {
        AfuTypeBean afuTypeBean = new AfuTypeBean();
        afuTypeBean.setName(typeName);
        return DBUtils.selectOne("select * from afu_type where name=:name", afuTypeBean);
    }

    public static String getSelectSql(AfuTypeBean afuTypeBean,
                                                      PageBean pageBean) {

        StringBuilder sbSql = new StringBuilder("select * from afu_type t where 1 = 1 ");

        buildSqlParams(sbSql, afuTypeBean);
        sbSql.append("group by t.id ");
        sbSql.append("order by t.id asc ");
        sbSql.append("limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return sbSql.toString();
    }
    public static PageBean selectAfuTypePageBean(AfuTypeBean afuTypeBean, PageBean pageBean){
        return DBUtils.selectPageBean(getSelectSql(afuTypeBean, pageBean), afuTypeBean, pageBean);
    }
    public static void buildSqlParams(StringBuilder sbSql,
                                               AfuTypeBean afuTypeBean) {
        if (afuTypeBean.getName() != null) {
            sbSql.append("and name like :afu ");
            afuTypeBean.setName("%" + afuTypeBean.getName() + "%");
        }
    }
}
