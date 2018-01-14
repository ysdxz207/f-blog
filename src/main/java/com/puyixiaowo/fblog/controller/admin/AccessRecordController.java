package com.puyixiaowo.fblog.controller.admin;

import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.bean.AccessRecordBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.AccessRecordService;
import com.puyixiaowo.fblog.utils.DBUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Moses
 * @date 2018-01-13
 */
public class AccessRecordController extends BaseController {

    @RequiresPermissions(value = {"article:view"})
    public static String accessRecords(Request request, Response response) {
        Boolean data = Boolean.valueOf(request.params(":data"));

        if (!data) {
            Map<String, Object> model = new HashMap<>();
            model.put("articleId", request.queryParams("articleId"));
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(model,
                            "admin/accessrecord/access_record_list.html"));
        }
        PageBean pageBean = getPageBean(request);

        try {
            return AccessRecordService.selectAccessRecordPageBean(
                    getParamsEntity(request, AccessRecordBean.class, false), pageBean).serialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequiresPermissions(value = {"article:delete"})
    public static String delete(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();

        try {
            DBUtils.deleteByIds(AccessRecordBean.class,
                    request.queryParams("id"));
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }

}
