package com.puyixiaowo.fblog.Controller.admin;

import com.puyixiaowo.core.exceptions.ValidateException;
import com.puyixiaowo.fblog.Controller.BaseController;
import com.puyixiaowo.fblog.bean.ResponseBean;
import com.puyixiaowo.fblog.domain.User;
import spark.Request;
import spark.Response;

/**
 * @author Moses
 * @date 2017-08-10 9:57
 */
public class AdminController extends BaseController{


    public static Object editUser(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();


        User user = getParamEntity(request, User.class);

        try {
            user.validate();
        } catch (ValidateException e) {
            responseBean.error(e.getMessage());
        }

//        return DBUtils.insertOrUpdate(user);
        return responseBean.toString();
    }
}
