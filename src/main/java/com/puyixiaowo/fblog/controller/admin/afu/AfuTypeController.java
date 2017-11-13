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
import com.puyixiaowo.fblog.utils.StringUtils;
import com.puyixiaowo.fblog.utils.sign.RSAKeyUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Moses
 * @date 2017-09-05 22:31:38
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
            pageBean = AfuTypeService.selectAfuTypePageBean(
                    getParamsEntity(request, AfuTypeBean.class, false), pageBean);
        } catch (Exception e) {
            pageBean.errorMessage(e.getMessage());
        }
        return pageBean.serialize();
    }


    @RequiresPermissions(value = {"afuType:edit"})
    public static String edit(Request request, Response response) {

        Boolean data = Boolean.valueOf(request.params(":data"));

        if (!data) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", request.queryParams("id"));
            AfuTypeBean afuTypeBean = DBUtils.selectOne(AfuTypeBean.class, "select * from afu_type where id=:id", map);
            map.clear();
            map.put("model", afuTypeBean);
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(map,
                            "admin/afu/type/afu_type_edit.html"));
        }

        ResponseBean responseBean = new ResponseBean();
        try {
            AfuTypeBean afuTypeBean = getParamsEntity(request, AfuTypeBean.class, false);
            if (afuTypeBean.getId() == null) {
                RSAKeyUtils.RSAKey key = RSAKeyUtils.generateRSAKey();
                afuTypeBean.setPrivateKey(StringUtils.replaceBlank(key.getPrivateKey()));
                afuTypeBean.setPublicKey(StringUtils.replaceBlank(key.getPublicKey()));
            }
            DBUtils.insertOrUpdate(afuTypeBean);
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"afuType:delete"})
    public static String delete(Request request, Response response) {
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
