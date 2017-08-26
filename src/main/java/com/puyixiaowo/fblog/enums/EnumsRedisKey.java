package com.puyixiaowo.fblog.enums;

public enum  EnumsRedisKey {
    REDIS_KEY_404_PAGE("REDIS_KEY_404_PAGE_", "404页面"),
    REDIS_KEY_IGNORE_CONF("REDIS_KEY_IGNORE_CONF_", "忽略路径配置"),
    REDIS_KEY_MENU_LIST("REDIS_KEY_MENU_LIST_", "菜单列表"),


    REDIS_KEY_REFRESH_MENU_LIST("REDIS_KEY_REFRESH_MENU_LIST_", "是否刷新菜单缓存");


    EnumsRedisKey(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String key;
    public String description;

}
