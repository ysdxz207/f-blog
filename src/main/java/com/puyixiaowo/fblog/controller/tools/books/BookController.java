package com.puyixiaowo.fblog.controller.tools.books;

import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.bean.admin.book.BookBean;
import com.puyixiaowo.fblog.bean.admin.book.BookChapterBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.bean.sys.ResponseBean;
import com.puyixiaowo.fblog.constants.BookConstants;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.controller.BaseController;
import com.puyixiaowo.fblog.freemarker.FreeMarkerTemplateEngine;
import com.puyixiaowo.fblog.service.book.BookChapterService;
import com.puyixiaowo.fblog.service.book.BookService;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.HttpUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Moses
 * @date 2017-12-18
 */
public class BookController extends BaseController {


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

    public static Object bookChapters(Request request, Response response) {
        Map<String, Object> model = new HashMap<>();


        PageBean pageBean = getPageBean(request);

        try {
            BookChapterBean bookChapterBean = getParamsEntity(request, BookChapterBean.class, false);
            pageBean = BookChapterService.selectBookChapterPageBean(bookChapterBean, pageBean);
        } catch (Exception e) {
            pageBean.error(e);
        }

        model.put("pageBean", pageBean);

        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(model, "tools/book/book_chapter_list.html"));
    }

    public static Object chapterContent(Request request, Response response) {
        Map<String, Object> model = new HashMap<>();

        BookChapterBean bookChapterBean = null;
        try {
            bookChapterBean = getParamsEntity(request, BookChapterBean.class, false);

            bookChapterBean = DBUtils.selectOne("select * from book_chapter where id =:id", bookChapterBean);
            bookChapterBean.setContent("&nbsp;&nbsp;&nbsp;&nbsp;" + bookChapterBean.getContent().replaceAll("\n", "</br>&nbsp;&nbsp;&nbsp;&nbsp;"));
        } catch (Exception e) {

        }

        model.put("model", bookChapterBean);

        return new FreeMarkerTemplateEngine()
                .render(new ModelAndView(model, "tools/book/book_chapter_content.html"));
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
}
