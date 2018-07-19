package com.puyixiaowo.fblog.utils;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.core.timer.TimerBackupDB;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.error.ErrorHandler;
import org.sql2o.Connection;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author W.feihong
 * @date 2017-08-12
 */
public class ConfigUtils {

    private static final String ADMIN_CONFIG_FILE = "conf/admin_auth.yaml";
    private static final String IGNORE_LIST = "ignore_list";
    private static final String PASS_DES_KEY = "pass_des_key";

    private static final String PATH_JDBC_PROPERTIES = "jdbc.properties";
    private static final String FOLDER_SQL = "sql";

    public static String DB_FILE_NAME = "";

    /**
     * 初始化配置有顺序
     */
    public static void init() {
        initRedis();
        initDB();
        initAdminConf();
        ErrorHandler.init();

        new TimerBackupDB().start();//启动备数据库份
//        new TimerFetchNews().start();//启动新闻获取定时器
    }

    /**
     * 初始化数据库
     */
    public static void initDB() {
        //设置临时目录路径
        String sqliteTempDir = System.getProperty("user.dir") + "/sqlite_temp";
        File tempFile = new File(sqliteTempDir);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        System.setProperty("org.sqlite.tmpdir",
                sqliteTempDir);

        try {
            DBUtils.initDB(PATH_JDBC_PROPERTIES);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = DBUtils.getDbProperties().getProperty("url");

        DB_FILE_NAME = url.substring(url.lastIndexOf(":") + 1);

        if (!new File(DB_FILE_NAME).exists()) {
            //创建数据库文件
            try (Connection conn = DBUtils.getSql2o().open()) {

                String[] filenames = ResourceUtils.getResourceFolderFiles(FOLDER_SQL);
                FileUtils.runResourcesSql(conn, FOLDER_SQL, filenames);
            }
            //清空redis
            EnumsRedisKey[] enumsRedisKeys = EnumsRedisKey.values();
            String[] keys = new String[enumsRedisKeys.length];
            for (int i = 0; i < enumsRedisKeys.length; i++) {
                keys[i] = enumsRedisKeys[i].key + "*";
            }
            RedisUtils.delete(keys);
        }
    }

    /**
     * chu初始化后台登录链接配置以及密钥配置
     */
    private static void initAdminConf() {
        Yaml yaml = new Yaml();
        Object obj = yaml.load(ResourceUtils.readFile(ADMIN_CONFIG_FILE));

        if (!(obj instanceof Map)) {
            throw new RuntimeException("后台用户登录链接配置不正确");
        }
        Map<String, Object> map = (Map) obj;

        Object ignoresObj = map.get(IGNORE_LIST);

        List<String> ignores = null;
        if (ignoresObj instanceof List) {
            ignores = (List<String>) ignoresObj;
        }
        Object keyObj = map.get(PASS_DES_KEY);
        String key = null;
        if (keyObj instanceof String) {
            key = keyObj.toString();
        }

        Constants.PASS_DES_KEY = key;

        if (ignores == null) {
            throw new RuntimeException("后台用户权限配置不正确");
        }

        RedisUtils.set(EnumsRedisKey.REDIS_KEY_IGNORE_CONF.key, JSON.toJSONString(ignores));

        //跨域
        String allowString = ResourceUtils.load("conf/common.properties").getProperty("origin.access.allow.url").trim();
        Constants.ALLOWED_ORIGINS = StringUtils.isNotBlank(allowString) ? allowString.split(",") : new String[0];
    }


    /**
     * 初始化redis配置
     */
    private static void initRedis() {
        RedisUtils.testConnection();
    }
}
