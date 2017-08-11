package com.puyixiaowo.fblog.Controller.admin;

import com.puyixiaowo.fblog.Controller.BaseController;
import com.puyixiaowo.fblog.bean.ResponseBean;
import com.puyixiaowo.fblog.domain.User;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.IdUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import spark.Request;
import spark.Response;

/**
 * @author Moses
 * @date 2017-08-10 9:57
 */
public class AdminController extends BaseController{


    public static Object editUser(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();
        User user = null;
        try {
            user = getParamEntity(request, User.class, true);
            if (StringUtils.isBlank(user.getNickname())) {
                user.setNickname("大帅比");
            }
            if (user.getId() == null) {
                user.setId(IdUtils.generateId());
            }
            //是否已存在用户
            int count = DBUtils.count("select count(*) from user where `username` = :username", user);
            if (count > 0) {
                responseBean.error("用户名已存在");
                return responseBean;
            }
            DBUtils.insertOrUpdate(user);
            responseBean.success();
            user.setPassword(null);
            responseBean.setData(user);
        } catch (Exception e) {
            responseBean.error(e.getMessage());
        }

        return responseBean.toString();
    }

    
}
