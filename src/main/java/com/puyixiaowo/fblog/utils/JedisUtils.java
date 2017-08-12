package com.puyixiaowo.fblog.utils;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import spark.utils.StringUtils;

import java.util.Properties;

/**
 * @author Moses
 * @date 2017-08-03 9:29
 */
public class JedisUtils {
    private static Jedis jedis;

    static {
        Properties properties = ResourceUtils.load("redis.properties");

        String host = properties.getProperty("redis.host");
        Integer port = Integer.valueOf(properties.getProperty("redis.port"));
        String auth = properties.getProperty("redis.password");
        jedis = new Jedis(host, port);
        jedis.auth(auth);
    }

    public static String get(String key){
        return jedis.get(key);
    }

    public static <T> T get(String key, Class<T> clazz){
        String str = jedis.get(key);
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return JSONObject.parseObject(str, clazz);
    }

    public static void set(String key, String value) {
        jedis.set(key, value);
    }

}
