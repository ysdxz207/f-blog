package com.puyixiaowo.fblog.Controller.admin;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.Controller.BaseController;
import com.puyixiaowo.fblog.bean.admin.RoleBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.RoleService;
import com.puyixiaowo.fblog.utils.DBUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.List;

/**
 * 
 * @author Moses
 * @date 2017-08-26 21:23:47
 * 
 */
public class RoleController extends BaseController {

    public static Object roles(Request request, Response response) {
        Boolean data = Boolean.valueOf(request.params(":data"));

        if (!data) {
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(null,
                            "rbac/role/role_list.html"));
        }
        PageBean pageBean = getPageBean(request);
        RoleBean roleBean = getParamsEntity(request, RoleBean.class, false);
        List<RoleBean> list = RoleService.selectRoleList(roleBean,
                pageBean);
        pageBean.setList(list);

        int count = RoleService.selectCount(roleBean);
        pageBean.setTotalCount(count);
        return pageBean.serialize();
    }


    public static String edit(Request request, Response response){
        ResponseBean responseBean = new ResponseBean();
        List<RoleBean> roleBeanList = getParamsEntityJson(request, RoleBean.class, true);
        try {

            for (RoleBean roleBean :
                    roleBeanList) {
                DBUtils.insertOrUpdate(roleBean);
            }
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }

    public static String delete(Request request, Response response){
        ResponseBean responseBean = new ResponseBean();

        try {
            DBUtils.deleteByIds(RoleBean.class,
                    request.queryParams("id"));
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }

    public static String allArray() {
        List<RoleBean> list = DBUtils.selectList(RoleBean.class,
                "select * from role ",
                null);

        return JSON.toJSONString(list);
    }

}
