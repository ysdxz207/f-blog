package com.puyixiaowo.fblog.controller.tools.books;

import com.puyixiaowo.core.entity.RowBounds;
import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.bean.admin.book.BookBean;
import com.puyixiaowo.fblog.bean.admin.book.BookChapterBean;
import com.puyixiaowo.fblog.bean.admin.book.BookReadBean;
import com.puyixiaowo.fblog.bean.admin.book.BookSource;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.book.BookChapterService;
import com.puyixiaowo.fblog.service.book.BookReadService;
import com.puyixiaowo.fblog.service.book.BookService;
import com.puyixiaowo.fblog.service.book.BookshelfService;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.net.URLEncoder;
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
            DBUtils.insertOrUpdate(bookBean, false);
        } catch (Exception e) {
            logger.error("[书]获取章节列表异常：" + e.getMessage());
        }

        model.put("book", bookBean);

        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(model, "tools/book/book_detail.html"));
    }

    public static Object chapterContent(Request request, Response response) {
        //章节列表选择章节时有link
        String link = request.queryParams("link");
        //必传,书籍详情页面看书只有bookId
        String bookIdStr = request.queryParams("bookId");
        //仅用于接口获取到章节名为.时显示
        String chapterName = request.queryParams("chapterName");
        //1下一章，-1上一章,0最后阅读章
        Integer page = Integer.valueOf(request.queryParamOrDefault("page", "0"));

        if (StringUtils.isBlank(bookIdStr)) {
            return "bookId不可为空";
        }


        Long bookId = Long.valueOf(bookIdStr);
        Map<String, Object> model = new HashMap<>();

        try {
            UserBean userBean = request.session().attribute(Constants.SESSION_USER_KEY);
            //读取读书配置
            BookReadBean bookReadBean = BookReadService.getUserReadConfig(userBean.getId(), bookId);

            BookBean bookBean = BookService.selectBookBeanById(bookId);

            if (bookBean == null) {
                return null;
            }

            BookChapterBean bookChapterBean = null;

            if (page != 0) {
                //上一章或下一章
                bookChapterBean = BookChapterService.getNextChapter(page, userBean.getId(),
                        bookId,
                        bookBean.getaId(),
                        bookReadBean.getLastReadingChapter());

                if (bookChapterBean == null) {
                    //首页或尾页跳转到书籍详情页
                    response.redirect("/book/detail?aId=" + bookBean.getaId());
                    return null;
                }

                link = bookChapterBean.getLink();
                chapterName = bookChapterBean.getTitle();
            } else if (StringUtils.isBlank(link)) {

                if (bookReadBean != null) {
                    link = bookReadBean.getLastReadingChapterLink();
                } else {
                    //获取第一章
                    bookChapterBean = BookChapterService
                            .requestFirstBookChapters(userBean.getId(), bookId, bookBean.getaId());
                    link = bookChapterBean.getLink();
                }
            }
            //保存读书配置
            bookReadBean.setLastReadingChapterLink(link);
            bookReadBean.setLastReadingChapter(chapterName);
            ResponseBean responseBean = BookReadService.saveBookRead(bookReadBean);

            System.out.println(responseBean);


            bookChapterBean = BookChapterService.requestBookContent(link);

            if (bookChapterBean == null) {
                String HTML_CHANGE_SOURCE = "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\">\n<div style='color: #DDD;text-align:center;height:400px;line-height:400px'>无法获取书籍，请<a href='/book/source?aId=" +
                        bookBean.getaId() + "'>切换书源</a></div>";
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

            //章节列表
            List<BookChapterBean> chapterBeanList = BookChapterService
                    .requestBookChapters(userBean.getId(), bookId, bookBean.getaId(), false);
            model.put("model", bookChapterBean);
            model.put("book", bookBean);
            model.put("bookRead", bookReadBean);
            model.put("bookChapters", chapterBeanList);

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


            //读书配置不存在
            if (bookReadBean == null) {
                responseBean.errorMessage("配置不存在");
                return responseBean.serialize();
            }
            //更新读书配置
            if (bookReadBeanDB != null) {
                bookReadBean.setId(bookReadBeanDB.getId());
            }

            bookReadBean.setUserId(userBean.getId());
            bookReadBean.setBookId(bookReadBean.getBookId());
            DBUtils.insertOrUpdate(bookReadBean, false);
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

            BookBean bookBean = BookService.selectBookBeanById(bookId);
            UserBean userBean = request.session().attribute(Constants.SESSION_USER_KEY);
            responseBean.setData(BookChapterService
                    .requestBookChapters(userBean.getId(), bookId, bookBean.getaId(), false));
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
            DBUtils.insertOrUpdate(bookBean, false);

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
            BookBean bookBean = BookService.selectBookBeanById(bookId);
            BookReadBean bookReadBean = BookReadService
                    .getUserReadConfig(userBean.getId(), bookId);

            if (bookReadBean == null) {
                bookReadBean = new BookReadBean();
                bookReadBean.setUserId(userBean.getId());
                bookReadBean.setBookId(bookId);
            }
            bookReadBean.setSource(source);
            DBUtils.insertOrUpdate(bookReadBean, false);
            //切换书源后需要查询出当前章Link
            List<BookChapterBean> bookChapterBeanList = BookChapterService
                    .requestBookChapters(userBean.getId(), bookId, bookBean.getaId(), false);

            //获取当前章
            BookChapterBean chapter = null;
            for (BookChapterBean bookChapterBean : bookChapterBeanList) {
                if (BookChapterService.isSameChapterTitle(bookReadBean.getLastReadingChapter(),
                        bookChapterBean.getTitle())) {
                    chapter = bookChapterBean;
                }
            }

            if (chapter == null) {
                //第一章
                chapter = BookChapterService
                        .requestFirstBookChapters(userBean.getId(), bookId, bookBean.getaId());

            }
            responseBean.setData(chapter);
        } catch (Exception e) {
            responseBean.error(e);
        }


        return responseBean.serialize();
    }
}
