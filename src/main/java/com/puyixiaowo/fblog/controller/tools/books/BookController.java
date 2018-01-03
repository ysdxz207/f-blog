package com.puyixiaowo.fblog.controller.tools.books;

import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.core.entity.RowBounds;
import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.bean.admin.book.BookBean;
import com.puyixiaowo.fblog.bean.admin.book.BookChapterBean;
import com.puyixiaowo.fblog.bean.admin.book.BookReadBean;
import com.puyixiaowo.fblog.bean.admin.book.BookSource;
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

        UserBean userBean = request.session().attribute(Constants.SESSION_USER_KEY);

        model.put("userBean", userBean);
        model.put("pageBean", pageBean);
        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(model, "tools/book/book_index.html"));
    }

    public static Object bookDetail(Request request, Response response) {
        Map<String, Object> model = new HashMap<>();

        BookBean bookBean = null;
        try {

            bookBean = getParamsEntity(request, BookBean.class, true);
            bookBean = BookService.requestBookDetail(bookBean);

            //是否在书架里
            bookBean.setOnShelf(BookshelfService.isBookOnShelf(request.session().attribute(Constants.SESSION_USER_KEY),
                    bookBean));
            //保存或更新书籍信息
            BookBean bookBeanDB = BookService.selectBookBeanByAId(bookBean.getaId());
            if (bookBeanDB != null) {
                bookBean.setId(bookBeanDB.getId());
            }
            DBUtils.insertOrUpdate(bookBean);
        } catch (Exception e) {
            logger.error("[书]获取章节列表异常：" + e.getMessage());
        }

        model.put("book", bookBean);

        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(model, "tools/book/book_detail.html"));
    }

    public static Object chapterContent(Request request, Response response) {
        String link = request.queryParams("link");
        String bookIdStr = request.queryParams("bookId");
        String chapterName = request.queryParams("chapterName");
        String aId = request.queryParams("aId");
        String source = request.queryParams("source");

        long start = System.currentTimeMillis();

        Integer page = Integer.valueOf(request.queryParamOrDefault("page", "0"));

        if (StringUtils.isBlank(bookIdStr)
                && StringUtils.isBlank(aId)) {
            return "bookId和aId不可同时为空";
        }

        String HTML_CHANGE_SOURCE = "<div style='color: #DDD;text-align:center;height:400px;line-height:400px'>无法获取书籍，请切换书源</div>";
        Long bookId = Long.valueOf(bookIdStr);
        Map<String, Object> model = new HashMap<>();
        try {
            UserBean userBean = request.session().attribute(Constants.SESSION_USER_KEY);
            //读取读书配置
            BookReadBean bookReadBean = getParamsEntity(request, BookReadBean.class, false);

            if (StringUtils.isNotBlank(bookReadBean
                    .getLastReadingChapterLink())) {
                bookReadBean.setLastReadingChapterLink(
                        URLEncoder.encode(bookReadBean.getLastReadingChapterLink(), Constants.ENCODING));
            }
            //无本地化配置,读取服务器配置
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
                    BookChapterBean bookChapterBean = BookChapterService
                            .requestFirstBookChapters(userBean.getId(), bookId);

                    if (bookChapterBean == null) {
                        return HTML_CHANGE_SOURCE;
                    }
                    link = bookChapterBean.getLink();
                    source = bookChapterBean.getSource();
                }
            }

            BookChapterBean bookChapterBean = null;

            bookChapterBean = BookChapterService.requestBookContent(link);


            if (bookChapterBean == null) {
                //提示切换书源
                return HTML_CHANGE_SOURCE;
            }
            if (".".equals(bookChapterBean.getTitle()== null ? "" : bookChapterBean.getTitle().trim())) {

                if (StringUtils.isNotBlank(chapterName)) {
                    bookChapterBean.setTitle(chapterName);
                } else if (bookReadBean != null) {
                    bookChapterBean.setTitle(bookReadBean.getLastReadingChapter());
                }
            }


            bookChapterBean.setBookId(bookId);
            String content = bookChapterBean
                    .getContent().replaceAll("\n", "</p>\n<p>&nbsp;&nbsp;&nbsp;&nbsp;");
            bookChapterBean.setContent(content);
            if (StringUtils.isNotBlank(source)) {
                //第一次读书可以获取到source
                bookChapterBean.setSource(source);
            }
            model.put("model", bookChapterBean);

        } catch (Exception e) {
            logger.error("[书]获取章节内容异常：" + e.getMessage());
            e.printStackTrace();
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



    public static Object searchPage(Request request, Response response) {

        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(null, "tools/book/book_search.html"));
    }

    public static Object search(Request request, Response response) {

        PageBean pageBean = getPageBean(request);
        try {
            String name = request.queryParams("name");

            RowBounds rowBounds = pageBean.getRowBounds();
            rowBounds.setLimit(100);
            pageBean.setRowBounds(rowBounds);
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
            UserBean userBean = request.session().attribute(Constants.SESSION_USER_KEY);
            responseBean.setData(BookChapterService
                    .requestBookChapters(userBean.getId(), bookId));
        } catch (Exception e) {
            responseBean.error(e);
        }

        return responseBean.serialize();
    }

    public static Object addOrDelBook(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();

        try {
            BookBean bookBean = getParamsEntity(request, BookBean.class, true);
            BookBean bookBeanDB = BookService.selectBookBeanByAId(bookBean.getaId());

            if (bookBeanDB != null) {
                bookBean.setId(bookBeanDB.getId());
            }

            bookBean = BookService.requestBookDetail(bookBean);

            //添加或更新书籍信息
            DBUtils.insertOrUpdate(bookBean);

            UserBean userBean = request.session().attribute(Constants.SESSION_USER_KEY);

            //增加到书架或删除书籍
            boolean isOnBookshelf = BookshelfService.addOrDelBookFromBookshelf(userBean, bookBean.getId());

            responseBean.setData(isOnBookshelf);
        } catch (Exception e) {
            responseBean.error(e);
            return responseBean;
        }
        return responseBean.serialize();
    }

    public static Object bookSource(Request request, Response response) {

        String aId = request.queryParams("aId");
        String title = request.queryParams("title");
        if (StringUtils.isBlank(aId)) {
            return "aId不可为空";
        }

        UserBean userBean = request.session().attribute(Constants.SESSION_USER_KEY);

        List<BookSource> list = BookService.getBookSource(aId);

        //查询bookRead获取当前书源
        BookBean bookBean = BookService.selectBookBeanByAId(aId);
        for (BookSource bookSource : list) {
            if (bookBean != null) {
                BookReadBean bookReadBean = BookReadService.getUserReadConfig(userBean.getId(), bookBean.getId());
                if (bookReadBean != null
                        && bookSource.get_id().equals(bookReadBean.getSource())) {
                    bookSource.setCurrentSource(true);
                }
            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("list", list);

        model.put("aId", aId);
        model.put("bookId", bookBean.getId());
        model.put("title", title);

        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(model, "tools/book/book_source.html"));
    }

    public static Object changeBookSource(Request request, Response response) {
        ResponseBean responseBean = new ResponseBean();
        String aId = request.queryParams("aId");
        String source = request.queryParams("source");
        String bookIdStr = request.queryParams("bookId");

        if (StringUtils.isBlank(aId)) {
            responseBean.errorMessage("书源Id为空");
            return responseBean.serialize();
        }

        UserBean userBean = request.session().attribute(Constants.SESSION_USER_KEY);

        try {
            Long bookId = Long.valueOf(bookIdStr);
            BookReadBean bookReadBean = BookReadService
                    .getUserReadConfig(userBean.getId(), bookId);
            bookReadBean.setSource(source);
            DBUtils.insertOrUpdate(bookReadBean);
            //切换书源后需要查询出当前章Link
            List<BookChapterBean> bookChapterBeanList = BookChapterService
                    .requestBookChapters(userBean.getId(), bookId);

            //获取当前章
            BookChapterBean chapter = null;
            for (BookChapterBean bookChapterBean : bookChapterBeanList) {
                if (StringUtils.getSimilarityRatio(bookReadBean.getLastReadingChapter(), bookChapterBean.getTitle()) > 0.9) {
                    chapter = bookChapterBean;
                    break;
                }
            }

            responseBean.setData(chapter);
        } catch (Exception e) {
            responseBean.error(e);
        }


        return responseBean.serialize();
    }
}
