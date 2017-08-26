package com.puyixiaowo.fblog.Controller.admin;

import com.puyixiaowo.fblog.Controller.BaseController;
import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.UserService;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.DesUtils;
import com.puyixiaowo.fblog.utils.RedisUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.List;

/**
 * @author Moses
 * @date 2017-08-10 9:57
 */
public class UserController extends BaseController{


    public static String users(Request request,
                                       Response response){

        Boolean data = Boolean.valueOf(request.params(":data"));

        if (!data) {
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(null,
                            "rbac/user/user_list.html"));
        }

        PageBean pageBean = getPageBean(request);
        try {
            UserBean params = getParamsEntity(request, UserBean.class, false);
            List<UserBean> list =
                    UserService.selectUserList(
                            getParamsEntity(request, UserBean.class, false));

            for (UserBean userBean :
                    list) {
                userBean.setPassword(DesUtils.decrypt(userBean.getPassword()));
            }
            pageBean.setList(list);
            int count = UserService.selectCount(params);
            pageBean.setTotalCount(count);
        } catch (Exception e) {
            pageBean.error(e);
        }
        return pageBean.serialize();
    }
    /**
     * 添加或修改用户
     * @param request
     * @param response
     * @return
     */
    public static String edit(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();
        try {
            List<UserBean> userBeanList = getParamsEntityJson(request, UserBean.class, true);

            for (UserBean userBean :
                    userBeanList) {
                if (userBean.getId() == null) {
                    //是否已存在用户
                    int count = DBUtils.count("select count(*) from user where `loginname` = :loginname", userBean);
                    if (count > 0) {
                        responseBean.errorMessage("用户名已存在");
                        return responseBean.serialize();
                    }
                }

                if (StringUtils.isBlank(userBean.getNickname())) {
                    userBean.setNickname("大帅比");
                }

                userBean.setPassword(DesUtils.encrypt(userBean.getPassword()));

                DBUtils.insertOrUpdate(userBean);
            }
        } catch (Exception e) {
            responseBean.errorMessage(e.getMessage());
        }

        return responseBean.serialize();
    }

    public static String delete(Request request, Response response){
        ResponseBean responseBean = new ResponseBean();

        try {
            DBUtils.deleteByIds(UserBean.class,
                    request.queryParams("id"));
            //删除缓存，下次刷新
            RedisUtils.delete(EnumsRedisKey.REDIS_KEY_MENU_LIST.key + "*");
            responseBean.setMessage("操作成功，请手动刷新页面。");
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }
}
