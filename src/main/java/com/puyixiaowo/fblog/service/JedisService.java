package com.puyixiaowo.fblog.service;

import redis.clients.jedis.Jedis;

/**
 * @author Moses
 * @date 2017-08-03 9:29
 */
public class JedisService {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("puyixiaowo.win", 8002);
        jedis.auth("");

    }
}
