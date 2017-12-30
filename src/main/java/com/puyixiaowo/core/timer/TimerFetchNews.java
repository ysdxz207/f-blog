package com.puyixiaowo.core.timer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.puyixiaowo.fblog.service.fnews.NewsFilterService;
import com.puyixiaowo.fblog.utils.FileUtils;
import com.puyixiaowo.fblog.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时获取新闻
 *
 * @author Moses
 * @date 2017-12-13
 */
public class TimerFetchNews extends TimerTask {


    private static final Logger logger = LoggerFactory.getLogger(TimerFetchNews.class);

    private static final String PATH_KWYWORDS = "conf/fnews/keywords.json";
    private static final String PATH_CHANNELS = "conf/fnews/channels.json";
    private static final int TIME_EVERY = 1 * 1 * 60 * 1000;
    public static final int CACHE_PAGE_SIZE = 3;

    private static Date FIRST_DATE;
    public static JSONArray KEYWORDS;
    public static JSONArray CHANNELS;


    public TimerFetchNews() {
        FIRST_DATE = getFirstStartDate();
        KEYWORDS = getKeywords();
        CHANNELS = getChannels();
    }

    private JSONArray getKeywords() {
        String jsonStr = FileUtils.readResourceFile(PATH_KWYWORDS);
        if (StringUtils.isBlank(jsonStr)) {
            logger.error("未获取到关键词信息，可能[" + PATH_KWYWORDS + "]内容为空。");
            return null;
        }

        return JSON.parseArray(jsonStr);
    }

    public JSONArray getChannels() {
        String jsonStr = FileUtils.readResourceFile(PATH_CHANNELS);
        if (StringUtils.isBlank(jsonStr)) {
            logger.error("未获取到频道信息，可能[" + PATH_CHANNELS + "]内容为空。");
            return null;
        }
        return JSON.parseArray(jsonStr);
    }

    public Date getFirstStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 5);
        return calendar.getTime();
    }

    @Override
    public void run() {
        NewsFilterService.fetchNews(CACHE_PAGE_SIZE);
    }

    public void start() {
        new Timer().schedule(
                new TimerFetchNews(),
                FIRST_DATE,
                TIME_EVERY);
    }


}
