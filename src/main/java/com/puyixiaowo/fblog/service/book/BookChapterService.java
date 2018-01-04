package com.puyixiaowo.fblog.service.book;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.bean.admin.book.BookBean;
import com.puyixiaowo.fblog.bean.admin.book.BookChapterBean;
import com.puyixiaowo.fblog.bean.admin.book.BookInfo;
import com.puyixiaowo.fblog.bean.admin.book.BookReadBean;
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

    public static List<BookChapterBean> requestBookChapters(Long userId, Long bookId, String aId) {

        List<BookChapterBean> list = new ArrayList<>();

        BookReadBean bookReadBean = BookReadService.getUserReadConfig(userId, bookId);

        String source = bookReadBean == null ? "" : bookReadBean.getSource();

        if (StringUtils.isBlank(source)) {
            source = BookService.getDefaultSource(aId, bookId).get_id();
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

        List<BookChapterBean> list = requestBookChapters(userId, bookId, aId);
        return list.size() == 0 ? null : list.get(list.size()-1);
    }
}
