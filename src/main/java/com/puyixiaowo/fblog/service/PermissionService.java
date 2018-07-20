package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.admin.PermissionBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;

import java.util.List;

/**
 * @author Moses
 * @date 2017-08-19
 */
public class PermissionService {


    public static String getSelectSql(PermissionBean permissionBean,
                                                      PageBean pageBean) {

        StringBuilder sbSql = new StringBuilder("select * from permission where 1 = 1 ");

        buildSqlParams(sbSql, permissionBean);
        sbSql.append(" order by id asc");
        sbSql.append(" limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return sbSql.toString();
    }

    public static PageBean<PermissionBean> selectPermissionPageBean(PermissionBean permissionBean,
                                                                    PageBean<PermissionBean> pageBean){
        String sql = getSelectSql(permissionBean, pageBean);
        List<PermissionBean> list = permissionBean.selectList(sql);
        int count = permissionBean.count(sql);
        pageBean.setList(list);
        pageBean.setTotalCount(count);

        return pageBean;
    }
    public static void buildSqlParams(StringBuilder sbSql,
                                               PermissionBean permissionBean) {
        if (permissionBean.getMenuId() != null) {
            sbSql.append("and menu_id = :menuId ");
        }
        if (permissionBean.getPermission() != null) {
            sbSql.append("and permission like :permission ");
            permissionBean.setPermission("%" + permissionBean.getPermission() + "%");
        }
        if (permissionBean.getPermissionName() != null) {
            sbSql.append("and permission_name like :permissionName ");
            permissionBean.setPermissionName("%" + permissionBean.getPermissionName() + "%");
        }
    }
}
