package com.puyixiaowo.fblog.service.book;

import com.puyixiaowo.fblog.bean.admin.book.BookBean;
import com.puyixiaowo.fblog.bean.admin.book.BookshelfBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.utils.DBUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Moses
 * @date 2017-12-19
 * 
 */
public class BookService {

    public static String getSelectSql(BookBean bookBean,
                                                      PageBean pageBean) {

        StringBuilder sbSql = new StringBuilder("select t.* from book t where 1 = 1 ");

        buildSqlParams(sbSql, bookBean);
        sbSql.append("group by t.id ");
        sbSql.append("order by t.id desc ");
        sbSql.append("limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return sbSql.toString();
    }
    public static PageBean selectBookPageBean(BookBean bookBean, PageBean pageBean){
        return DBUtils.selectPageBean(getSelectSql(bookBean, pageBean), bookBean, pageBean);
    }
    public static void buildSqlParams(StringBuilder sbSql,
                                               BookBean bookBean) {
        if (bookBean.getName() != null) {
            sbSql.append("and t.name like :name ");
            bookBean.setName("%" + bookBean.getName() + "%");
        }

        if (bookBean.getAId() != null) {
            sbSql.append("and t.a_id = :aId ");
        }
    }

    public static List<BookBean> getUserBookList(Long userId) {
        BookshelfBean bookshelfBean = BookshelfService.getUserShelf(userId);
        Map<String, Object> params = new HashMap<>();
        params.put("bookIds", bookshelfBean.getBookIds());

        List<BookBean> bookBeanList = DBUtils.selectList(BookBean.class,
                "select * from book where " +
                "id in (:bookIds)", params);
        return bookBeanList;
    }
}
