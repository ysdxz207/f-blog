package com.puyixiaowo.fblog.controller.admin;

import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.bean.admin.CategoryBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.service.CategoryService;
import spark.Request;
import spark.Response;

import java.util.List;

/**
 * 
 * @author Moses
 * @date 2017-09-05
 * 
 */
public class CategoryController extends BaseController {

    @RequiresPermissions(value = {"category:view"})
    public static String categorys(Request request, Response response) {
        PageBean pageBean = getPageBean(request);

        try {
            CategoryService.selectCategoryPageBean(
                    getParamsEntity(request, CategoryBean.class, false), pageBean).serialize();
            pageBean.success();
        } catch (Exception e) {
            e.printStackTrace();
            pageBean.error(e);
        }
        return pageBean.serialize();
    }


    @RequiresPermissions(value = {"category:edit"})
    public static String edit(Request request, Response response){
        ResponseBean responseBean = new ResponseBean();
        List<CategoryBean> categoryBeanList = getParamsEntityJson(request, CategoryBean.class, true);
        try {

            for (CategoryBean categoryBean :
                    categoryBeanList) {
                categoryBean.insertOrUpdate(false);
            }
            responseBean.success();
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"category:delete"})
    public static String delete(Request request, Response response){
        ResponseBean responseBean = new ResponseBean();

        try {
            new CategoryBean().deleteByIds(request.queryParams("id").split(","));
            responseBean.success();
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }

}
