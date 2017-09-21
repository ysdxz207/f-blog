package com.puyixiaowo.fblog.constants;

import com.puyixiaowo.fblog.utils.ResourceUtils;

public class Constants {
    /*
     * 后台用户session key
     */
    public static final String SESSION_USER_KEY = "fblog_session_admin_user_key";
    public static final int DEFAULT_PAGE_SIZE = 10;
    /*
     * 验证码session key
     */
    public static final String KAPTCHA_SESSION_KEY = "KAPTCHA_SESSION_KEY";
    /*
     * 成功状态码
     */
    public static final int RESPONSE_STATUS_CODE_SUCCESS = 200;
    /*
     * 错误状态码
     */
    public static final int RESPONSE_STATUS_CODE_ERROR = 300;
    /*
     * 成功描述
     */
    public static final String RESPONSE_SUCCESS_MESSAGE = "操作成功";


    /*
     * 密码des密钥
     */
    public static String PASS_DES_KEY = "20151106";
    public static final String SITE_ADMIN = ResourceUtils.load("conf/site.properties").getProperty("site.admin");
    public static final String SITE_YIYI = ResourceUtils.load("conf/site.properties").getProperty("site.yiyi");
    public static final String SITE_PUPU = ResourceUtils.load("conf/site.properties").getProperty("site.pupu");
}
