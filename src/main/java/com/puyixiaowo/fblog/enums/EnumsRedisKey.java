package com.puyixiaowo.fblog.enums;

public enum  EnumsRedisKey {
    REDIS_KEY_404_PAGE("REDIS_KEY_404_PAGE_", "404页面"),
    REDIS_KEY_IGNORE_CONF("REDIS_KEY_IGNORE_CONF_", "忽略路径配置"),
    REDIS_KEY_MENU_LEVEL("REDIS_KEY_MENU_LEVEL", "菜单层数"),
    REDIS_KEY_MENU_LIST_("REDIS_KEY_MENU_LIST_", "菜单列表");


    EnumsRedisKey(String key, String description) {
        this.key = key;
        this.description = description;
    }
    public String key;
    public String description;
}
