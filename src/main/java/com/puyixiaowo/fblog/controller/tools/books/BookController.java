package com.puyixiaowo.fblog.controller.tools.books;

import com.puyixiaowo.fblog.bean.admin.book.BookBean;
import com.puyixiaowo.fblog.bean.admin.book.BookChapterBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.book.BookChapterService;
import com.puyixiaowo.fblog.service.book.BookService;
import com.puyixiaowo.fblog.utils.StringUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.List;

/**
 * @author Moses
 * @date 2017-12-18
 */
public class BookController extends BaseController {


    public static Object userBooks(Request request, Response response) {
        Boolean data = Boolean.valueOf(request.params(":data"));

        if (!data) {
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(null,
                            "book/book_index.html"));
        }


        PageBean pageBean = getPageBean(request);

        String userIdStr = request.queryParams("userId");
        if (StringUtils.isBlank(userIdStr)) {
            pageBean.errorMessage("缺少userId");
            return pageBean.serialize();
        }
        try {
            Long userId = Long.valueOf(userIdStr);
            List<BookBean> list = BookService.getUserBookList(userId);
            pageBean.setList(list);
        } catch (Exception e) {
            pageBean.error(e);
        }

        return pageBean.serialize();
    }

    public static Object bookChapters(Request request, Response response) {
        Boolean data = Boolean.valueOf(request.params(":data"));

        if (!data) {
            return new FreeMarkerTemplateEngine()
                    .render(new ModelAndView(null,
                            "book/book_chapters.html"));
        }


        PageBean pageBean = getPageBean(request);

        String bookIdStr = request.queryParams("bookId");
        if (StringUtils.isBlank(bookIdStr)) {
            pageBean.errorMessage("缺少bookId");
            return pageBean.serialize();
        }
        try {
            BookChapterBean bookChapterBean = getParamsEntity(request, BookChapterBean.class, false);
            pageBean = BookChapterService.selectBookChapterPageBean(bookChapterBean, pageBean);
        } catch (Exception e) {
            pageBean.error(e);
        }

        return pageBean.serialize();
    }
}
