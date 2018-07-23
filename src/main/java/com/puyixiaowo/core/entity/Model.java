package com.puyixiaowo.core.entity;

import com.puyixiaowo.fblog.annotation.Id;
import com.puyixiaowo.fblog.annotation.Transient;
import com.puyixiaowo.fblog.exception.db.DBException;
import com.puyixiaowo.fblog.exception.db.DBObjectExistsException;
import com.puyixiaowo.fblog.exception.db.DBResultNotUniqueException;
import com.puyixiaowo.fblog.exception.db.DBSqlException;
import com.puyixiaowo.fblog.utils.ORMUtils;
import org.sql2o.*;
import spark.utils.Assert;
import win.hupubao.common.utils.CamelCaseUtils;
import win.hupubao.common.utils.LoggerUtils;
import win.hupubao.common.utils.ReflectionUtils;
import win.hupubao.common.utils.StringUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @param <E>
 * @@author W.feihong
 * jdbc.properties =>
 * <p>
 * url=xxx
 * username=xxx
 * password=xxx
 * sql.prefix.count=select count(*)
 */
public class Model<E> extends Validatable implements Serializable {

    private static final long serialVersionUID = 4922495587385402584L;

    private transient static final Properties dbProperties = convertResourceBundleToProperties(ResourceBundle.getBundle("jdbc"));
    private transient static final String SERIAL_VERSION_UID = "serialVersionUID";
    private transient static final String JOINPOINT_STATICPART_TYPE_NAME = "org.aspectj.lang.JoinPoint$StaticPart";
    private transient static final int SQL_TYPE_INSERT = 1;//添加
    private transient static final int SQL_TYPE_UPDATE = 2;//更新

    private transient static String COUNT_SQL_PREFIX = "select count(*)";
    private transient static Sql2o sql2o;
    private transient static String PRIMARY_KEY_NAME = "id";
    private transient static Class ID_GENERATER_CLASS = null;
    private transient static String ID_GENERATER_METHOD = "";

    public Model() {
        initDB();
        PRIMARY_KEY_NAME = getPrimaryKeyName();
    }

    public Model(Class clazz,
                 String methodName) {
        this();
        ID_GENERATER_CLASS = clazz;
        ID_GENERATER_METHOD = methodName;
    }

    /**
     * @param whereSql eg: username=:username and role_id=:roleId
     * @return
     */
    public List<E> where(String whereSql) {

        StringBuilder sqlStringBuilder = new StringBuilder("select * from ");

        Class clazz = this.getClass();
        sqlStringBuilder.append(ORMUtils.getTableNameByClass(clazz));
        sqlStringBuilder.append(" where ");

        if (StringUtils.isNotBlank(whereSql)) {
            sqlStringBuilder.append(whereSql);
        } else {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                field.setAccessible(true);

                if (!isParameter(field)) {
                    continue;
                }

                String column = fieldName;
                if (CamelCaseUtils.checkIsCamelCase(field.getName())) {
                    column = CamelCaseUtils.toUnderlineName(column);
                }
                sqlStringBuilder.append(column);
                sqlStringBuilder.append("=:");
                sqlStringBuilder.append(fieldName);
                sqlStringBuilder.append(" ");
            }
        }

