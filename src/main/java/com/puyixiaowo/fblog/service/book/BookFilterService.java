package com.puyixiaowo.fblog.service.book;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.core.thread.BookByFilterThread;
import com.puyixiaowo.fblog.bean.admin.book.BookBean;
import com.puyixiaowo.fblog.bean.admin.book.BookChapterBean;
import com.puyixiaowo.fblog.bean.admin.book.BookshelfBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.constants.BookConstants;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * @author Moses
 * @date 2017-12-19
 */
public class BookFilterService {
    private static final Logger logger = LoggerFactory.getLogger(BookFilterService.class);

    public static void fetchBookUpdate() {


        List<BookshelfBean> bookshelfBeanList = BookshelfService
                .selectBookshelfPageBean(new BookshelfBean(),
                        new PageBean()).getList();

        // 创建一个通用池
        ForkJoinPool pool = ForkJoinPool.commonPool();

        // 提交可分解的CalTask任务
        pool.submit(new BookByFilterThread(bookshelfBeanList, 0, true));
    }


    public static void fetchUserBookUpdate(Long userId) {

        List<BookBean> bookBeanList = BookService.getUserBookList(userId);

        for (BookBean bookBean :
                bookBeanList) {
            requestFetchBookUpdate(bookBean);
        }
    }

    public static void requestFetchBookUpdate(BookBean bookBean) {
        logger.info("书[" + bookBean.getName() + "]开始获取...");
        List<BookChapterBean> needFetchChapters = getNeedFetchChapters(bookBean);
        if (needFetchChapters == null
                || needFetchChapters.size() == 0) {
            logger.info("书[" + bookBean.getName() + "]没有更新");
            return;
        }
        logger.info("书[" + bookBean.getName() + "]需要更新章数：" + needFetchChapters.size());

        int lastSort = BookChapterService.getChapterLastSort(bookBean.getId());

        logger.info("书[" + bookBean.getName() + "]最新章序号：" + lastSort);

        for (int i = 0; i<needFetchChapters.size(); i++) {
            ++lastSort;
            BookChapterBean bookChapterBean = needFetchChapters.get(i);
            String content = requestBookContent(bookChapterBean);
            bookChapterBean.setContent(content);
            bookChapterBean.setSort(lastSort);
            try {
                DBUtils.insertOrUpdate(bookChapterBean);
            } catch (Exception e) {
                //重复跳过
            }
        }
    }

    private static String requestBookContent(BookChapterBean bookChapterBean) {
        String url = "";
        try {
            url = BookConstants.URL_CHAPTER_CONTENT +
                    URLEncoder.encode(bookChapterBean.getLink(), Constants.ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject json = HttpUtils.httpGet(url);

        if (json == null) {
            logger.error("[book]api返回章节内容json为null");
            return null;
        }
        boolean ok = json.getBoolean("ok") == null ? false : json.getBoolean("ok");
        if (ok) {
            return json.getJSONObject("chapter").getString("body");
        }
        return null;
    }

    public static List<BookChapterBean> getNeedFetchChapters(BookBean bookBean) {

        List<BookChapterBean> apiChapterList = requestBookChapters(bookBean);
        if (apiChapterList == null
                || apiChapterList.size() == 0) {
            logger.error("[book]书[" + bookBean.getName() + "]未获取到章节列表");
            return null;
        }

        List<BookChapterBean> localChapterList = BookChapterService.getBookChapterList(bookBean.getId());

        if (apiChapterList.size() == localChapterList.size()) {
            return null;
        }

        int startIndex = localChapterList.size();
        int endIndex = apiChapterList.size();

        return apiChapterList.subList(startIndex,
                endIndex);
    }

    private static List<BookChapterBean> requestBookChapters(BookBean bookBean) {
        List<BookChapterBean> list = new ArrayList<>();
        
        String url = BookConstants.URL_CHAPTERS + bookBean.getAId() + "?view=chapters";

        JSONObject jsonObject = HttpUtils.httpGet(url);
        logger.info(jsonObject.toJSONString());
        if (jsonObject == null) {
            logger.error("[book]api返回章节json为null");
            return list;
        }

        if (jsonObject.size() == 0) {
            return list;
        }

        JSONArray chapters = jsonObject.getJSONArray("chapters");
        if (chapters == null) {
            logger.error("[book]未从api返回的数据中获取到章节列表:" + jsonObject);
            return list;
        }

        for (Object obj :
                chapters) {
            JSONObject json = (JSONObject) obj;
            BookChapterBean bookChapterBean = new BookChapterBean();
            bookChapterBean.setBookId(bookBean.getId());
            bookChapterBean.setName(json.getString("title"));
            bookChapterBean.setLink(json.getString("link"));
            bookChapterBean.setStatus(json.getBoolean("unreadble") ? 1 : 0);
            list.add(bookChapterBean);
        }

        return list;
    }

}
