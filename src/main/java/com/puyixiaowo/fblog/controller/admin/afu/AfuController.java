package com.puyixiaowo.fblog.controller.admin.afu;

import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.bean.admin.afu.AfuBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.AfuService;
import com.puyixiaowo.fblog.utils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Moses
 * @date 2017-08-26 21:23:47
 */
public class AfuController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AfuController.class);

    @RequiresPermissions(value = {"afu:view"})
    public static String afus(Request request, Response response) {
        Boolean data = Boolean.valueOf(request.params(":data"));

        if (!data) {
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(null,
                            "admin/afu/afu_list.html"));
        }
        PageBean pageBean = getPageBean(request);
        AfuBean afuBean = null;
        try {
            afuBean = getParamsEntity(request, AfuBean.class, false);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        pageBean = AfuService.selectAfuPageBean(afuBean, pageBean);
        return pageBean.serialize();
    }

    @RequiresPermissions(value = {"afu:view"})
    public static String detail(Request request, Response response) {

        AfuBean afuBean = new AfuBean();
        try {
            afuBean.setId(Long.valueOf(request.params(":id")));
            afuBean = DBUtils.selectOne("select a.*,at.name as typeName from afu a " +
                    "left join afu_type at on a.type = at.id where a.id=:id", afuBean);
        }catch (Exception e) {
            logger.error("查看阿福详情异常：" + e.getMessage());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("model", afuBean);

        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(map,
                        "admin/afu/afu_detail.html"));

    }

    @RequiresPermissions(value = {"afu:delete"})
    public static String delete(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();

        try {
            DBUtils.deleteByIds(AfuBean.class,
                    request.queryParams("id"));
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }

}
