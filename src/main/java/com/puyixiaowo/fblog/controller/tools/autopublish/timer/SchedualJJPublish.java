package com.puyixiaowo.fblog.controller.tools.autopublish.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Moses
 */
public class SchedualJJPublish extends TimerTask{

    private static final Logger logger = LoggerFactory.getLogger(SchedualJJPublish.class);

    private static long DELAY;


    public SchedualJJPublish(Date pubDate) {
        DELAY = pubDate.getTime() - System.currentTimeMillis();
    }

    @Override
    public void run() {
        logger.info("发布...");
        

    }

    public void start () {
        new Timer().schedule(
                this,
                DELAY);
    }
}
