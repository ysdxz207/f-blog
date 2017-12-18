package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.utils.DBUtils;

import java.util.HashMap;

/**
 * @author Moses
 * @date 2017-08-27
 */
public class RolePermissionService {

    public static Object deleteByRoleIds(String roleIds) {
        String sql = "delete from role_permission where role_id in (:roleIds)";
        return DBUtils.executeSql(sql, new HashMap<String, Object>() {
            {
                put("roleIds", roleIds);
            }
        });
    }

    public static Object deleteByUserIds(String userIds) {
        String sql = "delete from role_permission " +
                "where role_id in " +
                "(select role_id from user_role where user_id in (:userIds))";
        return DBUtils.executeSql(sql, new HashMap<String, Object>() {
            {
                put("userIds", userIds);
            }
        });
    }
}
