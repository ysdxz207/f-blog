package com.puyixiaowo.fblog.service.book;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.fblog.bean.admin.book.BookChapterBean;
import com.puyixiaowo.fblog.bean.admin.book.BookReadBean;
import com.puyixiaowo.fblog.constants.BookConstants;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.HttpUtils;
import com.puyixiaowo.fblog.utils.NumberUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Moses
 * @date 2017-12-19
 */
public class BookChapterService {

    private static final Logger logger = LoggerFactory.getLogger(BookChapterService.class);

    public static List<BookChapterBean> requestBookChapters(Long userId,
                                                            Long bookId,
                                                            String aId,
                                                            boolean keepSort) {

        List<BookChapterBean> list = new ArrayList<>();

        BookReadBean bookReadBean = BookReadService.getUserReadConfig(userId, bookId);

        bookReadBean = bookReadBean == null ? new BookReadBean() : bookReadBean;

        String source = bookReadBean.getSource();

        if (StringUtils.isBlank(source)) {
            source = BookService.getDefaultSource(aId).get_id();
            //可能为第一次读书或配置被删除
            bookReadBean.setSource(source);
            bookReadBean.setBookId(bookId);
            bookReadBean.setUserId(userId);
        }
        String url = BookConstants.URL_CHAPTERS + source + "?view=chapters";

        JSONObject jsonObject = JSON.parseObject(HttpUtils.httpGet(url, null));
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


        try {
            for (Object obj :
                    chapters) {
                JSONObject json = (JSONObject) obj;
                BookChapterBean bookChapterBean = new BookChapterBean();
                bookChapterBean.setBookId(bookId);
                bookChapterBean.setTitle(json.getString("title"));
                bookChapterBean.setLink(URLEncoder.encode(json.getString("link"), Constants.ENCODING));
                list.add(bookChapterBean);
                bookChapterBean.setSource(source);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        list = getChapterHasReadList(list, bookReadBean.getLastReadingChapter());


        if (bookReadBean.getId() == null) {
            //
            BookChapterBean firstChapter = list.get(0);
            bookReadBean.setLastReadingChapterLink(firstChapter.getLink());
            bookReadBean.setLastReadingChapter(firstChapter.getTitle());
            DBUtils.insertOrUpdate(bookReadBean, false);
        }
        if (!keepSort
                && bookReadBean.getSort() != null
                && bookReadBean.getSort() == 0) {
            Collections.reverse(list);
        }

        return list;
    }

    public static List<BookChapterBean> getChapterHasReadList(List<BookChapterBean> list, String lastReadingChapter) {
        int lastReadingIndex = getReadingChapterIndex(list, lastReadingChapter);

        for (int i = 0; i < list.size(); i++) {
            BookChapterBean bookChapterBean = list.get(i);
            if (i <= lastReadingIndex) {
                bookChapterBean.setHasRead(true);
            }
        }
        return list;
    }


    public static BookChapterBean requestBookContent(String link) {

        if (StringUtils.isBlank(link)) {
            return null;
        }

        String linkDecode = "";

        try {

            linkDecode = URLDecoder.decode(link, Constants.ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (linkDecode.equals(link)) {
            try {
                link = URLEncoder.encode(link, Constants.ENCODING);
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }

        BookChapterBean bookChapterBean = new BookChapterBean();
        String url = BookConstants.URL_CHAPTER_CONTENT + link;
        JSONObject json = JSON.parseObject(HttpUtils.httpGet(url, null));

        if (json == null) {
            logger.error("[book]api返回章节内容json为null");
            return null;
        }
        boolean ok = json.getBoolean("ok") == null ? false : json.getBoolean("ok");
        if (ok) {
            JSONObject chapter = json.getJSONObject("chapter");

            String title = chapter.getString("title");
            String content = chapter.getString("body");
            String cpContent = chapter.getString("cpContent");

            if (StringUtils.isNotBlank(cpContent)) {
                content = cpContent;
            }
            bookChapterBean.setTitle(title);
            bookChapterBean.setContent(content != null ? content.replace("<a href=\"", "") : content);
            bookChapterBean.setLink(link);
            return bookChapterBean;
        }
        return null;
    }


    public static BookChapterBean requestFirstBookChapters(Long userId, Long bookId, String aId) {

        List<BookChapterBean> list = requestBookChapters(userId, bookId, aId, true);
        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 判断是否同一章
     *
     * @param lastReadingChapter
     * @param title
     * @return
     */
    public static boolean isSameChapterTitle(String lastReadingChapter, String title) {

        if (StringUtils.isBlank(lastReadingChapter)
                && StringUtils.isBlank(title)) {
            return true;
        }

        if (StringUtils.isBlank(lastReadingChapter)
                && StringUtils.isNotBlank(title)) {
            return false;
        }

        if (StringUtils.isNotBlank(lastReadingChapter)
                && StringUtils.isBlank(title)) {
            return false;
        }


        String[] arr1 = lastReadingChapter.split("章");
        String[] arr2 = title.split("章");
        String title1 = lastReadingChapter;
        String title2 = title;
        if (arr1.length == 2
                && arr2.length == 2) {
            title1 = arr1[1];
            title2 = arr2[1];
        }
        if (StringUtils.getSimilarityRatio(title1.replaceAll("[\\pP\\p{Punct}]", ""), title2.replaceAll("[\\pP\\p{Punct}]", "")) > 0.9) {
            return true;
        }
        return false;
    }

    /**
     * 获取正在阅读的章节索引
     *
     * @param chapterBeanList
     * @param lastReadingChapter
     * @return
     */
    public static int getReadingChapterIndex(List<BookChapterBean> chapterBeanList,
                                             String lastReadingChapter) {
        if (chapterBeanList == null
                || chapterBeanList.size() == 0
                || StringUtils.isBlank(lastReadingChapter)) {
            return 0;
        }
        for (int i = 0; i < chapterBeanList.size(); i++) {
            BookChapterBean bookChapterBean = chapterBeanList.get(i);
            if (isSameChapterTitle(lastReadingChapter, bookChapterBean.getTitle())) {
                return i;
            }
        }

        return 0;
    }

    public static BookChapterBean getNextChapter(int page,
                                                 Long userId,
                                                 Long bookId,
                                                 String aId,
                                                 String lastReadingChapter) {

        List<BookChapterBean> bookChapterBeanList = requestBookChapters(userId, bookId, aId, true);

        int i = getReadingChapterIndex(bookChapterBeanList, lastReadingChapter);

        int index = i + page;
        if (index < 0
                || index >= bookChapterBeanList.size()) {
            return null;
        }
        return bookChapterBeanList.get(index);
    }

    public static int getChapterNum(String lastChapter) {

        Pattern pattern = Pattern.compile("第(.*)章");
        Matcher matcher1 = pattern.matcher(lastChapter);
        if (matcher1.find()) {
            lastChapter = matcher1.group(1);
        }

        return NumberUtils.hasNumber(lastChapter) ?
                StringUtils.parseInteger(lastChapter) :
                NumberUtils.convertToNumber(lastChapter);
    }
}
