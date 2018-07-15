package com.puyixiaowo.fblog.error;

import win.hupubao.common.error.Error;

public enum LoginError implements Error {
    EMPTY_CAPTCHA_ERROR("EMPTY_CAPTCHA_ERROR", "请输入验证码"),
    WRONG_CAPTCHA_ERROR("WRONG_CAPTCHA_ERROR", "验证码错误"),
    WRONG_USERNAME_OR_PASSWORD_ERROR("WRONG_USERNAME_OR_PASSWORD_ERROR", "用户名或密码错误"),
    NO_AUTH_ERROR("NO_AUTH_ERROR", "未登录");

    public String error_code;
    public String error_msg;

    private LoginError(String error_code, String error_msg) {
        this.error_code = error_code;
        this.error_msg = error_msg;
    }

    public String getErrorCode() {
        return this.error_code;
    }

    public String getErrorMsg() {
        return this.error_msg;
    }
}