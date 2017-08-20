package com.puyixiaowo.fblog.Controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.fblog.Controller.BaseController;
import com.puyixiaowo.fblog.bean.admin.MenuBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.exception.MenuException;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.MenuService;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.List;

/**
 * @author feihong
 * @date 2017-08-13 18:58
 */
public class MenuController extends BaseController {

    public static Object navMenus(Request request, Response response) {
        String typeStr = request.params(":type");
        if (StringUtils.isBlank(typeStr)) {
            throw new MenuException("菜单类型不可为空");
        }
        Integer type = Integer.parseInt(typeStr);

        List<MenuBean> menuBeanList = MenuService.selectNavMenuList(type);
        return buildMenus(menuBeanList);
    }

    /**
     * @param list
     * @return
     */
    private static JSONArray buildMenus(List<MenuBean> list) {
        JSONArray result = new JSONArray();

        for (MenuBean menuBean : list) {
            JSONObject menus = new JSONObject();
            if (StringUtils.isNotBlank(menuBean.getCode())) {
                menus.put("id", menuBean.getCode());
            }
            if (StringUtils.isNotBlank(menuBean.getMenuName())) {
                menus.put("name", menuBean.getMenuName());
            }
            menus.put("target", "navtab");
            if (StringUtils.isNotBlank(menuBean.getHref())) {
                menus.put("url", menuBean.getHref());
            }
            List<MenuBean> menuList = menuBean.getMenuBeanList();
            if (menuList != null && menuList.size() > 0) {
                JSONArray children = buildMenus(menuList);
                menus.put("children", children);
            }
            result.add(menus);
        }
        return result;
    }

    public static Object menus(Request request, Response response) {
        Boolean data = Boolean.valueOf(request.params(":data"));

        if (!data) {
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(null,
                            "rbac/menu/menu_list.html"));
        }
        PageBean pageBean = getPageBean(request);
        MenuBean menuBean = getParamsEntity(request, MenuBean.class, false);
        List<MenuBean> list = MenuService.selectMenuList(menuBean,
                pageBean);
        pageBean.setList(list);

        int count = MenuService.selectCount(menuBean);
        pageBean.setTotalCount(count);
        return pageBean.serialize();
    }

    public static Object allArray() {
        return JSON.toJSONString(DBUtils.selectList(MenuBean.class,
                "select * from menu where pid > 0",
                null));
    }


}
