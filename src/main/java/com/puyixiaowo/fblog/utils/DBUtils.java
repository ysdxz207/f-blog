package com.puyixiaowo.fblog.utils;

import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import spark.utils.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Moses
 * @date 2017-08-04 12:57
 */
public class DBUtils {

    private static final int SQL_TYPE_INSERT = 1;//添加
    private static final int SQL_TYPE_UPDATE = 2;//更新

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

    public static Sql2o getSql2o() {
        return sql2o;
    }

    public static <E> List<E> selectList(Class clazz,
                                         String sql,
                                         Map<String, Object> params) {


        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection conn = sql2o.open()) {
            Query query = conn.createQuery(sql).throwOnMappingFailure(false);

            if (params != null) {
                for (Map.Entry<String, Object> entry :
                        params.entrySet()) {
                    query.addParameter(entry.getKey(), entry.getValue());
                }
            }

            return query.executeAndFetch(clazz);
        }
    }

    public static void insertOrUpdate(String tableName,
                                      Map<String, Object> params){
        String sql_update = "";
        String sql_insert = "";


    }

    private static String assembleSql(int sqlType,
                                      String tableName,
                                      Map<String, Object> params){
        StringBuilder sb_sql = new StringBuilder();
        String sql_update = "";
        String sql_insert = "";
        switch (sqlType) {
            case SQL_TYPE_INSERT:
                //insert sql
                sb_sql.append("insert into ");
                sb_sql.append(tableName);
                sb_sql.append("(`id`,`creator`,`title`,`context`,`category`,`tagIds`,`createDate`,`lastUpdateDate`,`status`,`isDel`)");
                sb_sql.append("values(");
                sb_sql.append(tableName);
                sb_sql.append(tableName);
                sb_sql.append(tableName);

                break;
            case SQL_TYPE_UPDATE:
                //update sql

                break;
            default:
                //insert sql

        }

        return sb_sql.toString();
    }
}
