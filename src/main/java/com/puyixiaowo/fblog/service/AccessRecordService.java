package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.AccessRecordBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.utils.DBUtils;

import java.util.List;

/**
 * 
 * @author Moses.wei
 * @date 2018-01-13 22:33:45
 * 
 */
public class AccessRecordService {

    public static String getSelectSql(AccessRecordBean accessRecordBean,
                                      PageBean pageBean) {
        StringBuilder sbSql = new StringBuilder("select * from access_record where 1 = 1 ");

        buildSqlParams(sbSql, accessRecordBean);
        sbSql.append("order by " +
                pageBean.getOrder() +
                (pageBean.getReverse() ? " desc " : " asc "));
        sbSql.append("limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return sbSql.toString();
    }

    public static PageBean selectAccessRecordPageBean (AccessRecordBean accessRecordBean,
                                                   PageBean pageBean) {
        return DBUtils.selectPageBean(getSelectSql(accessRecordBean, pageBean),
                accessRecordBean, pageBean);
    }

    public static void buildSqlParams(StringBuilder sbSql,
                                               AccessRecordBean accessRecordBean) {

        if (accessRecordBean.getArticleId() != null) {
            sbSql.append("and article_id = :articleId ");
        }

        if (accessRecordBean.getBrowser() != null) {
            sbSql.append("and browser like :browser ");
            accessRecordBean.setBrowser("%" + accessRecordBean.getBrowser() + "%");
        }

        if (accessRecordBean.getOs() != null) {
            sbSql.append("and os like :os ");
            accessRecordBean.setOs("%" + accessRecordBean.getOs() + "%");
        }

        if (accessRecordBean.getIp() != null) {
            sbSql.append("and ip like :ip ");
            accessRecordBean.setIp("%" + accessRecordBean.getIp() + "%");
        }

    }
}
