package com.puyixiaowo.fblog.service.book;

import com.puyixiaowo.fblog.bean.admin.book.BookReadSettingBean;
import com.puyixiaowo.fblog.utils.DBUtils;

public class BookReadSettingService {

    public static BookReadSettingBean getUserReadSetting(Long userId) {
        BookReadSettingBean bookReadSettingBean = new BookReadSettingBean();
        bookReadSettingBean.setUserId(userId);
        bookReadSettingBean = DBUtils.selectOne("select * from book_read_setting where " +
                "user_id = :userId", bookReadSettingBean);

        if (bookReadSettingBean == null) {
            bookReadSettingBean = new BookReadSettingBean();
            bookReadSettingBean.setUserId(userId);
            DBUtils.insertOrUpdate(bookReadSettingBean, false);
        }
        return bookReadSettingBean;
    }
}
