package com.puyixiaowo.fblog.utils;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.annotation.Id;
import com.puyixiaowo.fblog.annotation.Transient;
import com.puyixiaowo.fblog.bean.admin.UserBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import com.puyixiaowo.fblog.exception.DBException;
import com.puyixiaowo.fblog.exception.DBObjectExistsException;
import com.puyixiaowo.fblog.exception.DBSqlException;
import org.sql2o.*;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Moses
 * @date 2017-08-04
 */
public class DBUtils {

    private static final int SQL_TYPE_INSERT = 1;//添加
    private static final int SQL_TYPE_UPDATE = 2;//更新
    private static final String SERIAL_VERSION_UID = "serialVersionUID";

    private static Sql2o sql2o;
    private static Properties dbProperties = new Properties();
    public static Sql2o getSql2o() {
        return sql2o;
    }

    public static Properties getDbProperties() {
        return dbProperties;
    }

    /**
     * 初始化DB
     * @param jdbcPropertyFilePath
     * @throws Exception
     */
    public static void initDB(String jdbcPropertyFilePath) throws Exception {

        dbProperties.load(DBUtils.class.getResourceAsStream("/" + jdbcPropertyFilePath));

        initDB(dbProperties);
    }

    /**
     * 初始化DB
     * @param url
     *          包含端口号的数据库地址
     * @param username
     * @param password
     * @throws Exception
     */
    public static void initDB(String url,
                              String username,
                              String password) throws Exception {

        Properties properties = new Properties();
        properties.setProperty("url", url);
        properties.setProperty("user", username);
        properties.setProperty("password", password);
        initDB(properties);
    }

    /**
     * 初始化DB
     * @param dbProperties
     * @throws Exception
     */
    public static void initDB(Properties dbProperties) throws Exception {

        String url = dbProperties.getProperty("url");
        if (url == null
                || "".equals(url.trim())
                || "null".equals(url.trim())) {
            throw new RuntimeException("数据库属性文件中属性[url]的值不正确");
        }

        if (dbProperties.containsKey("username")) {
            dbProperties.setProperty("user", dbProperties.getProperty("username"));
            dbProperties.remove("username");
        }

        DataSource dataSource = new GenericDatasource(url, dbProperties);

        sql2o = new Sql2o(dataSource);
    }


    public static <T> T selectOne(Class<T> clazz,
                                  String sql,
                                  Map<String, Object> params) {

        List<T> list = selectList(clazz, sql, params);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public static <E> E selectOne(String sql,
                                  E entity) {

        List<E> list = selectList(sql, entity);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public static <E> List<E> selectList(Class<E> clazz,
                                         String sql,
                                         Map<String, Object> params) {

        E entity = JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(params)), clazz);
        return selectList(sql, entity);
    }

    public static <E> List<E> selectList(String sql,
                                         E entity) {
        if (entity == null) {
            throw new DBException("Paramater entity should not be null.");
        }
        Class clazz = entity.getClass();
        setCamelMapping(clazz);
        try (Connection conn = sql2o.open()) {
            Query query = conn.createQuery(sql).throwOnMappingFailure(false);
            Field[] filelds = clazz.getDeclaredFields();

            for (Field field : filelds) {
                if (field.getAnnotation(Transient.class) != null
                        || SERIAL_VERSION_UID.equals(field.getName())) {
                    continue;
                }
                query.addParameter(field.getName(), ReflectionUtils.getFieldValue(entity, field.getName()));
            }

            return query.executeAndFetch(clazz);
        }
    }

    /**
     * 下划线映射为驼峰
     *
     * @param clazz
     */
    private static void setCamelMapping(Class clazz) {

        if (String.class.equals(clazz)) {
            return;
        }
        Field[] fields = ORMUtils.getFieldListByClass(clazz);
        Map<String, String> mapping = new HashMap<>();
        for (Field field :
                fields) {

            if (!SERIAL_VERSION_UID.equals(field.getName())
                    && CamelCaseUtils.checkIsCamelCase(field.getName())) {
                mapping.put(CamelCaseUtils.toUnderlineName(field.getName()), field.getName());
            }
        }
        sql2o.setDefaultColumnMappings(mapping);
    }

