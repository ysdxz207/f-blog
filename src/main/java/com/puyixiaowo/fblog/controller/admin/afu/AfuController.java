package com.puyixiaowo.fblog.controller.admin.afu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.bean.admin.afu.AfuBean;
import com.puyixiaowo.fblog.bean.admin.afu.AfuTypeBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.service.AfuService;
import com.puyixiaowo.fblog.service.AfuTypeService;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Moses
 * @date 2017-08-26
 */
public class AfuController extends BaseController {

    @RequiresPermissions(value = {"afu:view"})
    public static String afus(Request request, Response response) {
        PageBean<AfuBean> pageBean = getPageBean(request);
        AfuBean afuBean = null;
        try {
            afuBean = getParamsEntity(request, AfuBean.class, false);

            pageBean.success();
        } catch (Exception e) {
            pageBean.error(e);
        }
        pageBean = AfuService.selectAfuPageBean(afuBean, pageBean);
        return pageBean.serialize();
    }

    @RequiresPermissions(value = {"afu:view"})
    public static String detail(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();

        Map<String, Object> map = new HashMap<>();
        try {

            AfuBean afuBean = getParamsEntity(request, AfuBean.class, false);
            if (afuBean.getId() != null) {
                afuBean = afuBean.selectOne("select a.*,at.name as typeName from afu a " +
                        "left join afu_type at on a.type = at.id where a.id=:id");
            } else {
                afuBean = new AfuBean();
            }

            //阿福类别
            PageBean afuTypePageBean = AfuTypeService.selectAfuTypePageBean(new AfuTypeBean(), new PageBean());


            map.put("afuTypeList", afuTypePageBean.getList());
            map.put("afuBean", afuBean);

            responseBean.success(map);
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"afu:edit"})
    public static String edit(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();

        try {
            AfuBean afuBean = getParamsEntity(request, AfuBean.class, false);
            if (afuBean.getId() == null) {
                afuBean.setCreateTime(System.currentTimeMillis());
            }
            afuBean.insertOrUpdate(false);
            responseBean.success();
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"afu:delete"})
    public static String delete(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();

        try {
            new AfuBean().deleteByIds(request.queryParams("id").split(","));
            responseBean.success();
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
            afuBean = afuBean.selectOne("select * from afu where id = :id");

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

            responseBean.success(sb.toString());
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }

}
