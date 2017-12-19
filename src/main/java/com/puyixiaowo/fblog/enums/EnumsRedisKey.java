package com.puyixiaowo.fblog.enums;

public enum  EnumsRedisKey {
    REDIS_KEY_404_PAGE("REDIS_KEY_404_PAGE_", "404页面"),
    REDIS_KEY_IGNORE_CONF("REDIS_KEY_IGNORE_CONF_", "admin忽略路径配置"),
    REDIS_KEY_IGNORE_CONF_BOOK("REDIS_KEY_IGNORE_CONF_BOOK_", "book忽略路径配置"),
    REDIS_KEY_MENU_LIST("REDIS_KEY_MENU_LIST_", "菜单列表"),


    REDIS_KEY_REFRESH_MENU_LIST("REDIS_KEY_REFRESH_MENU_LIST_", "是否刷新菜单缓存"),
    REDIS_KEY_FNEWS_LIST("REDIS_KEY_FNEWS_LIST_", "新闻列表"),
    REDIS_KEY_SHORT_LINK("REDIS_KEY_SHORT_LINK_", "短链接");


    EnumsRedisKey(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String key;
    public String description;

}
