package com.puyixiaowo.fblog.controller.admin;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.bean.admin.CategoryBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.CategoryService;
import com.puyixiaowo.fblog.utils.DBUtils;
import spark.ModelAndView;
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
        Boolean data = Boolean.valueOf(request.params(":data"));

        if (!data) {
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(null,
                            "admin/category/category_list.html"));
        }
        PageBean pageBean = getPageBean(request);

        try {
            return CategoryService.selectCategoryPageBean(
                    getParamsEntity(request, CategoryBean.class, false), pageBean).serialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    @RequiresPermissions(value = {"category:edit"})
    public static String edit(Request request, Response response){
        ResponseBean responseBean = new ResponseBean();
        List<CategoryBean> categoryBeanList = getParamsEntityJson(request, CategoryBean.class, true);
        try {

            for (CategoryBean categoryBean :
                    categoryBeanList) {
                DBUtils.insertOrUpdate(categoryBean);
            }
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"category:delete"})
    public static String delete(Request request, Response response){
        ResponseBean responseBean = new ResponseBean();

        try {
            DBUtils.deleteByIds(CategoryBean.class,
                    request.queryParams("id"));
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }


    @RequiresPermissions(value = {"category:view"})
    public static String allArray(Request request) {
        List<CategoryBean> list = DBUtils.selectList(CategoryBean.class,
                "select * from category ",
                null);

        CategoryBean categoryBean = new CategoryBean();
        categoryBean.setId(0L);
        categoryBean.setName("默认分类");
        list.add(0, categoryBean);
        return JSON.toJSONString(list);
    }

}
