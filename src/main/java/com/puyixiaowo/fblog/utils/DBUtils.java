package com.puyixiaowo.fblog.utils;

import com.puyixiaowo.fblog.domain.User;
import com.puyixiaowo.fblog.exception.DBException;
import com.puyixiaowo.fblog.exception.DBSqlException;
import com.sun.deploy.util.ReflectionUtil;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import spark.utils.StringUtils;

import java.lang.reflect.Field;
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
        initDB(null);
    }
    private static void initDB(String dbHost) {
        if (StringUtils.isBlank(dbHost)) {
            dbHost = (String) ResourceUtils.load("jdbc.properties").get("sqlite3.host");
        }

        if (StringUtils.isBlank(dbHost)) {
            throw new DBException("There is no db host found.");
        }


        sql2o = new Sql2o("jdbc:sqlite:" + dbHost, null, null);

        if (sql2o == null) {
            throw new DBException("Can not find db " + dbHost);
        }

    }

    public static Sql2o getSql2o() {
        return sql2o;
    }

    public static <E> List<E> selectList(Class clazz,
                                         String sql,
                                         Map<String, Object> params) {


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

    /**
     * insert or update
     * @param obj
     *          The object to insert or update.
     * @return
     */
    public static Object insertOrUpdate(Object obj) {

        String tableName = CamelCaseUtils.toUnderlineName(obj.getClass().getSimpleName().toLowerCase());

        String sql_update = assembleSql(SQL_TYPE_UPDATE, tableName, obj);
        String sql_insert = assembleSql(SQL_TYPE_INSERT, tableName, obj);

        try (Connection conn = sql2o.open()) {
            Query queryUpdate = conn.createQuery(sql_update).throwOnMappingFailure(false);
            Object primaryKey = queryUpdate.executeUpdate().getKey();

            Query queryInsert= conn.createQuery(sql_insert).throwOnMappingFailure(false);
            int lines = queryInsert.executeUpdate().getResult();

            if (primaryKey != null) {
                return primaryKey;
            }
            return lines;
        }
    }


    private static String assembleSql(int sqlType,
                                      String tableName,
                                      Object obj) {

        Field[] filelds = obj.getClass().getDeclaredFields();

        StringBuilder sb_sql = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        String sql_1 = "";
        String sql_2 = "";
        Object id = null;
        try {
            id = ReflectionUtil.invoke(obj, "getId", null, null);
        } catch (Exception e) {
            throw new DBSqlException("Can not invoke getId() method.");
        }
        switch (sqlType) {
            case SQL_TYPE_INSERT:

                //insert sql
                sb_sql.append("insert into ");
                sb_sql.append(tableName);
                sb_sql.append("(");


                for (int i = 0; i < filelds.length; i++) {
                    Field field = filelds[i];
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Object fieldValue = "";
                    try {
                        fieldValue = field.get(obj);
                    } catch (IllegalAccessException e) {
                    }
                    if ("serialVersionUID".equals(fieldName) ||
                            fieldValue == null ||
                            StringUtils.isBlank(fieldValue.toString())) {
                        continue;
                    }
                    sb1.append("`");
                    sb1.append(fieldName);
                    sb1.append("`");
                    sb1.append(",");

                    //
                    sb2.append("'");
                    sb2.append(fieldValue);
                    sb2.append("'");
                    sb2.append(",");

                }
                sql_1 = sb1.toString();
                sb_sql.append(sql_1.substring(0, sql_1.length() - 1));

                sb_sql.append(") ");

                sb_sql.append("values(");
                sql_2 = sb2.toString();
                sb_sql.append(sql_2.substring(0, sql_2.length() - 1));
                sb_sql.append(") ");

                break;
            case SQL_TYPE_UPDATE:
                if (id == null) {
                    throw new DBSqlException("Update table error:id is null");
                }
                //update sql
                sb_sql.append("update ");
                sb_sql.append(tableName);
                sb_sql.append(" set ");


                for (int i = 0; i < filelds.length; i++) {
                    Field field = filelds[i];
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Object fieldValue = "";
                    try {
                        fieldValue = field.get(obj);
                    } catch (IllegalAccessException e) {
                    }
                    if ("serialVersionUID".equals(fieldName) ||
                            fieldValue == null ||
                            StringUtils.isBlank(fieldValue.toString())) {
                        continue;
                    }
                    sb1.append("`");
                    sb1.append(fieldName);
                    sb1.append("`");
                    sb1.append("=");
                    sb1.append("'");
                    sb1.append(fieldValue);
                    sb1.append("'");
                    sb1.append(",");

                }
                sql_1 = sb1.toString();
                sb_sql.append(sql_1.substring(0, sql_1.length() - 1));
                sb_sql.append(" where id=");
                sb_sql.append("'");
                sb_sql.append(id);
                sb_sql.append("'");

                break;
            default:
                //insert sql

        }

        return sb_sql.toString();
    }

    public static void main(String[] args) throws Exception {

        User user = new User();
        user.setId(IdUtils.generateId());
        user.setPassword("113331");
        user.setUsername("444");

        insertOrUpdate(user);
    }
}
