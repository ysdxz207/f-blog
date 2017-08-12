package com.puyixiaowo.fblog.enums;

public enum  EnumsRedisKey {
    REDIS_KEY_404_PAGE_KEY("REDIS_KEY_404_PAGE_KEY_", "404页面"),
    REDIS_KEY_IGNORE_CONF_KEY("REDIS_KEY_IGNORE_CONF_KEY_", "忽略路径配置");


    EnumsRedisKey(String key, String description) {
        this.key = key;
        this.description = description;
    }
    public String key;
    public String description;
}
