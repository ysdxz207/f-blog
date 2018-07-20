package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.admin.RoleBean;
import com.puyixiaowo.fblog.bean.admin.RolePermissionBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import win.hupubao.common.utils.StringUtils;

import java.util.List;

/**
 * @author Moses
 * @date 2017-08-19
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

    public static RoleBean selectByPrimaryKey(String roleId) {

        RoleBean roleBean = new RoleBean();
        roleBean.setId(roleId);
        return roleBean.selectOne("select * from role where id=:id");
    }

    public static void setPermission(String roleId, String permissionIds) {

        if (StringUtils.isBlank(permissionIds)) {
            return;
        }

        //删除角色权限
        RolePermissionService.deleteByRoleIds(roleId);

        String [] ids = permissionIds.split(",");

        //添加角色权限
        for (String permissionId : ids) {
            RolePermissionBean bean = new RolePermissionBean();
            bean.setRoleId(roleId);
            bean.setPermissionId(permissionId);
            bean.insertOrUpdate(false);
        }
    }

    public static PageBean<RoleBean> selectRolePageBean(RoleBean roleBean,
                                              PageBean<RoleBean> pageBean) {

        String sql = getSelectSql(roleBean, pageBean);
        List<RoleBean> list = roleBean.selectList(sql);
        int count = roleBean.count(sql);
        pageBean.setList(list);
        pageBean.setTotalCount(count);

        return pageBean;
    }
}
