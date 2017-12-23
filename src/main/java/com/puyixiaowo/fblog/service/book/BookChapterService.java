package com.puyixiaowo.fblog.service.book;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.fblog.bean.admin.book.BookBean;
import com.puyixiaowo.fblog.bean.admin.book.BookChapterBean;
import com.puyixiaowo.fblog.bean.admin.book.BookInfo;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.constants.BookConstants;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.utils.DBUtils;
import com.puyixiaowo.fblog.utils.HttpUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import freemarker.template.utility.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Moses
 * @date 2017-12-19
 */
public class BookChapterService {

    private static final Logger logger = LoggerFactory.getLogger(BookChapterService.class);

    public static List<BookChapterBean> requestBookChapters(Long bookId) {

        List<BookChapterBean> list = new ArrayList<>();

        BookBean bookBean = BookService.selectBookBeanById(bookId);

        String url = BookConstants.URL_CHAPTERS + bookBean.getSource() + "?view=chapters";

        JSONObject jsonObject = HttpUtils.httpGet(url);
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
                bookChapterBean.setBookId(bookBean.getId());
                bookChapterBean.setTitle(json.getString("title"));
                bookChapterBean.setLink(URLEncoder.encode(json.getString("link"), Constants.ENCODING));
                list.add(bookChapterBean);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Collections.reverse(list);
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
        JSONObject json = HttpUtils.httpGet(url);

        if (json == null) {
            logger.error("[book]api返回章节内容json为null");
            return null;
        }
        boolean ok = json.getBoolean("ok") == null ? false : json.getBoolean("ok");
        if (ok) {
            JSONObject chapter = json.getJSONObject("chapter");

            String title = chapter.getString("title");
            String content = chapter.getString("body");
            bookChapterBean.setTitle(title);
            bookChapterBean.setContent(content);
            bookChapterBean.setLink(link);
            return bookChapterBean;
        }
        return null;
    }

    public static BookBean requestBookDetail(Long bookId) {

        BookBean bookBean = BookService.selectBookBeanById(bookId);

        String url = BookConstants.URL_BOOK + bookBean.getAId();

        JSONObject json = HttpUtils.httpGet(url);

        Boolean ok = json.getBoolean("ok");;



        if (ok != null && !ok) {
            ok = false;
        } else {
            ok = true;
        }

        if (json == null && !ok) {
            return null;
        }

        String description = json.getString("longIntro");
        String cover = json.getString("cover");
        try {
            cover = URLDecoder.decode(cover, Constants.ENCODING);
            cover = cover.replace("/agent/", "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String rating = json.getString("rating");
        String retentionRatio = json.getString("retentionRatio");//读着留存率
        String updated = json.getString("updated");
        String lastChapter = json.getString("lastChapter");
        String category = json.getString("cat");


        BookInfo bookInfo = new BookInfo();
        bookInfo.setBookId(bookId);
        bookInfo.setDescription(description);
        bookInfo.setCover(cover);
        bookInfo.setRating(rating);
        bookInfo.setRetentionRatio(retentionRatio);
        bookInfo.setUpdated( updated);
        bookInfo.setLastChapter(lastChapter);
        bookInfo.setCategory(category);

        bookBean.setBookInfo(bookInfo);
        return bookBean;
    }

    public static BookChapterBean requestFirstBookChapters(Long bookId) {

        List<BookChapterBean> list = requestBookChapters(bookId);
        return list.get(list.size()-1);
    }
}
