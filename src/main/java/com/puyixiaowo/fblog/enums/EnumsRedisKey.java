package com.puyixiaowo.fblog.enums;

public enum  EnumsRedisKey {
    REDIS_KEY_IGNORE_CONF("REDIS_KEY_IGNORE_CONF_", "忽略路径配置");


    EnumsRedisKey(String key, String description) {
        this.key = key;
        this.description = description;
    }
    public String key;
    public String description;
}
