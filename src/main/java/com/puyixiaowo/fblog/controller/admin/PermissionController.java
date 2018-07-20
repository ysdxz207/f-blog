package com.puyixiaowo.fblog.controller.admin;

import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.bean.admin.PermissionBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.PermissionService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.List;

/**
 * @author W.feihong
 * @date 2017-08-13
 */
public class PermissionController extends BaseController {

    @RequiresPermissions(value = {"permission:view"})
    public static String permissions(Request request, Response response) {
        PageBean<PermissionBean> pageBean = getPageBean(request);
        try {
            PermissionBean permissionBean = getParamsEntity(request, PermissionBean.class, false);
            PermissionService.selectPermissionPageBean(permissionBean, pageBean);
            pageBean.success();
        } catch (Exception e) {
            pageBean.error(e);
        }
        return pageBean.serialize();
    }


    @RequiresPermissions(value = {"permission:edit"})
    public static String edit(Request request, Response response){
        ResponseBean responseBean = new ResponseBean();
        List<PermissionBean> permissionBeanList = getParamsEntityJson(request, PermissionBean.class, true);
        try {

            for (PermissionBean permissionBean :
                    permissionBeanList) {
                permissionBean.insertOrUpdate(false);
            }
            responseBean.success();
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"permission:delete"})
    public static String delete(Request request, Response response){
        ResponseBean responseBean = new ResponseBean();

        try {
            new PermissionBean().deleteByIds(request.queryParams("id").split(","));
            responseBean.success();
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }

}
