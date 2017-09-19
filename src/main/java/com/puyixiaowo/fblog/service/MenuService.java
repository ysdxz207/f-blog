package com.puyixiaowo.fblog.service;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.bean.admin.MenuBean;
import com.puyixiaowo.fblog.bean.admin.other.MenuPermissionBean;
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

    public static List<MenuBean> selectMenuListRedis(long pid,
                                                      int type) {

        //redis
        String menuListStr = RedisUtils.get(EnumsRedisKey.REDIS_KEY_MENU_LIST.key + pid + "_" + type);

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

        if (!menuBeanList.isEmpty()) {
            //更新redis
            RedisUtils.set(EnumsRedisKey.REDIS_KEY_MENU_LIST.key + pid + "_" + type, JSON.toJSONString(menuBeanList));
        }

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


    public static String getSelectSql(MenuBean menuBean,
                                                PageBean pageBean) {
        StringBuilder sbSql = new StringBuilder("select * from menu where 1 = 1 ");

        buildSqlParams(sbSql, menuBean);
        sbSql.append(" order by sort asc");
        sbSql.append(" limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return sbSql.toString();
    }
    public static PageBean selectMenuPageBean(MenuBean menuBean, PageBean pageBean) {
        return DBUtils.selectPageBean(getSelectSql(menuBean, pageBean), menuBean);
    }
    public static void buildSqlParams(StringBuilder sbSql,
                                               MenuBean menuBean) {
        if (menuBean.getPid() != null) {
            sbSql.append("and pid = :pid ");
        }
        if (menuBean.getType() != null) {
            sbSql.append("and type = :type ");
        }
        if (menuBean.getMenuName() != null) {
            sbSql.append("and menu_name like :menuName");
            menuBean.setMenuName("%" + menuBean.getMenuName() + "%");
        }
        if (menuBean.getRemark() != null) {
            sbSql.append("and remark like :remark");
            menuBean.setRemark("%" + menuBean.getRemark() + "%");
        }
        if (menuBean.getCode() != null) {
            sbSql.append("and code like :code");
            menuBean.setCode("%" + menuBean.getCode() + "%");
        }
        if (menuBean.getStatus() != null) {
            sbSql.append("and status = :status ");
        }
        if (menuBean.getExpand() != null) {
            sbSql.append("and expand = :expand ");
        }
    }

    public static List<MenuPermissionBean> selectValidMenuPermissions(Long roleId) {

        String sql = "SELECT *\n" +
                "FROM (\n" +
                "       SELECT\n" +
                "         'm_' || id  AS id,\n" +
                "         menu_name,\n" +
                "         NULL        AS permission,\n" +
                "         'm_' || pid AS pid,\n" +
                "         0           AS is_checked\n" +
                "       FROM menu\n" +
                "       WHERE `status` = 1\n" +
                "       UNION\n" +
                "       SELECT\n" +
                "         id,\n" +
                "         permission_name,\n" +
                "         permission,\n" +
                "         'm_' || menu_id AS pid,\n" +
                "         CASE WHEN (id IN (\n" +
                "\n" +
                "           SELECT permission_id\n" +
                "           FROM\n" +
                "             permission mp\n" +
                "             LEFT JOIN role_permission mrp\n" +
                "               ON mp.id = mrp.permission_id\n" +
                "           WHERE mrp.role_id = :roleId\n" +
                "         ))\n" +
                "           THEN 'true'\n" +
                "         ELSE 'false' END    AS is_checked\n" +
                "       FROM permission\n" +
                "\n" +
                "       ORDER BY id\n" +
                "         ASC\n" +
                "     ) t;\n" +
                "\n";

        return DBUtils.selectList(MenuPermissionBean.class, sql,
                new HashMap<String, Object>(){
                    {
                        put("roleId", roleId);
                    }
                });
    }


}
