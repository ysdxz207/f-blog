package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.utils.DBUtils;

import java.util.HashMap;

/**
 * @author Moses
 * @date 2017-08-27
 */
public class UserRoleService {

    public static Object deleteByUserIds(String userIds) {
        return DBUtils.executeSql("delete from user_role where user_id in (:userIds)",
                new HashMap<String, Object>(){
                    {
                        put("userIds", userIds);
                    }
                });
    }
}
