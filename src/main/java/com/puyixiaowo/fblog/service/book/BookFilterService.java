package com.puyixiaowo.fblog.service.book;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.core.thread.BookByFilterThread;
import com.puyixiaowo.fblog.bean.admin.book.BookBean;
import com.puyixiaowo.fblog.bean.admin.book.BookChapterBean;
import com.puyixiaowo.fblog.bean.admin.book.BookshelfBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.constants.BookConstants;
import com.puyixiaowo.fblog.domain.BookChapter;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.HttpUtils;
import com.puyixiaowo.fblog.utils.IdUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

/**
 * @author Moses
 * @date 2017-12-19
 */
public class BookFilterService {
    private static final Logger logger = LoggerFactory.getLogger(BookFilterService.class);

    public static List<BookBean> fetchBookUpdate() {


        List<BookBean> list = new ArrayList<>();

        List<BookshelfBean> bookshelfBeanList = BookshelfService
                .selectBookshelfPageBean(new BookshelfBean(),
                        new PageBean()).getList();

        List<BookBean> result;
        // 创建一个通用池
        ForkJoinPool pool = ForkJoinPool.commonPool();

        // 提交可分解的CalTask任务
        try {
            Future<List<BookBean>> future = pool.submit(new BookByFilterThread(bookshelfBeanList, 0, true));
            result = future.get();
            list.addAll(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return list;
    }


    public static List<BookBean> fetchUserBookUpdate(Long userId) {
        List<BookBean> list = new ArrayList<>();

        List<BookBean> bookBeanList = BookService.getUserBookList(userId);

        for (BookBean bookBean :
                bookBeanList) {
            list.addAll(requestFetchBookUpdate(bookBean));
        }
        return list;
    }

    public static List<BookBean> requestFetchBookUpdate(BookBean bookBean) {
        List<BookBean> list = new ArrayList<>();
        System.out.println("开始获取书：" + bookBean.getName());
        List<BookChapterBean> needFetchChapters = getNeedFetchChapters(bookBean);

        for (BookChapterBean bookChapterBean :
                needFetchChapters) {
            String content = requestBookContent(bookChapterBean);
            bookChapterBean.setContent(content);
            DBUtils.insertOrUpdate(bookChapterBean);
        }
        return list;
    }

    private static String requestBookContent(BookChapterBean bookChapterBean) {
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

        int startIndex = localChapterList.size() - 1 < 0 ? 0 : localChapterList.size() - 1;
        int endIndex = apiChapterList.size() - 1 < 0 ? 0 : apiChapterList.size() - 1;

        return apiChapterList.subList(startIndex,
                endIndex);
    }

    private static List<BookChapterBean> requestBookChapters(BookBean bookBean) {
        List<BookChapterBean> list = new ArrayList<>();
        
        String url = BookConstants.URL_CHAPTERS + bookBean.getAId();

        JSONObject jsonObject = HttpUtils.httpGet(url);
        JSONArray chapters = jsonObject.getJSONObject("mixToc").getJSONArray("chapters");
        if (chapters == null) {
            logger.error("[book]未从api返回的数据中获取到章节列表:" + jsonObject);
            return list;
        }

        for (Object obj :
                chapters) {
            JSONObject json = (JSONObject) obj;
            BookChapterBean bookChapterBean = new BookChapterBean();
            bookChapterBean.setId(IdUtils.generateId());
            bookChapterBean.setBookId(bookBean.getId());
            bookChapterBean.setName(json.getString("title"));
            bookChapterBean.setLink(json.getString("link"));
            bookChapterBean.setStatus(json.getBoolean("unreadble") ? 1 : 0);
            list.add(bookChapterBean);
        }

        return list;
    }

}
