package com.puyixiaowo.fblog.service.book;

import com.puyixiaowo.fblog.bean.admin.book.BookshelfBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.utils.DBUtils;

/**
 *
 * @author Moses
 * @date 2017-12-19
 */
public class BookshelfService {

    public static BookshelfBean getBookshelfById(Long id) {
        BookshelfBean bookshelfBean = new BookshelfBean();
        bookshelfBean.setId(id);
        return DBUtils.selectOne("select * from bookshelf where id=:id", bookshelfBean);
    }

    public static String getSelectSql(BookshelfBean bookshelfBean,
                                                      PageBean pageBean) {

        StringBuilder sbSql = new StringBuilder("select * from bookshelf t where 1 = 1 ");

        buildSqlParams(sbSql, bookshelfBean);
        sbSql.append("group by t.id ");
        sbSql.append("order by t.id asc ");
        sbSql.append("limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return sbSql.toString();
    }
    public static PageBean selectBookshelfPageBean(BookshelfBean bookshelfBean, PageBean pageBean){
        return DBUtils.selectPageBean(getSelectSql(bookshelfBean, pageBean), bookshelfBean, pageBean);
    }
    public static void buildSqlParams(StringBuilder sbSql,
                                               BookshelfBean bookshelfBean) {
    }

    public static BookshelfBean getUserShelf(Long userId) {
        BookshelfBean bookshelfBean = new BookshelfBean();
        bookshelfBean.setUserId(userId);
        return DBUtils.selectOne("select * from bookshelf where user_id = :userId", bookshelfBean);
    }
}
