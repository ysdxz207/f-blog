package com.puyixiaowo.fblog.controller.admin;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.bean.admin.MenuBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.exception.MenuException;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.MenuService;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.RedisUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.List;

/**
 * @author feihong
 * @date 2017-08-13
 */
public class MenuController extends BaseController {


    public static Object navMenus(Request request, Response response) {

        ResponseBean responseBean = new ResponseBean();
        String type = request.params(":type");
        if (StringUtils.isBlank(type)) {
            throw new MenuException("菜单类型不可为空");
        }

        try {
            List<MenuBean> menuBeanList = MenuService.selectNavMenuList(type);
            responseBean.success(menuBeanList);
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean;
    }


    @RequiresPermissions(value = {"menu:view"})
    public static String menus(Request request, Response response) {
        Boolean data = Boolean.valueOf(request.params(":data"));

        if (!data) {
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(null,
                            "admin/menu/menu_list.html"));
        }
        PageBean pageBean = getPageBean(request);
        try {
            MenuBean menuBean = getParamsEntity(request, MenuBean.class, false);

            pageBean = MenuService.selectMenuPageBean(menuBean, pageBean);
        } catch (Exception e) {
            pageBean.error(e);
        }
        return pageBean.serialize();
    }

    @RequiresPermissions(value = {"menu:edit"})
    public static String edit(Request request, Response response) {
        System.out.println("edit");
        ResponseBean responseBean = new ResponseBean();
        List<MenuBean> menuBeanList = getParamsEntityJson(request, MenuBean.class, true);
        try {

            for (MenuBean menuBean :
                    menuBeanList) {
                DBUtils.insertOrUpdate(menuBean, false);
            }

            //删除缓存，下次刷新
            RedisUtils.delete(EnumsRedisKey.REDIS_KEY_MENU_LIST.key + "*");
            responseBean.setMessage("操作成功，请手动刷新页面。");

        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"menu:delete"})
    public static String delete(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();

        try {
            DBUtils.deleteByIds(MenuBean.class,
                    request.queryParams("id"));
            //删除缓存，下次刷新
            RedisUtils.delete(EnumsRedisKey.REDIS_KEY_MENU_LIST.key + "*");
            responseBean.setMessage("操作成功，请手动刷新页面。");
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"menu:view"})
    public static String typeList(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();

        try {
            responseBean.setData(MenuService.selectMenuTypeList());
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"menu:view"})
    public static String array(Request request, Response response) {
        String parent = request.params(":parent");
        List<MenuBean> list = DBUtils.selectList(MenuBean.class,
                "select * from menu where pid "
                        + ("yes".equals(parent) ? " = 0"
                        : ("no".equals(parent) ? " > 0" : ">= 0")),
                null);

        MenuBean bean = new MenuBean();
        bean.setId("");
        bean.setMenuName("无");
        list.add(0, bean);

        return JSON.toJSONString(list);
    }


}
