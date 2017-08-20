package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.domain.Role;
import com.puyixiaowo.fblog.utils.DBUtils;

import java.util.List;
import java.util.Map;

/**
 * @author feihong
 * @date 2017-08-06 21:57
 */
public class LoginService {

    public static UserBean login(Map<String ,Object> params){
        List<UserBean> userList = DBUtils.selectList(UserBean.class,
                "select * from user " +
                        "where loginname =:loginname " +
                        "and password=:password " +
                        "and status = 1",
                params);

        UserBean userBean = null;
        if (!userList.isEmpty()
                && userList.get(0) != null) {
            userBean= userList.get(0);

            params.clear();
            params.put("userId", userBean.getId());
            Role role = DBUtils.selectOne(Role.class, "select * from role r " +
                            "left join user_role ur on r.id=ur.role_id where " +
                            "ur.user_id = :userId",
                    params);

            userBean.setRoleId(role.getId());
            userBean.setRoleName(role.getRoleName());

        }

        return userBean;
    }
}