    /**
     * insert or update
     * @param obj The object to insert or update.
     * @param updateNull
     * @return
     */
    public static Object insertOrUpdate(Object obj,
                                        boolean updateNull) {

        String tableName = ORMUtils.getTableNameByClass(obj.getClass());

        try (Connection conn = sql2o.beginTransaction(java.sql.Connection.TRANSACTION_SERIALIZABLE)) {
            Object primaryKey = null;
            int lines = 0;
            try {
                String sql_update = assembleSql(SQL_TYPE_UPDATE, tableName, obj, updateNull);
                Query queryUpdate = conn.createQuery(sql_update).throwOnMappingFailure(false).bind(obj);
                lines = queryUpdate.executeUpdate().getResult();
            } catch (Exception e) {
                try {
                    ORMUtils.setId(obj);
                    String sql_insert = assembleSql(SQL_TYPE_INSERT, tableName, obj, updateNull);
                    System.out.println(sql_insert);
                    Query queryInsert = conn.createQuery(sql_insert).throwOnMappingFailure(false).bind(obj);
                    primaryKey = queryInsert.executeUpdate().getKey();
                } catch (Sql2oException e1) {
                    if (e1.getMessage() != null &&
                            e1.getMessage().contains("SQLITE_CONSTRAINT_UNIQUE")) {
                        throw new DBObjectExistsException("重复插入对象");
                    } else {
                        throw new DBException(e1.getMessage());
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            conn.commit();
            if (primaryKey != null) {
                return primaryKey;
            }
            return lines;
        }
    }


    private static String assembleSql(int sqlType,
                                      String tableName,
                                      Object obj,
                                      boolean updateNull) {

        //获取主键字段名和值map
        Map<String, Object> primaryKeyValueMap = ORMUtils.getPrimaryKeyValues(obj);

        //将值为0的主键值设为null
        for (Map.Entry entry :
                primaryKeyValueMap.entrySet()) {
            Object name = entry.getKey();
            Object value = entry.getValue();

            if (value != null
                    && value.toString().equals("0")) {
                primaryKeyValueMap.replace(name.toString(), null);
            }
        }

        Field[] filelds = obj.getClass().getDeclaredFields();

        StringBuilder sb_sql = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        String sql_1;
        String sql_2;


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
                    String columnName = ORMUtils.getFieldColumnName(field);
                    Object fieldValue = "";
                    try {
                        fieldValue = field.get(obj);
                    } catch (IllegalAccessException e) {
                    }
                    if (SERIAL_VERSION_UID.equals(columnName) ||
                            fieldValue == null ||
                            StringUtils.isBlank(fieldValue)) {
                        continue;
                    }
                    sb1.append("`");
                    sb1.append(columnName);
                    sb1.append("`");
                    sb1.append(",");

                    //
                    sb2.append(":" + field.getName());
                    sb2.append(",");

                }
                sql_1 = sb1.toString();
                sb_sql.append(sql_1, 0, sql_1.length() - 1);

                sb_sql.append(") ");

                sb_sql.append("values(");
                sql_2 = sb2.toString();
                sb_sql.append(sql_2, 0, sql_2.length() - 1);
                sb_sql.append(") ");

                break;
            case SQL_TYPE_UPDATE:

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
                    String columnName = ORMUtils.getFieldColumnName(field);
                    Object fieldValue = "";
                    try {
                        fieldValue = field.get(obj);
                    } catch (IllegalAccessException e) {
                    }
                    if (SERIAL_VERSION_UID.equals(columnName) ||
                            (updateNull && fieldValue == null) ||
                            StringUtils.isBlank(fieldValue) ||
                            field.getAnnotation(Id.class) != null ||
                            "id".equalsIgnoreCase(columnName)) {
                        continue;
                    }
                    sb1.append("`");
                    sb1.append(columnName);
                    sb1.append("`");
                    sb1.append("=");
                    sb1.append(":" + field.getName());
                    sb1.append(",");

                }
                sql_1 = sb1.toString();
                sb_sql.append(sql_1, 0, sql_1.length() - 1);
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


    public static int count(String sql, Object paramObj) {

        if (StringUtils.isBlank(sql)) {
            return 0;
        }
        if (sql.toLowerCase().contains("limit")) {
            sql = sql.replaceAll("limit +\\d+, *\\d+", "");
        }

        if (!sql.toLowerCase().replaceAll("\\s+", "").startsWith("selectcount(")) {
            sql = "select count(*) from ("
                    + (sql.endsWith(";") ? sql.substring(0, sql.length() - 1) : sql)
                    + ")";
        }

        try (Connection conn = sql2o.open()) {
            Query query = conn.createQuery(sql).throwOnMappingFailure(false).bind(paramObj);
            Integer count = query.executeScalar(Integer.class);
            if (count == null) {
                System.out.println("统计sql可能不正确：" + sql);
            }
            return count == null ? 0 : count;
        }
    }

    public static Integer deleteByIds(Class clazz, String ids) {
        if (StringUtils.isBlank(ids)) {
            return 0;
        }
        List<String> list = Arrays.asList(ids.split(","));
        if (list.isEmpty() || list.get(0) == null) {
            return 0;
        }

        StringBuilder sb_del_sql = new StringBuilder("delete from ");
        String tableName = ORMUtils.getTableNameByClass(clazz);

        sb_del_sql.append("`");
        sb_del_sql.append(tableName);
        sb_del_sql.append("` where id in(");


        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            sb_del_sql.append("'");
            sb_del_sql.append(it.next());
            sb_del_sql.append("'");
            if (it.hasNext())
                sb_del_sql.append(", ");
        }

        sb_del_sql.append(")");

        System.out.println(sb_del_sql.toString());
        try (Connection conn = sql2o.open()) {
            Query query = conn.createQuery(sb_del_sql.toString()).throwOnMappingFailure(false);

            return query.executeUpdate().getResult();
        }
    }

    public static Object executeSql(String sql, Map<String, Object> params) {
        try (Connection conn = sql2o.beginTransaction(java.sql.Connection.TRANSACTION_SERIALIZABLE)) {
            Query query = conn.createQuery(sql).throwOnMappingFailure(false);

            if (params != null) {
                for (Map.Entry<String, Object> entry :
                        params.entrySet()) {
                    query.addParameter(entry.getKey(), entry.getValue());
                }
            }
            int rows = query.executeUpdate().getResult();
            conn.commit();
            return rows;
        }
    }

    public static Object executeSql(String sql, Object paramsObj) {
        try (Connection conn = sql2o.beginTransaction(java.sql.Connection.TRANSACTION_SERIALIZABLE)) {
            Query query = conn.createQuery(sql).throwOnMappingFailure(false).bind(paramsObj);
            int rows = query.executeUpdate().getResult();
            conn.commit();
            return rows;
        }
    }


    public static PageBean selectPageBean(String sql,
                                          Object paramObj,
                                          PageBean pageBean) {
        List<Object> list = selectList(sql, paramObj);
        int count = count(sql, paramObj);
        pageBean.setList(list);
        pageBean.setTotalCount(count);
        return pageBean;
    }


    public static void main(String[] args) throws Exception {

        DBUtils.initDB("D:\\workspace\\idea\\f-blog\\f_blog.db");

        UserBean userBean = new UserBean();
        userBean.setLoginname("feihong");

        List<UserBean> userList = DBUtils.selectList("select * from user " +
                "where loginname =:loginname", userBean);
        System.out.println(userList.get(0).getId());


        Object obj = new PageBean();

        System.out.println(obj.getClass().getName());
    }
}
