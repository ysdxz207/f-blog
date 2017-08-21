package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.utils.DBUtils;

import java.util.List;

/**
 * @author Moses
 * @date 2017-08-19 17:13
 */
public class UserService {

    public static List<UserBean> selectUserList(UserBean userBean){
        StringBuilder sbSql = new StringBuilder("select * from user where 1 = 1 ");

        if (userBean.getLoginname() != null) {
            sbSql.append("and loginname = :loginname ");
        }
        if (userBean.getNickname() != null) {
            sbSql.append("and nickname like '%");
            sbSql.append(userBean.getNickname());
            sbSql.append("%' ");
            userBean.setNickname(null);
        }
        if (userBean.getCreateTime() != null) {
            sbSql.append("and creat_time = :createTime ");
        }


        sbSql.append(" order by sort asc");
        return DBUtils.selectList(UserBean.class, sbSql.toString(), userBean);
    }
}
