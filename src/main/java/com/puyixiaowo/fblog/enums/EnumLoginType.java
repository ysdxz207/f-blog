package com.puyixiaowo.fblog.enums;

/**
 * 登录类型
 */
public enum EnumLoginType {

    LOGIN_TYPE_ADMIN("admin", "管理后台"),
    LOGIN_TYPE_BOOK("book", "书");


    EnumLoginType(String type, String description) {
        this.type = type;
        this.description = description;
    }


    public static EnumLoginType getEnumType(String type) {

        for (EnumLoginType enumCaptchaType :
                EnumLoginType.values()) {
            if (enumCaptchaType.type.equalsIgnoreCase(type)) {
                return enumCaptchaType;
            }
        }
        return null;
    }

    public String type;
    public String description;
}
