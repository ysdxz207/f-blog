package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.admin.CategoryBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.utils.DBUtils;

import java.util.List;

/**
 * 
 * @author Moses
 * @date 2017-09-05 22:29:28
 * 
 */
public class CategoryService {


    public static List<CategoryBean> selectCategoryList(CategoryBean categoryBean,
                                                      PageBean pageBean) {

        StringBuilder sbSql = new StringBuilder("select * from category where 1 = 1 ");

        buildSqlParams(sbSql, categoryBean);
        sbSql.append(" order by id asc");
        sbSql.append(" limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return DBUtils.selectList(sbSql.toString(), categoryBean);
    }

    public static int selectCount(CategoryBean categoryBean) {
        StringBuilder sbSql = new StringBuilder("select count(*) from category where 1 = 1 ");

        buildSqlParams(sbSql, categoryBean);
        return DBUtils.count(sbSql.toString(), categoryBean);
    }

    public static void buildSqlParams(StringBuilder sbSql,
                                               CategoryBean categoryBean) {
        if (categoryBean.getName() != null) {
            sbSql.append("and category like :category ");
            categoryBean.setName("%" + categoryBean.getName() + "%");
        }
    }

    /**
     *
     * @param categoryBean
     * @param pageBean
     * @return
     */
    public static String selectCategoryListPage(CategoryBean categoryBean,
                                                PageBean pageBean) {

        try {
            List<CategoryBean> list = CategoryService.selectCategoryList(categoryBean,
                    pageBean);
            pageBean.setList(list);

            int count = CategoryService.selectCount(categoryBean);
            pageBean.setTotalCount(count);
        } catch (Exception e) {
            pageBean.error(e);
        }
        return pageBean.serialize();
    }
}
