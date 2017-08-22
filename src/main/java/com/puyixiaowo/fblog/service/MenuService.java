package com.puyixiaowo.fblog.service;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.bean.admin.MenuBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.RedisUtils;
import com.puyixiaowo.fblog.utils.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author Moses
 * @date 2017-08-19 09:35
 */
public class MenuService {

    private static List<MenuBean> selectMenuListRedis(long pid,  int type) {
        //redis
        String menuListStr = RedisUtils.get(EnumsRedisKey.REDIS_KEY_MENU_LIST_.key);

        if (StringUtils.isNotBlank(menuListStr)) {
            return JSON.parseArray(menuListStr, MenuBean.class);
        }

        String sql = "select * from menu " +
                "where pid = :pid " +
                "and type = :type and status = 1 " +
                "order by sort asc";

        List<MenuBean> menuBeanList = DBUtils.selectList(MenuBean.class, sql, new HashMap<String, Object>(){
            {
                put("pid", pid);
                put("type", type);
            }
        });

        return menuBeanList;
    }

    private static List<MenuBean> selectNestedMenuList(long pid, int type){
        //父级
        List<MenuBean> parentMenuList = selectMenuListRedis(pid, type);

        for (MenuBean menuBean : parentMenuList) {
            List<MenuBean> childMenuList = selectNestedMenuList(menuBean.getId(), type);
            if (childMenuList.size() > 0) {
                menuBean.setMenuBeanList(childMenuList);
            }
        }

        return parentMenuList;
    }

    public static List<MenuBean> selectNavMenuList(int type) {
        return selectNestedMenuList(0L, type);
    }


    public static List<MenuBean> selectMenuList(MenuBean menuBean,
                                                PageBean pageBean) {
        StringBuilder sbSql = new StringBuilder("select * from menu where 1 = 1 ");

        buildSqlParams(sbSql, menuBean);
        sbSql.append(" order by sort asc");
        sbSql.append(" limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return DBUtils.selectList(MenuBean.class, sbSql.toString(), menuBean);
    }

    public static int selectCount(MenuBean menuBean) {
        StringBuilder sbSql = new StringBuilder("select count(*) from menu where 1 = 1 ");

        buildSqlParams(sbSql, menuBean);
        return DBUtils.count(sbSql.toString(), menuBean);
    }

    public static void buildSqlParams(StringBuilder sbSql,
                                               MenuBean menuBean) {
        if (menuBean.getPid() != null) {
            sbSql.append("and pid = :pid ");
        }
        if (menuBean.getType() != null) {
            sbSql.append("and type = :type ");
        }
        if (menuBean.getPid() != null) {
            sbSql.append("and menu_name like :menuName");
            menuBean.setMenuName("%" + menuBean.getMenuName() + "%");
        }
        if (menuBean.getStatus() != null) {
            sbSql.append("and status = :status ");
        }
    }
}
