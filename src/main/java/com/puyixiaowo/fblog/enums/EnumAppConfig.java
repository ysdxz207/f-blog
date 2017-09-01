package com.puyixiaowo.fblog.enums;
/**
 * 
 * @author Moses
 * @date 2017-09-01 13:39
 * 
 */
public enum EnumAppConfig {
    ARG_PORT("-p", "端口参数");

    EnumAppConfig(String arg, String description) {
        this.arg = arg;
        this.description = description;
    }

    public String arg;
    public String description;
}
