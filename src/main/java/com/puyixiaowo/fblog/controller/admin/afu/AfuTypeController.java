package com.puyixiaowo.fblog.controller.admin.afu;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.bean.admin.afu.AfuTypeBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.AfuTypeService;
import com.puyixiaowo.fblog.utils.DBUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.List;

/**
 * 
 * @author Moses
 * @date 2017-09-05 22:31:38
 * 
 */
public class AfuTypeController extends BaseController {

    @RequiresPermissions(value = {"afuType:view"})
    public static String afuTypes(Request request, Response response) {
        Boolean data = Boolean.valueOf(request.params(":data"));

        if (!data) {
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(null,
                            "admin/afu/type/afu_type_list.html"));
        }
        PageBean pageBean = getPageBean(request);

        try {
            return AfuTypeService.selectAfuTypePageBean(
                    getParamsEntity(request, AfuTypeBean.class, false), pageBean).serialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    @RequiresPermissions(value = {"afuType:edit"})
    public static String edit(Request request, Response response){
        ResponseBean responseBean = new ResponseBean();
        List<AfuTypeBean> afuTypeBeanList = getParamsEntityJson(request, AfuTypeBean.class, true);
        try {

            for (AfuTypeBean afuTypeBean :
                    afuTypeBeanList) {
                DBUtils.insertOrUpdate(afuTypeBean);
            }
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"afuType:delete"})
    public static String delete(Request request, Response response){
        ResponseBean responseBean = new ResponseBean();

        try {
            DBUtils.deleteByIds(AfuTypeBean.class,
                    request.queryParams("id"));
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }


    @RequiresPermissions(value = {"afuType:view"})
    public static String allArray(Request request) {
        List<AfuTypeBean> list = DBUtils.selectList(AfuTypeBean.class,
                "select * from afu_type ",
                null);

        return JSON.toJSONString(list);
    }

}
