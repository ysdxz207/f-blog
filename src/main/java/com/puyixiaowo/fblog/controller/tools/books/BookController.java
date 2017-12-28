package com.puyixiaowo.fblog.controller.tools.books;

import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.bean.admin.book.BookBean;
import com.puyixiaowo.fblog.bean.admin.book.BookChapterBean;
import com.puyixiaowo.fblog.bean.admin.book.BookReadBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.constants.BookConstants;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.book.BookChapterService;
import com.puyixiaowo.fblog.service.book.BookReadService;
import com.puyixiaowo.fblog.service.book.BookService;
import com.puyixiaowo.fblog.service.book.BookshelfService;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.HttpUtils;
import com.puyixiaowo.fblog.utils.IdUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Moses
 * @date 2017-12-18
 */
public class BookController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);


    public static Object userBooks(Request request, Response response) {
        Map<String, Object> model = new HashMap<>();

        PageBean pageBean = getPageBean(request);


        try {
            UserBean userBean = request.session().attribute(Constants.SESSION_USER_KEY);
            List<BookBean> list = BookService.getUserBookList(userBean.getId());
            pageBean.setList(list);
        } catch (Exception e) {
            pageBean.error(e);
        }

        model.put("pageBean", pageBean);
        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(model, "tools/book/book_index.html"));
    }

    public static Object bookDetail(Request request, Response response) {
        Map<String, Object> model = new HashMap<>();

        BookBean bookBean = null;
        try {

            Long bookId = Long.valueOf(request.queryParams("bookId"));

            bookBean = BookChapterService.requestBookDetail(bookId);

            //是否在书架里
            bookBean.setOnShelf(BookshelfService.isBookOnShelf(request.session().attribute(Constants.SESSION_USER_KEY),
                    bookBean));
        } catch (Exception e) {
            logger.error("[书]获取章节列表异常：" + e.getMessage());
        }

        model.put("model", bookBean);

        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(model, "tools/book/book_detail.html"));
    }

    public static Object chapterContent(Request request, Response response) {
        String link = request.queryParams("link");
        String bookIdStr = request.queryParams("bookId");
        String chapterName = request.queryParams("chapterName");

        long start = System.currentTimeMillis();

        Integer page = Integer.valueOf(request.queryParamOrDefault("page", "0"));

        if (StringUtils.isBlank(bookIdStr)) {
            return "参数不正确";
        }
        Long bookId = Long.valueOf(bookIdStr);
        Map<String, Object> model = new HashMap<>();

        try {
            UserBean userBean = request.session().attribute(Constants.SESSION_USER_KEY);
            //读取读书配置
            BookReadBean bookReadBean = getParamsEntity(request, BookReadBean.class, false);

            if (bookReadBean == null
                    || (StringUtils.isBlank(bookReadBean.getLastReadingChapter())
                    && StringUtils.isBlank(bookReadBean.getLastReadingChapterLink()))) {
                bookReadBean = BookReadService.getUserReadConfig(userBean.getId(), bookId);
            }



            if (StringUtils.isBlank(link)) {

                if (bookReadBean != null) {
                    link = bookReadBean.getLastReadingChapterLink();
                } else {
                    //获取第一章
                    BookChapterBean bookChapterBean = BookChapterService.requestFirstBookChapters(bookId);
                    link = bookChapterBean.getLink();
                }
            }


            BookChapterBean bookChapterBean = null;

            bookChapterBean = BookChapterService.requestBookContent(link);

            if (".".equals(bookChapterBean.getTitle().trim())) {

                if (StringUtils.isNotBlank(chapterName)) {
                    bookChapterBean.setTitle(chapterName);
                } else if (bookReadBean != null) {
                    bookChapterBean.setTitle(bookReadBean.getLastReadingChapter());
                }
            }


            bookChapterBean.setBookId(bookId);
            model.put("model", bookChapterBean);

            //查询章节列表
//            List<BookChapterBean> bookChapterBeanList = BookChapterService.requestBookChapters(bookId);
//            model.put("bookChapterList", bookChapterBeanList);

        } catch (Exception e) {
            logger.error("[书]获取章节内容异常：" + e.getMessage());
        }


        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(model, "tools/book/book_chapter_content.html"));
    }

    public static Object saveBookReadConfig(Request request,
                                            Response response) {
        ResponseBean responseBean = new ResponseBean();

        try {
            UserBean userBean = request.session().attribute(Constants.SESSION_USER_KEY);
            BookReadBean bookReadBean = getParamsEntity(request, BookReadBean.class, false);

            //读取读书配置
            BookReadBean bookReadBeanDB = BookReadService.getUserReadConfig(userBean.getId(), bookReadBean.getBookId());


            //更新读书配置
            if (bookReadBean == null) {
                bookReadBean = new BookReadBean();
            }

            if (bookReadBeanDB != null) {
                bookReadBean.setId(bookReadBeanDB.getId());
            }

            bookReadBean.setUserId(userBean.getId());
            bookReadBean.setBookId(bookReadBean.getBookId());
            DBUtils.insertOrUpdate(bookReadBean);
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }


    public static Object bookSource(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();
        String aId = request.queryParams("aId");

        if (StringUtils.isBlank(aId)) {
            responseBean.errorMessage("书源Id为空");
            return responseBean.serialize();
        }

        String url = BookConstants.URL_BOOK_SOURCE + aId;
        JSONObject json = HttpUtils.httpGet(url);
        responseBean.setData(json);
        return responseBean.serialize();
    }

    public static Object searchPage(Request request, Response response) {

        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(null, "tools/book/book_search.html"));
    }

    public static Object search(Request request, Response response) {

        PageBean pageBean = getPageBean(request);
        try {
            String name = request.queryParams("name");

            pageBean = BookService.requestSearchBook(name, pageBean);
        } catch (Exception e) {
            pageBean.error(e);
        }
        return pageBean.serialize();
    }

    public static Object chapters(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();

        try {
            String bookIdStr = request.queryParams("bookId");

            if (StringUtils.isBlank(bookIdStr)) {
                responseBean.errorMessage("bookId不可为空");
                return responseBean.serialize();
            }
            Long bookId = Long.valueOf(bookIdStr);
            responseBean.setData(BookChapterService.requestBookChapters(bookId));
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }
}
