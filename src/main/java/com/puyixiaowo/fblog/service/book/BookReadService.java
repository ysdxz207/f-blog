package com.puyixiaowo.fblog.service.book;

import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.bean.admin.book.BookReadBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.utils.DBUtils;
import spark.Request;

public class BookReadService {

    public static String getSelectSql(BookReadBean bookReadBean,
                                      PageBean pageBean) {

        StringBuilder sbSql = new StringBuilder("select t.* from book_read t where 1 = 1 ");

        buildSqlParams(sbSql, bookReadBean);
        sbSql.append("order by t.id desc ");
        sbSql.append("limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return sbSql.toString();
    }
    public static PageBean selectBookPageBean(BookReadBean bookReadBean, PageBean pageBean){
        return DBUtils.selectPageBean(getSelectSql(bookReadBean, pageBean), bookReadBean, pageBean);
    }
    public static void buildSqlParams(StringBuilder sbSql,
                                      BookReadBean bookReadBean) {
        if (bookReadBean.getUserId() != null) {
            sbSql.append("and t.userId = :userId ");
            bookReadBean.setUserId(bookReadBean.getUserId());
        }
    }

    public static BookReadBean getUserReadConfig(Long userId,
                                                 Long bookId) {
        BookReadBean bookReadBean = new BookReadBean();
        bookReadBean.setUserId(userId);
        bookReadBean.setBookId(bookId);
        bookReadBean = DBUtils.selectOne("select * from book_read where " +
                "user_id = :userId and book_id = :bookId", bookReadBean);

        return bookReadBean;
    }
}
