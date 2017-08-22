package com.puyixiaowo.fblog.Controller.admin;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.Controller.BaseController;
import com.puyixiaowo.fblog.bean.admin.MenuBean;
import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.UserService;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.Md5Utils;
import com.puyixiaowo.fblog.utils.StringUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * @author Moses
 * @date 2017-08-10 9:57
 */
public class UserController extends BaseController{


    public static Object users(Request request,
                                       Response response){
        Boolean data = Boolean.valueOf(request.params(":data"));

        if (!data) {
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(null,
                            "rbac/user/user_list.html"));
        }
        return JSON.toJSONString(
                UserService.selectUserList(
                        getParamsEntity(request, MenuBean.class, false)));
    }
    /**
     * 添加或修改用户
     * @param request
     * @param response
     * @return
     */
    public static Object editUser(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();
        UserBean user = null;
        try {
            user = getParamsEntity(request, UserBean.class, true);
            //是否已存在用户
            int count = DBUtils.count("select count(*) from user where `username` = :username", user);
            if (count > 0) {
                responseBean.errorMessage("用户名已存在");
                return responseBean;
            }

            if (StringUtils.isBlank(user.getNickname())) {
                user.setNickname("大帅比");
            }

            user.setPassword(Md5Utils.md5Password(user.getPassword()));

            DBUtils.insertOrUpdate(user);
            user.setPassword(null);
            responseBean.setData(user);
        } catch (Exception e) {
            responseBean.errorMessage(e.getMessage());
        }

        return responseBean.toString();
    }

    
}
