package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.annotation.admin.Logical;
import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.ListUtils;
import spark.Request;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Moses
 * @date 2017-08-19
 */
public class UserService {

    public static String getSelectSql(UserBean userBean,
                                      PageBean pageBean){
        StringBuilder sbSql = new StringBuilder("select u.*,ur.role_id " +
                "from user u left join user_role ur on u.id=ur.user_id where loginname<>'feihong' ");

        buildSqlParams(sbSql, userBean);
        sbSql.append("order by id asc ");
        sbSql.append("limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return sbSql.toString();
    }

    public static PageBean selectUserPageBean(UserBean userBean, PageBean pageBean){
        return DBUtils.selectPageBean(getSelectSql(userBean, pageBean), userBean, pageBean);
    }

    public static void buildSqlParams(StringBuilder sbSql,
                                      UserBean userBean) {
        if (userBean.getLoginname() != null) {
            sbSql.append("and loginname like :loginname ");
            userBean.setLoginname("%" + userBean.getLoginname() + "%");
        }
        if (userBean.getNickname() != null) {
            sbSql.append("and nickname like :nickname ");
            userBean.setNickname("%" + userBean.getNickname() + "%");
        }
        if (userBean.getCreateTime() != null) {
            sbSql.append("and creat_time = :createTime ");
        }
        if (userBean.getStatus() != null) {
            sbSql.append("and status = :status ");
        }
        ///
        if (userBean.getRoleId() != null) {
            sbSql.append("and role_id = :roleId ");
        }
    }

    /**
     * 判断当前登录用户是否拥有权限
     * @param request
     * @param permissions
     * @return
     */
    public static boolean currentUserHasPermissions(Request request,
                                                    String[] permissions,
                                                    Logical logical) {
        UserBean userBean = request.session().attribute(Constants.SESSION_USER_KEY);

        if (userBean == null) {
            return false;
        }

        if (userBean.getId().equals(20151106L)) {
            //超管
            return true;
        }
        String sql = "select p.permission from role_permission rp " +
                "left join permission p " +
                "on rp.permission_id = p.id where role_id = :roleId";

        List<String> permissionList = DBUtils.selectList(String.class, sql,
                new HashMap<String, Object>(){
                    {
                        put("roleId", userBean.getRoleId());
                    }
                });

        if (Logical.AND.equals(logical)) {

            return permissionList.containsAll(Arrays.asList(permissions));
        }

        return ListUtils.containsAny(permissionList, Arrays.asList(permissions));
    }
}
