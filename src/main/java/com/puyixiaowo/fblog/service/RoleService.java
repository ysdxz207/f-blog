package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.admin.RoleBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.utils.DBUtils;

import java.util.List;

/**
 * @author Moses
 * @date 2017-08-19 09:35
 */
public class RoleService {


    public static List<RoleBean> selectRoleList(RoleBean roleBean,
                                                      PageBean pageBean) {

        StringBuilder sbSql = new StringBuilder("select * from role where 1 = 1 ");

        buildSqlParams(sbSql, roleBean);
        sbSql.append(" order by id asc");
        sbSql.append(" limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return DBUtils.selectList(RoleBean.class, sbSql.toString(), roleBean);
    }

    public static int selectCount(RoleBean roleBean) {
        StringBuilder sbSql = new StringBuilder("select count(*) from role where 1 = 1 ");

        buildSqlParams(sbSql, roleBean);
        return DBUtils.count(sbSql.toString(), roleBean);
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
}
