package com.puyixiaowo.fblog.enums;

public enum  EnumsRedisKey {
    REDIS_KEY_IGNORE_CONF("REDIS_KEY_IGNORE_CONF_", "admin忽略路径配置"),
    REDIS_KEY_IGNORE_CONF_BOOK("REDIS_KEY_IGNORE_CONF_BOOK_", "book忽略路径配置"),
    REDIS_KEY_MENU_LIST("REDIS_KEY_MENU_LIST_", "菜单列表"),


    REDIS_KEY_REFRESH_MENU_LIST("REDIS_KEY_REFRESH_MENU_LIST_", "是否刷新菜单缓存"),
    REDIS_KEY_FNEWS_LIST("REDIS_KEY_FNEWS_LIST_", "新闻列表"),
    REDIS_KEY_SHORT_LINK("REDIS_KEY_SHORT_LINK_", "短链接"),
    REDIS_KEY_TOOLS_AUTOPUBLISH_CAPTCHA_COOKIES("REDIS_KEY_TOOLS_AUTOPUBLISH_CAPTCHA_COOKIES", "自动发布验证码cookie"),
    REDIS_KEY_TOOLS_AUTOPUBLISH_LOGIN_COOKIES("REDIS_KEY_TOOLS_AUTOPUBLISH_LOGIN_COOKIES", "自动发布登录cookie"),
    REDIS_KEY_TOOLS_AUTOPUBLISH_LOGIN_USERNAME("REDIS_KEY_TOOLS_AUTOPUBLISH_LOGIN_USERNAME", "自动发布登录用户名"),
    REDIS_KEY_TOOLS_AUTOPUBLISH_LOGIN_PUBPAGE("REDIS_KEY_TOOLS_AUTOPUBLISH_LOGIN_PUBPAGE", "自动发布发布页面");


    EnumsRedisKey(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String key;
    public String description;

}
