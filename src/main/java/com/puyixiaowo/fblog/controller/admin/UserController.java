package com.puyixiaowo.fblog.controller.admin;

import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.bean.admin.UserRoleBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.RolePermissionService;
import com.puyixiaowo.fblog.service.UserRoleService;
import com.puyixiaowo.fblog.service.UserService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import win.hupubao.common.utils.DesUtils;
import win.hupubao.common.utils.Md5Utils;
import win.hupubao.common.utils.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author Moses
 * @date 2017-08-10
 */
public class UserController extends BaseController {


    @RequiresPermissions(value = {"user:view"})
    public static String users(Request request,
                               Response response) {

        PageBean<UserBean> pageBean = getPageBean(request);
        try {
            UserBean params = getParamsEntity(request, UserBean.class, false);
            pageBean = UserService.selectUserPageBean(params, pageBean);
            for (UserBean userBean :
                    pageBean.getList()) {
                userBean.setPassword(Md5Utils.md5(userBean.getPassword() + Constants.PASSWORD_MD5_SALT));
            }
            pageBean.success();
        } catch (Exception e) {
            pageBean.error(e);
        }
        return pageBean.serialize();
    }

    /**
     * 添加或修改用户
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions(value = {"user:edit"})
    public static String edit(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();
        try {
            List<UserBean> userBeanList = getParamsEntityJson(request, UserBean.class, true);

            for (UserBean userBean :
                    userBeanList) {
                if (userBean.getId() == null) {
                    //是否已存在用户
                    int count = userBean.count("select count(*) from user where `loginname` = :loginname");
                    if (count > 0) {
                        responseBean.errorMessage("用户名已存在");
                        return responseBean.serialize();
                    }
                    userBean.setCreateTime(System.currentTimeMillis());
                }

                if (StringUtils.isBlank(userBean.getNickname())) {
                    userBean.setNickname("大帅比");
                }

                userBean.setPassword(Md5Utils.md5(userBean.getPassword() + Constants.PASSWORD_MD5_SALT));

                userBean.insertOrUpdate(false);
                //用户角色
                UserRoleBean userRoleBean = new UserRoleBean();
                userRoleBean.setRoleId(userBean.getRoleId());
                userRoleBean.setUserId(userBean.getId());
                userRoleBean.deleteOrUpdate("delete from user_role where user_id = :userId", new HashMap<>());
                userRoleBean.insertOrUpdate(false);
                responseBean.success();
            }
        } catch (Exception e) {
            responseBean.errorMessage(e.getMessage());
        }

        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"user:delete"})
    public static String delete(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();

        try {
            String ids = request.queryParams("id");
            new UserBean().deleteByIds(ids.split(","));
            //删除user_role
            UserRoleService.deleteByUserIds(ids);
            //删除role_permission
            RolePermissionService.deleteByUserIds(ids);
            responseBean.success();
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }

    /**
     * 添加或修改用户
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions(value = {"user:edit"})
    public static Object currentEdit(Request request, Response response) {
        Boolean data = Boolean.valueOf(request.params(":data"));
        Boolean isUpdatePass = Boolean.valueOf(request.queryParamOrDefault("pass", "false"));
        UserBean currentUserBean = request.session().attribute(Constants.SESSION_USER_KEY);

        if (!data) {
            String page = "admin/user/user_edit_current.html";
            if (isUpdatePass) {
                page = "admin/user/user_edit_current_pass.html";
            }
            HashMap<String, Object> model = new HashMap<>();
            model.put("model", currentUserBean);
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(model,
                            page));
        }


        ResponseBean responseBean = new ResponseBean();

        try {
            UserBean userBean = getParamsEntity(request, UserBean.class, false);

            if (userBean.getId().equals(currentUserBean.getId())) {
                if (StringUtils.isNotBlank(userBean.getPassword())) {
                    userBean.setPassword(Md5Utils.md5(userBean.getPassword() + Constants.PASSWORD_MD5_SALT));
                }
                userBean.insertOrUpdate(false);
            }

        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();

    }

}
