package com.puyixiaowo.fblog.utils;

import org.sql2o.Sql2o;
import spark.utils.StringUtils;

/**
 * @author Moses
 * @date 2017-08-04 12:57
 */
public class DBUtils {

    private static Sql2o sql2o;

    public static void initDB() {

        String dbHost = (String) ResourceUtils.load("jdbc.properties").get("sqlite3.host");

        if (StringUtils.isBlank(dbHost)) {
            throw new RuntimeException("There is no db host found.");
        }


        sql2o = new Sql2o("jdbc:sqlite:" + dbHost, null, null);

        if (sql2o == null) {
            throw new RuntimeException("Can not find db " + dbHost);
        }

    }

    public static Sql2o getSql2o(){
        return sql2o;
    }
}
