package com.puyixiaowo.fblog.controller.admin;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.bean.admin.RoleBean;
import com.puyixiaowo.fblog.bean.admin.other.MenuPermissionBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.MenuService;
import com.puyixiaowo.fblog.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Moses
 * @date 2017-08-26
 */
public class RoleController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @RequiresPermissions(value = {"role:view"})
    public static String roles(Request request, Response response) {
        PageBean<RoleBean> pageBean = getPageBean(request);
        RoleBean roleBean = null;
        try {
            roleBean = getParamsEntity(request, RoleBean.class, false);
            pageBean = RoleService.selectRolePageBean(roleBean, pageBean);
            pageBean.success();
        } catch (Exception e) {
            pageBean.error(e);
        }
        return pageBean.serialize();
    }


    @RequiresPermissions(value = {"role:edit"})
    public static String edit(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();
        List<RoleBean> roleBeanList = getParamsEntityJson(request, RoleBean.class, true);
        try {

            for (RoleBean roleBean :
                    roleBeanList) {
                roleBean.insertOrUpdate(false);
            }
            responseBean.success();
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"role:delete"})
    public static String delete(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();

        try {
            new RoleBean().deleteByIds(request.queryParams("id").split(","));
            responseBean.success();
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"role:edit"})
    public static Object rolePermissionList(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();

        String roleId = request.queryParams("roleId");



        try {
            Map<String, Object> model = new HashMap<>();
            model.put("model", RoleService.selectByPrimaryKey(roleId));
            //权限列表
            List<MenuPermissionBean> menuList = MenuService.selectValidMenuPermissions(roleId);
            responseBean.success(menuList);
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"role:edit"})
    public static Object setPermission(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();

        String roleId = request.queryParams("roleId");
        String permissionIds = request.queryParams("permissionIds");

        try {
            RoleService.setPermission(roleId, permissionIds);
            responseBean.success();
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }

}
