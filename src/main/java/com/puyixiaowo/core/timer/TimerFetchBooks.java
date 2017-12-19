package com.puyixiaowo.core.timer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.puyixiaowo.fblog.service.book.BookFilterService;
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
 * 定时获取书
 *
 * @author Moses
 * @date 2017-12-13
 */
public class TimerFetchBooks extends TimerTask {


    private static final Logger logger = LoggerFactory.getLogger(TimerFetchBooks.class);

    private static final int TIME_EVERY = 1 * 1 * 60 * 1000;

    private static Date FIRST_DATE;


    public TimerFetchBooks() {
        FIRST_DATE = getFirstStartDate();
    }

    public Date getFirstStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 5);
        return calendar.getTime();
    }

    @Override
    public void run() {
        BookFilterService.fetchBookUpdate();
    }

    public void start() {
        new Timer().schedule(
                new TimerFetchBooks(),
                FIRST_DATE,
                TIME_EVERY);
    }


}
