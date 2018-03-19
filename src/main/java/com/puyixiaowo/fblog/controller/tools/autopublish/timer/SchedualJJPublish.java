package com.puyixiaowo.fblog.controller.tools.autopublish.timer;

import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.utils.RedisUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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
        RedisUtils.set(EnumsRedisKey.REDIS_KEY_TOOLS_AUTOPUBLISH_PUBDATE.key,
                DateFormatUtils.format(pubDate,
                        "yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public void run() {
        logger.info("发布...");



        RedisUtils.delete(EnumsRedisKey.REDIS_KEY_TOOLS_AUTOPUBLISH_PUBDATE.key);
    }

    public void start () {
        new Timer().schedule(
                this,
                DELAY);
    }
}
