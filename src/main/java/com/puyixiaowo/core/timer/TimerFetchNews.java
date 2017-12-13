package com.puyixiaowo.core.timer;

import com.alibaba.fastjson.JSONObject;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.utils.FileUtils;
import com.puyixiaowo.fblog.utils.HttpUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * 定时获取新闻
 * @author Moses
 * @date 2017-12-13 17:46:58
 */
public class TimerFetchNews extends TimerTask{


    private static final Logger logger = LoggerFactory.getLogger(TimerFetchNews.class);

    private static final int TIME_EVERY = 1 * 1 * 60 * 1000;
    private static Date FIRST_DATE;

    private static final String NEWS_API_URL = "http://route.showapi.com/109-35";
    private static final String APIKEY = "955daa5dd0644ca796aaa978c4c951aa";
    private static final String APPID= "24272";

    public TimerFetchNews() {
        FIRST_DATE = getFirstStartDate();
    }

    public Date getFirstStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 5);
        return calendar.getTime();
    }

    @Override
    public void run() {
        logger.info("开始获取...");
        Map<String, String> params = new HashMap<>();
        params.put("needHtml", "1");
        params.put("showapi_res_gzip", "0");
        params.put("showapi_appid", APPID);
        params.put("showapi_sign", APIKEY);
        JSONObject jsonResult = HttpUtils.httpPost(NEWS_API_URL, params);
        System.out.println(jsonResult);
    }

    public void start () {
        new Timer().schedule(
                new TimerFetchNews(),
                FIRST_DATE,
                TIME_EVERY);
    }

    public static void main(String[] args) {
        Map<String, String> params = new HashMap<>();
        params.put("needHtml", "1");
        params.put("showapi_res_gzip", "0");
        params.put("showapi_appid", APPID);
        params.put("showapi_sign", APIKEY);
        JSONObject jsonResult = HttpUtils.httpPost(NEWS_API_URL, params);
        System.out.println(jsonResult);
    }
}
