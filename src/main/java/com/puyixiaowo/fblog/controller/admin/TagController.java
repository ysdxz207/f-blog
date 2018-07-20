package com.puyixiaowo.fblog.controller.admin;

import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.bean.admin.TagBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.service.TagService;
import spark.Request;
import spark.Response;

import java.util.List;

/**
 * 
 * @author Moses
 * @date 2017-09-05
 * 
 */
public class TagController extends BaseController {

    @RequiresPermissions(value = {"tag:view"})
    public static String tags(Request request, Response response) {
        PageBean pageBean = getPageBean(request);
        try {
            TagBean tagBean = getParamsEntity(request, TagBean.class, false);
            pageBean = TagService.selectTagPageBean(tagBean, pageBean);

            pageBean.success();
        } catch (Exception e) {
            pageBean.error(e);
        }
        return pageBean.serialize();
    }


    @RequiresPermissions(value = {"tag:edit"})
    public static String edit(Request request, Response response){
        ResponseBean responseBean = new ResponseBean();
        List<TagBean> tagBeanList = getParamsEntityJson(request, TagBean.class, true);
        try {

            for (TagBean tagBean :
                    tagBeanList) {
                tagBean.insertOrUpdate(false);
            }
            responseBean.success();
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"tag:delete"})
    public static String delete(Request request, Response response){
        ResponseBean responseBean = new ResponseBean();

        try {
            new TagBean().deleteByIds(request.queryParams("id").split(","));
            responseBean.success();
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }
}
