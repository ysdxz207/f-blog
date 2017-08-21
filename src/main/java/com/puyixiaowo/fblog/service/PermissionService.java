package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.admin.PermissionBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.utils.DBUtils;

import java.util.List;

/**
 * @author Moses
 * @date 2017-08-19 09:35
 */
public class PermissionService {


    public static List<PermissionBean> selectPermissionList(PermissionBean permissionBean,
                                                      PageBean pageBean) {

        StringBuilder sbSql = new StringBuilder("select * from permission where 1 = 1 ");

        buildSqlParams(sbSql, permissionBean);
        sbSql.append(" order by id asc");
        sbSql.append(" limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return DBUtils.selectList(PermissionBean.class, sbSql.toString(), permissionBean);
    }

    public static int selectCount(PermissionBean permissionBean) {
        StringBuilder sbSql = new StringBuilder("select count(*) from permission where 1 = 1 ");

        buildSqlParams(sbSql, permissionBean);
        return DBUtils.count(sbSql.toString(), permissionBean);
    }

    public static void buildSqlParams(StringBuilder sbSql,
                                               PermissionBean permissionBean) {
        if (permissionBean.getMenuId() != null) {
            sbSql.append("and menu_id = :menuId ");
        }
        if (permissionBean.getPermission() != null) {
            sbSql.append("and permission like '%");
            sbSql.append(permissionBean.getPermission());
            sbSql.append("%' ");
            permissionBean.setPermission(null);
        }
        if (permissionBean.getPermissionName() != null) {
            sbSql.append("and permission_name like '%");
            sbSql.append(permissionBean.getPermissionName());
            sbSql.append("%' ");
            permissionBean.setPermissionName(null);
        }
    }
}
