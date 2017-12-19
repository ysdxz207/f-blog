package com.puyixiaowo.fblog.enums;

/**
 * 验证码类型
 */
public enum EnumCaptchaType {

    CAPTCHA_TYPE_ADMIN(1, "管理后台"),
    CAPTCHA_TYPE_BOOK(2, "书");


    EnumCaptchaType(int type, String description) {
        this.type = type;
        this.description = description;
    }


    public static EnumCaptchaType getEnumType(int type) {

        for (EnumCaptchaType enumCaptchaType :
                EnumCaptchaType.values()) {
            if (enumCaptchaType.type == type) {
                return enumCaptchaType;
            }
        }
        return null;
    }

    public int type;
    public String description;
}
