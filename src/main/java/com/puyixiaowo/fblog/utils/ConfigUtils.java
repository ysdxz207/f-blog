package com.puyixiaowo.fblog.utils;

import com.alibaba.fastjson.JSON;
import com.puyixiaowo.fblog.constants.Constants;
import com.puyixiaowo.fblog.enums.EnumsRedisKey;
import com.puyixiaowo.fblog.error.ErrorHandler;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

/**
 * @author feihong
 * @date 2017-08-12 23:05
 */
public class ConfigUtils {

    private static final String ADMIN_CONFIG_FILE = "conf/admin_auth.yaml";
    private static final String IGNORE_LIST = "ignore_list";
    private static final String PASS_DES_KEY = "pass_des_key";

    /**
     * 初始化配置有顺序
     */
    public static void init() {
        initRedis();
        DBUtils.initDB();
        initAdminConf();
        ErrorHandler.init();
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
    }

    /**
     * 初始化redis配置
     */
    private static void initRedis() {
        RedisUtils.testConnection();
    }
}
