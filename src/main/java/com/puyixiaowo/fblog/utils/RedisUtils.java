package com.puyixiaowo.fblog.utils;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.Properties;
import java.util.Set;

/**
 * @author Moses
 * @date 2017-08-03 9:29
 */
public class RedisUtils {
    private static Jedis jedis;

    static {
        Properties properties = ResourceUtils.load("redis.properties");

        String host = properties.getProperty("redis.host");
        Integer port = Integer.valueOf(properties.getProperty("redis.port"));
        String auth = properties.getProperty("redis.password");
        jedis = new Jedis(host, port);
        if (StringUtils.isNotBlank(auth)) {
            jedis.auth(auth);
        }
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

    public static Long delete(String... keys) {
        return jedis.del(keys);
    }

    public static Long delete(String pattern) {
        Set<String> keysSet = RedisUtils.keys(pattern);
        String [] keys = keysSet.toArray(new String[keysSet.size()]);
        if (keys.length == 0) {
            return 0L;
        }
        return RedisUtils.delete(keys);
    }
    public static Set<String> keys(String pattern){
        return jedis.keys(pattern);
    }

    public static <T> T getDefault(String key, Class<T> clazz, T defaultValue) {
        String str = jedis.get(key);
        if (StringUtils.isBlank(str)) {
            return defaultValue;
        }
        return JSONObject.parseObject(str, clazz);
    }
}
