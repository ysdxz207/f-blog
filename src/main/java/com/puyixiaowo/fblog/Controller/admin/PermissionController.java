package com.puyixiaowo.fblog.Controller.admin;

import com.puyixiaowo.fblog.Controller.BaseController;
import com.puyixiaowo.fblog.bean.admin.PermissionBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.PermissionService;
import com.puyixiaowo.fblog.utils.DBUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.List;

/**
 * @author feihong
 * @date 2017-08-13 18:58
 */
public class PermissionController extends BaseController {

    public static Object permissions(Request request, Response response) {
        Boolean data = Boolean.valueOf(request.params(":data"));

        if (!data) {
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(null,
                            "rbac/permission/permission_list.html"));
        }
        PageBean pageBean = getPageBean(request);
        PermissionBean permissionBean = getParamsEntity(request, PermissionBean.class, false);
        List<PermissionBean> list = PermissionService.selectPermissionList(permissionBean,
                pageBean);
        pageBean.setList(list);

        int count = PermissionService.selectCount(permissionBean);
        pageBean.setTotalCount(count);
        return pageBean.serialize();
    }


    public static Object edit(Request request, Response response){
        ResponseBean responseBean = new ResponseBean();
        List<PermissionBean> permissionBeanList = getParamsEntityJson(request, PermissionBean.class, true);
        try {

            for (PermissionBean permissionBean :
                    permissionBeanList) {
                DBUtils.insertOrUpdate(permissionBean);
            }
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }

}
