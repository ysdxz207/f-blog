package com.puyixiaowo.fblog.controller.admin.afu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.bean.admin.afu.AfuBean;
import com.puyixiaowo.fblog.bean.admin.afu.AfuTypeBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.AfuService;
import com.puyixiaowo.fblog.service.AfuTypeService;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
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

    @RequiresPermissions(value = {"afu:edit"})
    public static String edit(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();

        Boolean data = Boolean.valueOf(request.params(":data"));
        Map<String, Object> map = new HashMap<>();
        try {

            if (!data) {
                AfuBean afuBean = getParamsEntity(request, AfuBean.class, false);
                if (afuBean.getId() != null) {
                    afuBean = DBUtils.selectOne("select a.*,at.name as typeName from afu a " +
                            "left join afu_type at on a.type = at.id where a.id=:id", afuBean);
                } else {
                    afuBean = new AfuBean();
                }

                //阿福类别
                PageBean afuTypePageBean = AfuTypeService.selectAfuTypePageBean(new AfuTypeBean(), new PageBean());


                map.put("afuTypeList", afuTypePageBean.getList());
                map.put("model", afuBean);

                return new FreeMarkerTemplateEngine()
                        .render(new ModelAndView(map,
                                "admin/afu/afu_edit.html"));
            }

            AfuBean afuBean = getParamsEntity(request, AfuBean.class, false);
            if (afuBean.getId() == null) {
                afuBean.setCreateTime(System.currentTimeMillis());
            }
            DBUtils.insertOrUpdate(afuBean, false);
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
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

    @RequiresPermissions(value = {"afu:view"})
    public static String text(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();

        try {
            AfuBean afuBean = getParamsEntity(request, AfuBean.class, false);
            StringBuilder sb = new StringBuilder();
            afuBean = DBUtils.selectOne("select * from afu where id = :id", afuBean);

            int num = 1;
            if (afuBean != null) {

                for (Object o :
                        JSON.parseArray(afuBean.getContent())) {

                    JSONArray array = (JSONArray) o;
                    String content = array.getString(1);
                    Integer wanchengdu = array.getJSONObject(3).getInteger("percent");
                    String wanchengduDescription = array.getJSONObject(3).getString("description");
                    if (wanchengdu != 0) {
                        sb.append(num);
                        sb.append("、");
                        sb.append(content);
                        sb.append("[");
                        sb.append(wanchengduDescription);
                        sb.append("]");
                        sb.append("\n");
                        num ++;
                    }
                }
            }

            responseBean.setMessage(null);
            responseBean.setData(sb.toString());
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }

}
