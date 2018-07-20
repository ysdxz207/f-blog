package com.puyixiaowo.fblog.service;

import com.puyixiaowo.fblog.bean.admin.CategoryBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;

import java.util.List;

/**
 * 
 * @author Moses
 * @date 2017-09-05
 * 
 */
public class CategoryService {

    public static String getSelectSql(CategoryBean categoryBean,
                                      PageBean pageBean) {
        StringBuilder sbSql = new StringBuilder("select * from category where 1 = 1 ");

        buildSqlParams(sbSql, categoryBean);
        sbSql.append(" order by id asc");
        sbSql.append(" limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return sbSql.toString();
    }

    public static List<CategoryBean> selectCategoryList(CategoryBean categoryBean,
                                                      PageBean pageBean) {


        return categoryBean.selectList(getSelectSql(categoryBean, pageBean));
    }

    public static PageBean<CategoryBean> selectCategoryPageBean (CategoryBean categoryBean,
                                                   PageBean<CategoryBean> pageBean) {

        String sql = getSelectSql(categoryBean, pageBean);
        List<CategoryBean> list = categoryBean.selectList(sql);
        int count = categoryBean.count(sql);
        pageBean.setList(list);
        pageBean.setTotalCount(count);
        return pageBean;
    }

    public static void buildSqlParams(StringBuilder sbSql,
                                               CategoryBean categoryBean) {
        if (categoryBean.getName() != null) {
            sbSql.append("and category like :category ");
            categoryBean.setName("%" + categoryBean.getName() + "%");
        }
    }
}
