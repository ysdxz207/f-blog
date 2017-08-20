package com.puyixiaowo.fblog.utils;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.annotation.Id;
import com.puyixiaowo.fblog.annotation.Transient;
import com.puyixiaowo.fblog.domain.User;
import com.puyixiaowo.fblog.exception.DBException;
import com.puyixiaowo.fblog.exception.DBSqlException;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import spark.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

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

    public static <T> T selectOne(Class clazz,
                                   String sql,
                                   Map<String, Object> params) {

        List<T> list = selectList(clazz, sql, params);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public static <E> List<E> selectList(Class clazz,
                                         String sql,
                                         Map<String, Object> params) {

        setCamelMapping(clazz);
        try (Connection conn = sql2o.open()) {
            Query query = conn.createQuery(sql).throwOnMappingFailure(false);

            try {
                if (params != null) {
                    for (Map.Entry<String, Object> entry :
                            params.entrySet()) {
                        query.addParameter(entry.getKey(), entry.getValue());
                    }
                }
            } catch (Exception e) {
                throw new DBSqlException("Add parameter :[" +
                sql + "],error:" + e.getMessage());
            }

            List<E> list = new ArrayList<>();
            try {
                list = query.executeAndFetch(clazz);
            } catch (Exception e) {
                throw new DBSqlException("Execute sql:[" + sql + "],error:" + e.getMessage());
            }
            return list;
        }
    }

    public static <E> List<E> selectList(Class clazz,
                                         String sql,
                                         Object params){
        Map<String, Object> map =  JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(params)), Map.class);
        return selectList(clazz, sql, map);
    }

    /**
     * 下划线映射为驼峰
     * @param clazz
     */
    private static void setCamelMapping(Class clazz) {

        Field [] fields = ReflectionUtils.getFieldListByClass(clazz);
        Map<String, String> mapping = new HashMap<>();
        for (Field field:
                fields) {

            if (!"serialVersionUID".equals(field.getName())
                    && CamelCaseUtils.checkIsCamelCase(field.getName())){
                mapping.put(CamelCaseUtils.toUnderlineName(field.getName()), field.getName());
            }
        }
        sql2o.setDefaultColumnMappings(mapping);
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

        System.out.println(sql_update);

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


        switch (sqlType) {
            case SQL_TYPE_INSERT:

                //insert sql
                sb_sql.append("insert into ");
                sb_sql.append(tableName);
                sb_sql.append("(");


                for (int i = 0; i < filelds.length; i++) {
                    Field field = filelds[i];
                    if (field.getAnnotation(Transient.class) != null) {
                        continue;
                    }
                    field.setAccessible(true);
                    String fieldName = ReflectionUtils.getFieldColumnName(field);
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
                Map<String, Object> primaryKeyValueMap = ReflectionUtils.getPrimaryKeyValues(obj);

                //update sql
                sb_sql.append("update ");
                sb_sql.append(tableName);
                sb_sql.append(" set ");


                for (int i = 0; i < filelds.length; i++) {
                    Field field = filelds[i];
                    if (field.getAnnotation(Transient.class) != null) {
                        continue;
                    }
                    field.setAccessible(true);
                    String fieldName = ReflectionUtils.getFieldColumnName(field);
                    Object fieldValue = "";
                    try {
                        fieldValue = field.get(obj);
                    } catch (IllegalAccessException e) {
                    }
                    if ("serialVersionUID".equals(fieldName) ||
                            fieldValue == null ||
                            StringUtils.isBlank(fieldValue.toString()) ||
                            field.getAnnotation(Id.class) != null) {
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
                sb_sql.append(" where ");

                Iterator<Map.Entry<String, Object>> it = primaryKeyValueMap.entrySet().iterator();

                while (it.hasNext()) {
                    Map.Entry entry = it.next();
                    Object name = entry.getKey();
                    Object id = entry.getValue();
                    if (id == null) {
                        throw new DBSqlException("Update table error: primary key [" + name + "] is null");
                    }
                    sb_sql.append(name);
                    sb_sql.append("=");
                    sb_sql.append("'");
                    sb_sql.append(id);
                    sb_sql.append("'");
                    if (it.hasNext())
                    sb_sql.append(" and ");
                }

                break;
            default:
                //insert sql

        }

        return sb_sql.toString();
    }


    public static void main(String[] args) throws Exception {

        DBUtils.initDB("D:\\workspace\\idea\\f-blog\\f_blog.db");
        List<User> userList = DBUtils.selectList(User.class,
                "select * from user " +
                        "where loginname =:loginname",
                new HashMap<String, Object>(){
                    {
                        put("loginname", "feihong");
                    }
                });
        System.out.println(userList.get(0).getId());
    }

    public static int count(String sql, Object paramObj) {
        try (Connection conn = sql2o.open()) {
            Query query = conn.createQuery(sql).throwOnMappingFailure(false).bind(paramObj);
            return query.executeScalar(Integer.class);
        }
    }
}
