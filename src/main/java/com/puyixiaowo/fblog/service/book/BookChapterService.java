package com.puyixiaowo.fblog.service.book;

import com.puyixiaowo.fblog.bean.admin.book.BookChapterBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Moses
 * @date 2017-12-19
 * 
 */
public class BookChapterService {

    public static String getSelectSql(BookChapterBean bookChapterBean,
                                                      PageBean pageBean) {

        StringBuilder sbSql = new StringBuilder("select id,book_id,sort,name,status from book_chapter where 1 = 1 ");

        buildSqlParams(sbSql, bookChapterBean);
        sbSql.append("order by " +
                pageBean.getOrder() +
                (pageBean.getReverse() ? " desc " : " asc "));
        sbSql.append("limit ");
        sbSql.append(pageBean.getRowBounds().getOffset());
        sbSql.append(", ");
        sbSql.append(pageBean.getRowBounds().getLimit());
        return sbSql.toString();
    }
    public static PageBean selectBookChapterPageBean(BookChapterBean bookChapterBean, PageBean pageBean){
        return DBUtils.selectPageBean(getSelectSql(bookChapterBean, pageBean), bookChapterBean, pageBean);
    }
    public static void buildSqlParams(StringBuilder sbSql,
                                               BookChapterBean bookChapterBean) {
        if (bookChapterBean.getName() != null) {
            sbSql.append("and name like :name ");
            bookChapterBean.setName("%" + bookChapterBean.getName() + "%");
        }

        if (bookChapterBean.getBookId() != null) {
            sbSql.append("and book_id = :bookId ");
        }
    }

    public static List<BookChapterBean> getBookChapterList(Long bookId) {
        BookChapterBean bookChapterBean = new BookChapterBean();
        bookChapterBean.setBookId(bookId);

        PageBean pageBean = new PageBean();
        pageBean.setOrder("sort");
        List<BookChapterBean> list = selectBookChapterPageBean(bookChapterBean, pageBean).getList();
        if (list != null
                && list.size() > 0) {
            return list;
        }
        return new ArrayList<>();
    }

    public static int getChapterLastSort(Long bookId) {
        BookChapterBean bookChapterBean = new BookChapterBean();
        bookChapterBean.setBookId(bookId);
        bookChapterBean = DBUtils.selectOne("select * from book_chapter where " +
                        "book_id=:bookId order by sort desc limit 1",
                bookChapterBean);

        if (bookChapterBean == null) {

            return 0;
        }

        return bookChapterBean.getSort();
    }

//    public static BookChapterBean getBookLastChapter(Long bookId) {
//        BookChapterBean bookChapterBean = new BookChapterBean();
//        bookChapterBean.setBookId(bookId);
//        bookChapterBean = DBUtils.selectOne("select * from book_chapter where " +
//                "book_id=:bookId order by id desc limit 1", bookChapterBean);
//        return bookChapterBean;
//    }
}
