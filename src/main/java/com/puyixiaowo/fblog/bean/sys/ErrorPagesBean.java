package com.puyixiaowo.fblog.bean.sys;

import java.io.Serializable;

public class ErrorPagesBean implements Serializable {
    private static final long serialVersionUID = 6833498301358559776L;
    private String code;
    private String redisKey;
    private String errorPage;
    private String errorDesc;

    public ErrorPagesBean() {
    }

    public ErrorPagesBean(String code, String redisKey, String errorPage, String errorDesc) {
        this.code = code;
        this.redisKey = redisKey;
        this.errorPage = errorPage;
        this.errorDesc = errorDesc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorPage() {
        return errorPage;
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }
}
