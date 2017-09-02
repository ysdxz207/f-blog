package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.utils.DBUtils;

import java.util.Map;

/**
 * @author feihong
 * @date 2017-08-06 21:57
 */
public class LoginService {

    public static UserBean login(Map<String ,Object> params){
        UserBean userBean = DBUtils.selectOne(UserBean.class,
                "SELECT\n" +
                        "  u.*,\n" +
                        "  r.id AS role_id\n" +
                        "FROM user u\n" +
                        "  LEFT JOIN user_role ur\n" +
                        "    ON u.id = ur.user_id\n" +
                        "  LEFT JOIN role r\n" +
                        "    ON r.id = ur.role_id\n" +
                        "WHERE loginname = :loginname\n" +
                        "      AND password = :password\n" +
                        "      AND status = 1;",
                params);
        return userBean;
    }
}
