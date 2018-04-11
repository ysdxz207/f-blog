package com.puyixiaowo.core.timer;

import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.utils.ConfigUtils;
import com.puyixiaowo.fblog.utils.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Moses
 * @date 2017-09-27
 * 每天零点执行备份f_blog.db
 */
public class TimerBackupDB extends TimerTask{

    private static final Logger logger = LoggerFactory.getLogger(TimerBackupDB.class);

    private static final int TIME_EVERY_DAY = 1 * 24 * 60 * 60 * 1000;
    private static Date FIRST_DATE;



    public TimerBackupDB() {
        FIRST_DATE = getTomorrowDate();
    }

    @Override
    public void run() {
        logger.info("开始数据库备份...");
        String SOURCE_FILE_NAME = ConfigUtils.DB_FILE_NAME;
        String TARGET_FILE_NAME = Constants.BACK_UP_DB_FILE_PATH
                + "/"
                + DateFormatUtils.format(new Date(), "yyyy-MM-dd")
                + ".fblog.db";

        File source = new File(SOURCE_FILE_NAME);
        File target = new File(TARGET_FILE_NAME);
        if (!target.getParentFile().exists()) {
            target.getParentFile().mkdirs();
        }
        FileUtils.copyFile(source, target);
    }

    private static Date getTomorrowDate() {
        LocalDate localDate = LocalDate.now(ZoneId.systemDefault()).plusDays(1L);
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public void start () {
        new Timer().schedule(
                new TimerBackupDB(),
                FIRST_DATE,
                TIME_EVERY_DAY);
    }
}
