package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.admin.afu.AfuTypeBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;

import java.util.List;

/**
 * 
 * @author Moses
 * @date 2017-09-05
 * 
 */
public class AfuTypeService {

    public static AfuTypeBean getAfuTypeById(String id) {
        AfuTypeBean afuTypeBean = new AfuTypeBean();
        afuTypeBean.setId(id);
        return afuTypeBean.selectOne("select * from afu_type where id=:id");
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
    public static PageBean<AfuTypeBean> selectAfuTypePageBean(AfuTypeBean afuTypeBean,
                                                              PageBean<AfuTypeBean> pageBean){

        String sql = getSelectSql(afuTypeBean, pageBean);
        List<AfuTypeBean> list = afuTypeBean.selectList(sql);
        int count = afuTypeBean.count(sql);
        pageBean.setList(list);
        pageBean.setTotalCount(count);
        return pageBean;
    }
    public static void buildSqlParams(StringBuilder sbSql,
                                               AfuTypeBean afuTypeBean) {
        if (afuTypeBean.getName() != null) {
            sbSql.append("and name like :afu ");
            afuTypeBean.setName("%" + afuTypeBean.getName() + "%");
        }
    }
}
