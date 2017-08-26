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
        StringBuilder sbSql = new StringBuilder("select u.*,ur.role_id " +
                "from user u left join user_role ur on u.id=ur.user_id where loginname<>'feihong' ");

        buildSqlParams(sbSql, userBean);
        sbSql.append(" order by id asc");
        return DBUtils.selectList(UserBean.class, sbSql.toString(), userBean);
    }

    public static int selectCount(UserBean userBean) {
        StringBuilder sbSql = new StringBuilder("select count(*) " +
                "from user u left join user_role ur on u.id=ur.user_id where loginname<>'feihong' ");

        buildSqlParams(sbSql, userBean);
        return DBUtils.count(sbSql.toString(), userBean);
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
}
