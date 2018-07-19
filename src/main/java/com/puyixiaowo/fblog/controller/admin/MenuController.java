package com.puyixiaowo.fblog.controller.admin;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.annotation.admin.RequiresPermissions;
import com.puyixiaowo.fblog.bean.admin.MenuBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.service.MenuService;
import com.puyixiaowo.fblog.utils.RedisUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import spark.Request;
import spark.Response;
import win.hupubao.common.utils.LoggerUtils;

import java.util.List;

/**
 * @author W.feihong
 * @date 2017-08-13
 */
public class MenuController extends BaseController {


    public static Object navMenus(Request request, Response response) {

        ResponseBean responseBean = new ResponseBean();
        String type = request.queryParams("type");
        if (StringUtils.isBlank(type)) {
            return responseBean.errorMessage("菜单类型不可为空");
        }

        try {
            List<MenuBean> menuBeanList = MenuService.selectNavMenuList(type);
            responseBean.success(menuBeanList);
        } catch (Exception e) {
            responseBean.error(e);
        }
        return responseBean;
    }


    @RequiresPermissions(value = {"menu:edit"})
    public static String edit(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();
        List<MenuBean> menuBeanList = getParamsEntityJson(request, MenuBean.class, true);
        try {

            for (MenuBean menuBean :
                    menuBeanList) {
                menuBean.insertOrUpdate(false);
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
            new MenuBean().deleteByIds(request.queryParams("id").split(","));
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
            LoggerUtils.error("菜单分类异常：", e);
            responseBean.error(e);
        }

        return responseBean.serialize();
    }

    @RequiresPermissions(value = {"menu:view"})
    public static String array(Request request, Response response) {
        String parent = request.params(":parent");
        MenuBean bean = new MenuBean();
        List<MenuBean> list = bean.selectList("select * from menu where pid "
                + ("yes".equals(parent) ? " = 0"
                : ("no".equals(parent) ? " > 0" : ">= 0")));

        bean.setId("");
        bean.setMenuName("无");
        list.add(0, bean);

        return JSON.toJSONString(list);
    }


}