        return selectList(sqlStringBuilder.toString());
    }


    public List<E> selectList(String sql) {

        return selectList(sql, buildParams());
    }



    @SuppressWarnings("unchecked")
    public List<E> selectList(String sql,
                              Map<String, Object> params) {
        Class<E> clazz = (Class<E>) this.getClass();
        return selectList(sql, params, clazz);
    }


    @SuppressWarnings("unchecked")
    public static <E> List<E> selectList(String sql,
                                         Map<String, Object> params,
                                         Class<E> clazz) {
        Model<E> model = new Model<>();

        model.setCamelMapping(clazz);
        try (Connection conn = sql2o.open()) {
            Query query = conn.createQuery(sql).throwOnMappingFailure(false);

            model.addParameters(params, query);

            return query.executeAndFetch(clazz);
        }
    }


    public E selectOne(String sql) {

        List<E> list = selectList(sql);
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            throw new DBResultNotUniqueException("Expected one result,but " + list.size());
        }
        return list.get(0);
    }


    @SuppressWarnings("unchecked")
    public E insertOrUpdate(boolean updateNull) {

        String tableName = ORMUtils.getTableNameByClass(this.getClass());

        try (Connection conn = sql2o.beginTransaction(java.sql.Connection.TRANSACTION_SERIALIZABLE)) {
            try {
                String sql_update = assembleSql(SQL_TYPE_UPDATE, tableName, this, updateNull);
                Query queryUpdate = conn.createQuery(sql_update).throwOnMappingFailure(false).bind(this);
                queryUpdate.executeUpdate();
            } catch (Exception e) {
                try {

                    ReflectionUtils.setFieldValue(this, PRIMARY_KEY_NAME, generateId());
                    String sql_insert = assembleSql(SQL_TYPE_INSERT, tableName, this, updateNull);
                    System.out.println(sql_insert);
                    conn.createQuery(sql_insert).throwOnMappingFailure(false).bind(this);
//                    Query queryInsert = conn.createQuery(sql_insert).throwOnMappingFailure(false).bind(this);
//                    Object primaryKey = queryInsert.executeUpdate().getKey();
                } catch (Sql2oException e1) {
                    if (e1.getMessage() != null
                            && e1.getMessage().contains("SQLITE_CONSTRAINT_UNIQUE")) {
                        throw new DBObjectExistsException("重复插入对象");
                    } else {
                        throw new DBException(e1.getMessage());
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            conn.commit();
            return (E) this;
        }
    }

    public int deleteOrUpdate(String sql) {
        return deleteOrUpdate(sql, buildParams());
    }

    public int deleteOrUpdate(String sql,
                              Map<String, Object> params) {
        try (Connection conn = sql2o.beginTransaction(java.sql.Connection.TRANSACTION_SERIALIZABLE)) {
            Query query = conn.createQuery(sql).throwOnMappingFailure(false).bind(this);
            if (params != null
                    && !params.isEmpty()) {
                addParameters(params, query);
            }
            int rows = query.executeUpdate().getResult();
            conn.commit();
            return rows;
        }
    }


    private String assembleSql(int sqlType,
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


                for (Field field : filelds) {
                    if (!isField(field)) {
                        continue;
                    }
                    String columnName = ORMUtils.getFieldColumnName(field);
                    Object fieldValue = getFieldValue(field);
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
                    sb2.append(":")
                            .append(field.getName());
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


                for (Field field : filelds) {
                    if (!isField(field)) {
                        continue;
                    }
                    String columnName = ORMUtils.getFieldColumnName(field);
                    Object fieldValue = getFieldValue(field);

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
                    sb1.append(":");
                    sb1.append(field.getName());
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


    public int count(String whereSql) {

        if (StringUtils.isBlank(whereSql)) {
            return 0;
        }
        if (whereSql.toLowerCase().contains("limit")) {
            whereSql = whereSql.replaceAll("limit +\\d+, *\\d+", "");
        }

        if (!whereSql.toLowerCase().replaceAll("\\s+", "").startsWith("selectcount")) {
            whereSql = COUNT_SQL_PREFIX + " from ("
                    + (whereSql.endsWith(";") ? whereSql.substring(0, whereSql.length() - 1) : whereSql)
                    + ")";
        }

        try (Connection conn = sql2o.open()) {
            Query query = conn.createQuery(whereSql).throwOnMappingFailure(false).bind(this);
            Integer count = query.executeScalar(Integer.class);
            if (count == null) {
                LoggerUtils.warn("统计sql可能不正确：{}", whereSql);
            }
            return count == null ? 0 : count;
        }
    }

    public int delete() {
        Object primaryKeyValue = ReflectionUtils.getFieldValue(this, PRIMARY_KEY_NAME);
        String tableName = ORMUtils.getTableNameByClass(this.getClass());
        String stringBuilderDeleteSql = "delete from " + tableName +
                " where " +
                PRIMARY_KEY_NAME +
                "=" +
                primaryKeyValue;
        return deleteOrUpdate(stringBuilderDeleteSql, new HashMap<>());
    }

    public int deleteByIds(String [] ids) {
        if (StringUtils.isBlank(ids)) {
            return 0;
        }
        List<String> list = Arrays.asList(ids);
        if (list.isEmpty() || list.get(0) == null) {
            return 0;
        }

        StringBuilder sb_del_sql = new StringBuilder("delete from ");
        String tableName = ORMUtils.getTableNameByClass(this.getClass());

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

    private Object getFieldValue(Field field) {
        try {
            field.setAccessible(true);
            return field.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void initDB() {
        if (sql2o != null) {
            return;
        }

        String url = dbProperties.getProperty("url");

        if (dbProperties.containsKey("username")) {
            dbProperties.setProperty("user", dbProperties.getProperty("username"));
            dbProperties.remove("username");
        }
        COUNT_SQL_PREFIX = dbProperties.containsKey("sql.prefix.count") ?
                dbProperties.getProperty("sql.prefix.count") : COUNT_SQL_PREFIX;

        DataSource dataSource = new GenericDatasource(url, dbProperties);

        sql2o = new Sql2o(dataSource);

        //设置临时目录路径
        String sqliteTempDir = System.getProperty("user.dir") + "/sqlite_temp";
        File tempFile = new File(sqliteTempDir);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        System.setProperty("org.sqlite.tmpdir",
                sqliteTempDir);
    }

    /**
     * 下划线映射为驼峰
     *
     * @param clazz
     */
    private void setCamelMapping(Class<E> clazz) {

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

    private static Properties convertResourceBundleToProperties(ResourceBundle resource) {
        Properties properties = new Properties();
        Enumeration<String> keys = resource.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            properties.put(key, resource.getString(key));
        }
        return properties;
    }

    private boolean isField(Field field) {
        return isParameter(field) && field.getAnnotation(Transient.class) == null;
    }

    private boolean isParameter(Field field) {
        String fieldName = field.getName();
        return !SERIAL_VERSION_UID.equals(fieldName)
                && getFieldValue(field) != null
                && !JOINPOINT_STATICPART_TYPE_NAME.equals(field.getGenericType().getTypeName());
    }

    private String getPrimaryKeyName() {
        for (Field field : this.getClass().getDeclaredFields()) {
            if (field.getAnnotation(Id.class) != null) {
                return CamelCaseUtils.toUnderlineName(field.getName());
            }
        }
        return PRIMARY_KEY_NAME;
    }

    private void addParameters(Map<String, Object> params,
                               Query query) {

        if (params == null
                || params.isEmpty()) {
            return;
        }
        for (Map.Entry entry : params.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }
            try {
                query.addParameter(entry.getKey().toString(), entry.getValue());
            } catch (Exception ignored) {

            }
        }
    }

    private Map<String, Object> buildParams() {

        Map<String, Object> params = new HashMap<>();
        Class clazz = this.getClass();
        if (Model.class.equals(clazz)) {
            return params;
        }
        Field [] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (isParameter(field)) {
                params.put(field.getName(), getFieldValue(field));
            }
        }
        return params;
    }

    @SuppressWarnings("unchecked")
    private String generateId() {
        Class clazz = ID_GENERATER_CLASS;

        Assert.notNull(clazz, "Constructor argument idGeneraterClass should not be null.");
        Method method = null;

        try {
            method = clazz.getDeclaredMethod(ID_GENERATER_METHOD);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Assert.notNull(method, "Can not get generate id method.");


        try {
            return (String) method.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;

    }

    //////////////
    public Sql2o getSql2o() {
        return sql2o;
    }
}
