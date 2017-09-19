package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.admin.RoleBean;
import com.puyixiaowo.fblog.bean.admin.RolePermissionBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.utils.ArrayUtils;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.StringUtils;

import java.util.HashMap;

/**
 * @author Moses
 * @date 2017-08-19 09:35
 */
public class RoleService {


    public static String getSelectSql(RoleBean roleBean,
                                                      PageBean pageBean) {

        StringBuilder sbSql = new StringBuilder("select * from role where 1 = 1 ");

        buildSqlParams(sbSql, roleBean);
        sbSql.append(" order by id asc");
        sbSql.append(" limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return sbSql.toString();
    }

    public static void buildSqlParams(StringBuilder sbSql,
                                               RoleBean roleBean) {
        if (roleBean.getRoleName() != null) {
            sbSql.append("and role like :roleName ");
            roleBean.setRoleName("%" + roleBean.getRoleName() + "%");
        }
        if (roleBean.getCode() != null) {
            sbSql.append("and role like :code ");
            roleBean.setCode("%" + roleBean.getCode() + "%");
        }

    }

    public static RoleBean selectByPrimaryKey(Object roleId) {

        return DBUtils.selectOne(RoleBean.class, "select * from role where id=:id",
                new HashMap<String, Object>(){
                    {
                        put("id", roleId);
                    }
                });
    }

    public static void setPermission(Long roleId, String permissionIds) {

        if (StringUtils.isBlank(permissionIds)) {
            return;
        }

        //删除角色权限
        RolePermissionService.deleteByRoleIds(roleId.toString());

        long [] ids = ArrayUtils.parseToLongArray(permissionIds);

        //添加角色权限
        for (Long permissionId : ids) {
            RolePermissionBean bean = new RolePermissionBean();
            bean.setRoleId(roleId);
            bean.setPermissionId(permissionId);
            DBUtils.insertOrUpdate(bean);
        }
    }

    public static PageBean selectRolePageBean(RoleBean roleBean, PageBean pageBean) {
        return DBUtils.selectPageBean(getSelectSql(roleBean, pageBean), roleBean);
    }
}
