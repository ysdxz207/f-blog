package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.domain.User;
import com.puyixiaowo.fblog.utils.DBUtils;

import java.util.List;
import java.util.Map;

/**
 * @author feihong
 * @date 2017-08-06 21:57
 */
public class LoginService {

    public static User login(Map<String ,Object> params){
        List<User> userList = DBUtils.selectList(User.class,
                "select username, nickname from user " +
                        "where username =:username and password=:password",
                params);

        if (!userList.isEmpty()
                && userList.get(0) != null) {
            return userList.get(0);
        }
        return null;
    }
}
