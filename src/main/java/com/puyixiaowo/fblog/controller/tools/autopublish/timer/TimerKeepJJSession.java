package com.puyixiaowo.fblog.controller.tools.autopublish.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Moses
 */
public class TimerKeepJJSession extends TimerTask{

    private static final Logger logger = LoggerFactory.getLogger(TimerKeepJJSession.class);

    private static final int TIME_INTERVAL = 1 * 60 * 1000;
    private static Date FIRST_DATE;


    public TimerKeepJJSession() {
        FIRST_DATE = new Date();
    }

    @Override
    public void run() {
        logger.info("访问页面...");


    }

    public void start () {
        new Timer().schedule(
                new TimerKeepJJSession(),
                FIRST_DATE,
                TIME_INTERVAL);
    }
}
